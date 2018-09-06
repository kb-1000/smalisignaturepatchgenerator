package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.signature.SignatureLoader
import com.google.common.io.ByteStreams
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.rewriter.DexRewriter
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class PatchGenerator(val signatureLoaderParameter: Any? = null, val signatureVerificationTypesCallback: (SignatureVerificationTypes) -> Unit = {}) : Runnable {
    private val thread = Thread(this)

    val inputQueue: BlockingQueue<InputMessage> = LinkedBlockingQueue()
    val outputQueue: BlockingQueue<OutputMessage> = LinkedBlockingQueue()

    private fun identifySignatureVerificationTypes(dexFile: DexFile): Pair<DexFile, SignatureVerificationTypes> {
        val signatureVerificationTypes = SignatureVerificationTypes()
        val rebuiltDexFile = rebuildDexFile(DexRewriter(IdentificationRewriterModule(signatureVerificationTypes)).rewriteDexFile(dexFile))
        return Pair(rebuiltDexFile, signatureVerificationTypes)

    }

    private var dexFile: DexFile? = null

    var identifiedSignatureVerificationTypes: SignatureVerificationTypes = SignatureVerificationTypes()
        get() = field.copy()
        private set

    @Throws(InvalidApkFileException::class)
    private fun loadMainApkFile(file: File) {
        val zipFile = java.util.zip.ZipFile(file, ZipFile.OPEN_READ) //TODO: catch errors
        val zipEntry = zipFile.getEntry("classes.dex")
                ?: throw InvalidApkFileException("The APK file is invalid because it has no classes.dex.")
        val inputStream = zipFile.getInputStream(zipEntry)
        val ret = identifySignatureVerificationTypes(DexBackedDexFile(null, ByteStreams.toByteArray(inputStream)))
        this.dexFile = ret.first
        if(identifiedSignatureVerificationTypes != ret.second) {
            identifiedSignatureVerificationTypes = ret.second
            signatureVerificationTypesCallback(identifiedSignatureVerificationTypes)
        }
    }

    private var signatures: List<ByteArray>? = null

    private fun loadSignatureApk(file: File) {
        signatures = SignatureLoader.loadSignature(file, signatureLoaderParameter)
    }


    @Suppress("UNUSED") // The additional constructors should be there once they are used.
    class InvalidApkFileException : Exception {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }

    override fun run() {
        try {
            loop@ while (true) {
                val message = inputQueue.take()
                when (message) {
                    is Stop -> break@loop
                    is ChangeMainApk -> loadMainApkFile(message.file)
                    is ChangeSignatureApk -> loadSignatureApk(message.file)
                    is Generate -> generatePatch(message.file, message.signatureVerificationTypes)
                    else -> throw UnsupportedOperationException("\"${message::class.java.simpleName}\" is currently not implemented.")
                }
            }
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun generatePatch(file: File, signatureVerificationTypes: SignatureVerificationTypes) {
        FileOutputStream(file).use { fileOutputStream ->
            ZipOutputStream(fileOutputStream).use { topLevelZipOutputStream ->
                topLevelZipOutputStream.putNextEntry(ZipEntry("patch.txt"))
                topLevelZipOutputStream.bufferedWriter(Charsets.UTF_8)
            }
        }
    }

    fun start() {
        thread.start()
    }

    fun join() {
        thread.join()
    }
}

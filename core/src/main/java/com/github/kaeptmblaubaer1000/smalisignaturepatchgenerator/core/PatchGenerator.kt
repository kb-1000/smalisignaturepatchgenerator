package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.verifySelectedSignatureVerificationTypes
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.metadata.MetadataLoader
import org.jetbrains.annotations.Contract
import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.rewriter.DexRewriter
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class PatchGenerator(val signatureLoaderParameter: Any? = null, val signatureVerificationTypesCallback: (SignatureVerificationTypes) -> Unit = {}) : Runnable {
    private val thread = Thread(this)

    val inputQueue: BlockingQueue<InputMessage> = LinkedBlockingQueue()
    val outputQueue: BlockingQueue<OutputMessage> = LinkedBlockingQueue()
    val packageName: String? = null

    private var dexFile: DexFile? = null

    var identifiedSignatureVerificationTypes: SignatureVerificationTypes = SignatureVerificationTypes()
        @Contract(" -> new")
        get() = field.copy()
        private set

    @Throws(InvalidApkFileException::class)
    private fun loadMainApkFile(file: File) {
        val signatureVerificationTypes = SignatureVerificationTypes()
        this.dexFile = rebuildDexFile(DexRewriter(IdentificationRewriterModule(signatureVerificationTypes)).rewriteDexFile(DexFileFactory.loadDexFile(File(file, "classes.dex"), null)))
        if (identifiedSignatureVerificationTypes != signatureVerificationTypes) {
            identifiedSignatureVerificationTypes = signatureVerificationTypes
            signatureVerificationTypesCallback(identifiedSignatureVerificationTypes)
        }
    }

    private var signatures: List<ByteArray>? = null

    private fun loadSignatureApk(file: File) {
        signatures = MetadataLoader.loadSignature(file, signatureLoaderParameter)
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
        if (!verifySelectedSignatureVerificationTypes(identifiedSignatureVerificationTypes, signatureVerificationTypes)) {
            throw IllegalArgumentException("$signatureVerificationTypes selected, but only $identifiedSignatureVerificationTypes available")
        }

        FileOutputStream(file).use { fileOutputStream ->
            ZipOutputStream(fileOutputStream).use { topLevelZipOutputStream ->
                topLevelZipOutputStream.putNextEntry(ZipEntry("patch.txt"))
                topLevelZipOutputStream.bufferedWriter(Charsets.UTF_8).use { bufferedPatch ->
                    bufferedPatch.write("""
[MIN_ENGINE_VER]
1

[AUTHOR]
SmaliSignaturePatchGenerator (https://github.com/kaeptmblaubaer1000/SmaliSignaturePatchGenerator)

[PACKAGE]
${packageName!!}
""")
                }
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

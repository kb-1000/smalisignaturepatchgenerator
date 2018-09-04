package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.rewriter.DexRewriter
import java.io.File
import java.util.concurrent.BlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.zip.ZipFile

class PatchGenerator : Runnable {
    private val thread = Thread(this)

    val inputQueue: BlockingQueue<InputMessage> = SynchronousQueue(true)
    val outputQueue: BlockingQueue<OutputMessage> = SynchronousQueue(true)

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
        val ret = identifySignatureVerificationTypes(DexBackedDexFile.fromInputStream(null, inputStream))
        this.dexFile = ret.first
        this.identifiedSignatureVerificationTypes = ret.second
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
                    else -> throw UnsupportedOperationException("\"${message::class.java.simpleName}\" is currently not implemented.")
                }
            }
        } catch (e: Throwable) {
            outputQueue.addAll(List(10) { null as OutputMessage? }) // To ensure any read will not block but fail.
        }
    }

    fun start() {
        thread.start()
    }

    fun join() {
        thread.join()
    }
}

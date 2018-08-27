package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.rewriter.DexRewriter
import java.io.File
import java.util.zip.ZipFile

class PatchGenerator {
    fun identifySignatureVerificationTypes(dexFile: DexFile): Pair<DexFile, SignatureVerificationTypes> {
        val signatureVerificationTypes = SignatureVerificationTypes()
        val rebuiltDexFile = rebuildDexFile(DexRewriter(IdentificationRewriterModule(signatureVerificationTypes)).rewriteDexFile(dexFile))
        return Pair(rebuiltDexFile, signatureVerificationTypes)
    }

    @Throws(InvalidApkFileException::class)
    fun loadApkFile(file: File): DexFileWrapper {
        val zipFile = java.util.zip.ZipFile(file, ZipFile.OPEN_READ) //TODO: catch errors
        val zipEntry = zipFile.getEntry("classes.dex")
                ?: throw InvalidApkFileException("The APK file is invalid because it has no classes.dex.")
        val inputStream = zipFile.getInputStream(zipEntry)
        return DexFileWrapper(DexBackedDexFile.fromInputStream(null, inputStream))
    }


    @Suppress("UNUSED") // The additional constructors should be there once they are used.
    class InvalidApkFileException : Exception {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }
}

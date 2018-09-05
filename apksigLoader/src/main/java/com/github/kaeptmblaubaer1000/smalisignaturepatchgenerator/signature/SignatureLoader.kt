package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.signature

import com.android.apksig.ApkVerifier
import java.io.File

object SignatureLoader {
    fun loadSignature(apkFile: File, any: Any?): List<ByteArray> {
        val result: ApkVerifier.Result = ApkVerifier.Builder(apkFile).build().verify()
        return result.signerCertificates.map { it.encoded }
    }
}
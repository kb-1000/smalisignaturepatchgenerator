package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.signature

import android.content.Context
import android.content.pm.PackageManager
import java.io.File

/**
 * This implementation is based on [PackageManager], because apksig uses [java.math.BigInteger] API that's not supported on Android.
 */
object SignatureLoader {
    fun loadSignature(apkFile: File, context: Any?): List<ByteArray> {
        return (context as Context).packageManager.getPackageArchiveInfo(apkFile.absolutePath, PackageManager.GET_SIGNATURES).signatures.map { it.toByteArray() }
    }
}
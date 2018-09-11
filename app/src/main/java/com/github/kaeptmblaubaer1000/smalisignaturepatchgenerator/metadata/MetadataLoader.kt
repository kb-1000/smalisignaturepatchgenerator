package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.metadata

import android.content.Context
import android.content.pm.PackageManager
import java.io.File

/**
 * This implementation is based on [PackageManager], because apksig uses [java.math.BigInteger] API that's not supported on Android.
 */
object MetadataLoader {
    fun loadSignature(apkFile: File, context: Any?): List<ByteArray> {
        return (context as Context).packageManager.getPackageArchiveInfo(apkFile.absolutePath, PackageManager.GET_SIGNATURES).signatures.map { it.toByteArray() }
    }

    fun getPackageName(apkFile: File, context: Any?): String {
        return (context as Context).packageManager.getPackageArchiveInfo(apkFile.absolutePath, 0).packageName
    }
}

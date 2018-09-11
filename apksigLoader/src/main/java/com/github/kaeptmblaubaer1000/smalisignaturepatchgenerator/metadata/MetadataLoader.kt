package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.metadata

import com.android.apksig.ApkVerifier
import java.io.File
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems

@SuppressWarnings("NewApi")
object MetadataLoader {
    fun loadSignature(apkFile: File, any: Any?): List<ByteArray> {
        val result: ApkVerifier.Result = ApkVerifier.Builder(apkFile).build().verify()
        return result.signerCertificates.map { it.encoded }
    }

    fun getPackageName(apkFile: File, any: Any?): String {
        getOrNewFileSystem(URI("jar:${apkFile.toURI()}")).use { fileSystem ->
            val manifest = fileSystem.rootDirectories.first().resolve("AndroidManifest.xml")
            println(manifest)
        }
        return ""
    }

    @JvmStatic
    private fun getOrNewFileSystem(uri: URI): FileSystem {
        return try {
            FileSystems.getFileSystem(uri)
        } catch (e: FileSystemNotFoundException) {
            FileSystems.newFileSystem(uri, emptyMap<String, Any>())
        }
    }
}
package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.metadata

import com.android.apksig.ApkVerifier
import com.android.apksig.internal.apk.AndroidBinXmlParser
import java.io.File
import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.FileSystem
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardOpenOption

@SuppressWarnings("NewApi")
object MetadataLoader {
    fun loadSignature(apkFile: File, any: Any?): List<ByteArray> {
        val result: ApkVerifier.Result = ApkVerifier.Builder(apkFile).build().verify()
        return result.signerCertificates.map { it.encoded }
    }

    fun getPackageName(apkFile: File, any: Any?): String {
        getOrNewFileSystem(URI("jar:${apkFile.toURI()}")).use { fileSystem ->
            val manifest = fileSystem.rootDirectories.first().resolve("AndroidManifest.xml")
            val buffer = ByteBuffer.allocate(Files.size(manifest).toInt())
            Files.newByteChannel(manifest, StandardOpenOption.READ).read(buffer)
            buffer.flip()
            val parser = AndroidBinXmlParser(buffer)
            var eventType = parser.eventType
            while (eventType != AndroidBinXmlParser.EVENT_END_DOCUMENT) {
                if (eventType == AndroidBinXmlParser.EVENT_START_ELEMENT && parser.depth == 1 && parser.name == "manifest" && parser.namespace.isEmpty()) {
                    for (i in 0 until parser.attributeCount) {
                        if (parser.getAttributeName(i) == "package") {
                            val valueType = parser.getAttributeValueType(i)
                            when (valueType) {
                                AndroidBinXmlParser.VALUE_TYPE_STRING -> return parser.getAttributeStringValue(i)
                                else -> throw InvalidApkFileException("The AndroidManifest.xml manifest[package] attribute is not a string")
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
            throw InvalidApkFileException("The AndroidManifest.xml has no <manifest> element")
        }
    }

    @Suppress("UNUSED") // The additional constructors should be there once they are used.
    class InvalidApkFileException : Exception {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
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

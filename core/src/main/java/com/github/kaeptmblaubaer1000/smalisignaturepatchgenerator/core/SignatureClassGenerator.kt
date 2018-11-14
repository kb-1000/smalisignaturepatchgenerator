package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import java.math.BigInteger
import java.security.MessageDigest
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.iface.Annotation
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.Field
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.base.reference.BaseTypeReference

fun generateSignatureClass(signatureData: String): ClassDef {
    return object : BaseTypeReference(), ClassDef {
        private val type: String = run {
            val md = MessageDigest.getInstance("SHA-512")
            md.update(signatureData.toByteArray(Charsets.US_ASCII))
            val digest = md.digest()
            "Signature${BigInteger(digest).toString(16).padStart(digest.size * 2, '0')}"
        }

        override fun getType() = type

        override fun getAnnotations() = emptySet<Annotation>()

        override fun getFields() = staticFields

        private val directMethods: Iterable<Method> = emptyList()

        override fun getDirectMethods() = directMethods
	
        override fun getMethods() = directMethods

        override fun getSuperclass(): String? = null

        private val staticFields: Iterable<Field> = emptySet()

        override fun getStaticFields() = staticFields

        override fun getInstanceFields(): Iterable<Field> = emptyList()

        private val accessFlags: Int = AccessFlags.PUBLIC.ordinal

        override fun getAccessFlags() = accessFlags

        override fun getInterfaces(): List<String> = emptyList()

        override fun getSourceFile(): String? = null

        override fun getVirtualMethods(): Iterable<Method> = emptyList()
    }
}

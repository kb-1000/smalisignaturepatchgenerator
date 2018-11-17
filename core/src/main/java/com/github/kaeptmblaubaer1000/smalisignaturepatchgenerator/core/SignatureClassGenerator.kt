package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import java.math.BigInteger
import java.security.MessageDigest
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.ReferenceType
import org.jf.dexlib2.analysis.RegisterType
import org.jf.dexlib2.iface.Annotation
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.Field
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.base.reference.BaseMethodReference
import org.jf.dexlib2.base.reference.BaseTypeReference
import org.jf.dexlib2.iface.ExceptionHandler
import org.jf.dexlib2.iface.MethodImplementation
import org.jf.dexlib2.iface.MethodParameter
import org.jf.dexlib2.iface.TryBlock
import org.jf.dexlib2.iface.debug.DebugItem
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.formats.Instruction10x
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.reference.Reference

private val emptyList: List<Nothing> = emptyList()
private val emptySet: Set<Nothing> = emptySet()

fun generateSignatureClass(signatureData: String): ClassDef {
    return object : BaseTypeReference(), ClassDef {
        private val type: String = run {
            val md = MessageDigest.getInstance("SHA-512")
            md.update(signatureData.toByteArray(Charsets.US_ASCII))
            val digest = md.digest()
            "Signature${BigInteger(digest).toString(16).padStart(digest.size * 2, '0')}"
        }

        abstract inner class BaseMethod: BaseMethodReference(), Method {
            override fun getReturnType() = "V"
            override fun getDefiningClass() = type
            override fun getParameterTypes(): List<CharSequence> = emptyList
            override fun getParameters(): List<MethodParameter> = emptyList
            override fun getAnnotations(): Set<Annotation> = emptySet
        }

        abstract inner class BaseMethodImplementation : MethodImplementation {
            override fun getDebugItems(): Iterable<DebugItem> = emptyList
            override fun getTryBlocks(): List<TryBlock<ExceptionHandler>> = emptyList
        }

        abstract inner class BaseInstruction : Instruction {
            override fun getCodeUnits() = opcode.format.size / 2
        }

        override fun getType() = type

        override fun getAnnotations(): Set<Annotation> = emptySet

        override fun getFields() = staticFields

        private val directMethods: Iterable<Method> = listOf(object : BaseMethod() {
            override fun getName(): String = "<init>"
            override fun getAccessFlags(): Int = AccessFlags.PRIVATE.ordinal

            private val implementation: MethodImplementation = object : BaseMethodImplementation() {
                override fun getRegisterCount() = 1

                override fun getInstructions(): Iterable<Instruction> = listOf(object : BaseInstruction(), Instruction35c {
                    override fun getReferenceType() = ReferenceType.METHOD
                    override fun getRegisterCount() = 1
                    override fun getRegisterF() = 0
                    override fun getRegisterG() = 0
                    override fun getOpcode() = Opcode.INVOKE_DIRECT
                    override fun getRegisterC() = 0
                    override fun getRegisterD() = 0
                    override fun getRegisterE() = 0

                    private val reference = object : BaseMethodReference() {
                        override fun getName() = "<init>"
                        override fun getReturnType() = "V"
                        override fun getParameterTypes(): List<CharSequence> = emptyList
                        override fun getDefiningClass() = "Ljava/lang/Object;"

                    }
                    override fun getReference(): Reference = reference
                }, object : BaseInstruction(), Instruction10x {
                    override fun getOpcode() = Opcode.RETURN_VOID
                })
            }
            override fun getImplementation(): MethodImplementation? = implementation
        })

        override fun getDirectMethods() = directMethods
	
        override fun getMethods() = directMethods

        override fun getSuperclass(): String? = null

        private val staticFields: Iterable<Field> = listOf()

        override fun getStaticFields() = staticFields

        override fun getInstanceFields(): Iterable<Field> = emptyList

        private val accessFlags: Int = AccessFlags.PUBLIC.ordinal or AccessFlags.FINAL.ordinal

        override fun getAccessFlags() = accessFlags

        override fun getInterfaces(): List<String> = emptyList

        override fun getSourceFile(): String? = null

        override fun getVirtualMethods(): Iterable<Method> = emptyList
    }
}

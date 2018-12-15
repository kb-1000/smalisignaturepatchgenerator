package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.ReferenceType
import org.jf.dexlib2.base.reference.BaseFieldReference
import org.jf.dexlib2.base.reference.BaseMethodReference
import org.jf.dexlib2.base.reference.BaseTypeReference
import org.jf.dexlib2.iface.Annotation
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.ExceptionHandler
import org.jf.dexlib2.iface.Field
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.MethodImplementation
import org.jf.dexlib2.iface.MethodParameter
import org.jf.dexlib2.iface.TryBlock
import org.jf.dexlib2.iface.debug.DebugItem
import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.formats.Instruction10x
import org.jf.dexlib2.iface.instruction.formats.Instruction11n
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.instruction.formats.Instruction22c
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.reference.Reference
import org.jf.dexlib2.iface.reference.TypeReference
import org.jf.dexlib2.iface.value.EncodedValue
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction11n
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction21c
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction23x
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction31c
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction35c
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference
import org.jf.dexlib2.immutable.reference.ImmutableStringReference
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference
import java.security.MessageDigest

private val emptyList: List<Nothing> = emptyList()
private val emptySet: Set<Nothing> = emptySet()

fun generateSignatureClass(signatureData: List<String>): ClassDef {
    if (signatureData.size > 7) {
        //Because every application where I counted only had one Signature, this is a reasonable limit that saves space in the resulting DEX file.
        throw UnsupportedOperationException("It's currently impossible to use more than seven Signatures")
    }
    return object : BaseTypeReference(), ClassDef {
        private val type: String = run {
            val md = MessageDigest.getInstance("SHA-512")
            md.update(signatureData.joinTo(StringBuilder(), "\n").toString().toByteArray(Charsets.US_ASCII))
            val digest = md.digest()
            "LSignature${digest.toHexString()};"
        }

        private inline fun className(function: () -> Unit): String {
            function()
            return type
        }

        abstract inner class BaseMethod : BaseMethodReference(), Method {
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

        abstract inner class BaseConst4Instruction : BaseInstruction(), Instruction11n {
            override fun getOpcode() = Opcode.CONST_4
            override fun getWideLiteral() = narrowLiteral.toLong()
        }

        abstract inner class BaseFiveRegisterInstruction : BaseInstruction(), FiveRegisterInstruction {
            override fun getRegisterCount() = 0
            override fun getRegisterC() = 0
            override fun getRegisterD() = 0
            override fun getRegisterE() = 0
            override fun getRegisterF() = 0
            override fun getRegisterG() = 0
        }

        abstract inner class BaseInvokeDirectInstruction : BaseFiveRegisterInstruction(), Instruction35c {
            override fun getReferenceType() = ReferenceType.METHOD
            override fun getOpcode() = Opcode.INVOKE_DIRECT
        }

        private val signatureArrayType: String = "[Landroid/content/pm/Signature;"

        private inline fun getListStaticFields(function: () -> Unit): List<Field> {
            function()
            return staticFields
        }

        private val staticFields = listOf(object : BaseFieldReference(), Field {
            override fun getDefiningClass() = className {}
            override fun getName() = "signatures"
            private val type = signatureArrayType
            override fun getType() = type
            private val accessFlags = AccessFlags.STATIC.value or AccessFlags.PUBLIC.value or AccessFlags.FINAL.value
            override fun getAccessFlags() = accessFlags
            override fun getInitialValue(): EncodedValue? = null
            override fun getAnnotations(): Set<Annotation> = emptySet
        })

        private val returnVoidInstruction = object : BaseInstruction(), Instruction10x {
            override fun getOpcode() = Opcode.RETURN_VOID
        }

        override fun getType() = type

        override fun getAnnotations(): Set<Annotation> = emptySet

        override fun getFields() = staticFields

        private val directMethods: Iterable<Method> = listOf(object : BaseMethod() {
            override fun getName(): String = "<init>"
            override fun getAccessFlags(): Int = AccessFlags.PRIVATE.value or AccessFlags.CONSTRUCTOR.value

            private val implementation: MethodImplementation = object : BaseMethodImplementation() {
                override fun getRegisterCount() = 1

                private val instructions: Iterable<Instruction> = listOf(object : BaseInvokeDirectInstruction() {
                    override fun getRegisterCount() = 1
                    override fun getRegisterC() = 0

                    private val reference = object : BaseMethodReference() {
                        override fun getName() = "<init>"
                        override fun getReturnType() = "V"
                        override fun getParameterTypes(): List<CharSequence> = emptyList
                        override fun getDefiningClass() = "Ljava/lang/Object;"

                    }

                    override fun getReference(): Reference = reference
                }, returnVoidInstruction)

                override fun getInstructions() = instructions
            }

            override fun getImplementation(): MethodImplementation? = implementation
        }, object : BaseMethod() {
            override fun getName() = "<clinit>"
            override fun getAccessFlags() = AccessFlags.STATIC.value or AccessFlags.CONSTRUCTOR.value

            private val implementation: MethodImplementation = object : BaseMethodImplementation() {
                override fun getRegisterCount() = 4
                private val instructions: Iterable<Instruction> = listOf(object : BaseConst4Instruction() {
                    override fun getNarrowLiteral() = signatureData.size
                    override fun getRegisterA() = 0
                }, object : BaseInstruction(), Instruction22c {
                    override fun getOpcode() = Opcode.NEW_ARRAY
                    override fun getRegisterA() = 0
                    override fun getRegisterB() = 0
                    override fun getReferenceType() = ReferenceType.TYPE
                    private val reference: Reference = ImmutableTypeReference(signatureArrayType)
                    override fun getReference() = reference
                }, *signatureData.foldIndexed(ArrayList(signatureData.size * 4)) { index: Int, arrayList: ArrayList<Instruction>, s: String ->
                    arrayList.add(ImmutableInstruction21c(Opcode.NEW_INSTANCE, 1,  ImmutableTypeReference("Landroid/content/pm/Signature;")))
                    arrayList.add(ImmutableInstruction31c(Opcode.CONST_STRING_JUMBO, 2, ImmutableStringReference(s)))
                    arrayList.add(ImmutableInstruction35c(Opcode.INVOKE_DIRECT, 2, 1, 2, 0, 0, 0, ImmutableMethodReference("Landroid/content/pm/Signature;", "<init>", listOf("Ljava/lang/String;"), "V")))
                    arrayList.add(ImmutableInstruction11n(Opcode.CONST_4, 3, index))
                    arrayList.add(ImmutableInstruction23x(Opcode.APUT_OBJECT, 1, 0, 3))
                    arrayList
                }.toTypedArray(), object : BaseInstruction(), Instruction21c {
                    override fun getOpcode() = Opcode.SPUT_OBJECT
                    private val reference: Reference = getListStaticFields({})[0]
                    override fun getReference() = reference
                    override fun getReferenceType() = ReferenceType.FIELD
                    override fun getRegisterA() = 0
                })

                override fun getInstructions() = instructions
            }

            override fun getImplementation() = implementation
        })

        override fun getDirectMethods() = directMethods

        override fun getMethods() = directMethods

        override fun getSuperclass(): String? = "Ljava/lang/Object;"

        override fun getStaticFields(): Iterable<Field> = staticFields

        override fun getInstanceFields(): Iterable<Field> = emptyList

        private val accessFlags: Int = AccessFlags.PUBLIC.value or AccessFlags.FINAL.value

        override fun getAccessFlags() = accessFlags

        override fun getInterfaces(): List<String> = emptyList

        override fun getSourceFile(): String? = null

        override fun getVirtualMethods(): Iterable<Method> = emptyList
    }
}

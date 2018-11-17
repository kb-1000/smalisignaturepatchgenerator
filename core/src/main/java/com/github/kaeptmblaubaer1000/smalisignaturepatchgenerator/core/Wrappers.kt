package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import org.jf.dexlib2.ReferenceType
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.MethodImplementation
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc

abstract class Wrapped<T>(val methodName: String) {
    abstract fun unwrap(): T
}

class WrappedMethod(private val method: Method) : Method by method, Wrapped<Method>(method.toString()) {
    override fun getImplementation(): MethodImplementation? {
        return WrappedMethodImplementation(method.implementation ?: return null, methodName)
    }

    override fun unwrap() = method
}

class WrappedMethodImplementation(private val methodImplementation: MethodImplementation, methodName: String) : MethodImplementation by methodImplementation, Wrapped<MethodImplementation>(methodName) {
    override fun unwrap() = methodImplementation

    override fun getInstructions(): Iterable<Instruction> = methodImplementation.instructions.map {
        when (it) {
            is ReferenceInstruction -> when (it.referenceType) {
                ReferenceType.METHOD -> when (it) {
                    is Instruction35c -> WrappedInstruction35c(it, methodName)
                    is Instruction3rc -> WrappedInstruction3rc(it, methodName)
                    else -> it
                }
                else -> it
            }
            else -> it
        }
    }
}

class WrappedInstruction35c(private val instruction35c: Instruction35c, methodName: String) : Wrapped<Instruction35c>(methodName), Instruction35c by instruction35c {
    override fun unwrap() = instruction35c
}

class WrappedInstruction3rc(private val instruction3rc: Instruction3rc, methodName: String) : Wrapped<Instruction3rc>(methodName), Instruction3rc by instruction3rc {
    override fun unwrap() = instruction3rc
}

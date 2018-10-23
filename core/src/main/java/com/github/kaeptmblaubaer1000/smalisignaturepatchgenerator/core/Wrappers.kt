package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.MethodImplementation


interface Wrapped<T> {
    fun unwrap(): T
}

class WrappedMethod(private val method: Method) : Method by method, Wrapped<Method> {
    override fun getImplementation(): MethodImplementation? {
        return WrappedMethodImplementation(method.implementation ?: return null)
    }

    override fun unwrap() = method
}

class WrappedMethodImplementation(private val methodImplementation: MethodImplementation) : MethodImplementation by methodImplementation, Wrapped<MethodImplementation> {
    override fun unwrap() = methodImplementation

    override fun getInstructions(): Iterable<Instruction> = methodImplementation.instructions.map {
        when(it) {
            else -> it
        }
    }
}

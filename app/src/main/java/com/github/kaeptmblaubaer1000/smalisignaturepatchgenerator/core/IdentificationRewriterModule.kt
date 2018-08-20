package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.IdentificationMethodRewriter
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.rewriter.Rewriter
import org.jf.dexlib2.rewriter.RewriterModule
import org.jf.dexlib2.rewriter.Rewriters

class IdentificationRewriterModule : RewriterModule() {
    override fun getMethodRewriter(rewriters: Rewriters): Rewriter<Method> {
        return IdentificationMethodRewriter(rewriters)
    }
}
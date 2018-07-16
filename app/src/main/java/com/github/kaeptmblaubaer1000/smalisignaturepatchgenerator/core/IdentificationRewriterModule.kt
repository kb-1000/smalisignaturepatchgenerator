package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.IdentificationClassDefRewriter
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.rewriter.Rewriter
import org.jf.dexlib2.rewriter.RewriterModule
import org.jf.dexlib2.rewriter.Rewriters

class IdentificationRewriterModule : RewriterModule() {
    override fun getClassDefRewriter(rewriters: Rewriters): Rewriter<ClassDef> {
        return IdentificationClassDefRewriter(rewriters)
    }
}
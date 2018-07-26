package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

class Parser {
    private val mutablePatchDefs: MutableMap<String, PatchDef> = mutableMapOf()
    val patchDefs: Map<String, PatchDef>
        get() = mutablePatchDefs

    fun parse(string: String) {
        val lexer = PatchDefLexer(CharStreams.fromString(string))
        val tokens = CommonTokenStream(lexer)
        val parser = PatchDefParser(tokens)
        val parserContext = parser.rootParser()
        for (ctx in parserContext.patch()) {
            patch(unescapeJavaString(ctx.StringLiteral().text)) {
                ParseTreeWalker().walk(object : PatchDefBaseListener() {
                    override fun exitHumanNameAssignment(ctx: PatchDefParser.HumanNameAssignmentContext?) {
                        humanName = unescapeJavaString((ctx ?: return).StringLiteral().text)
                    }

                    override fun enterModifiedClassAsignment(ctx: PatchDefParser.ModifiedClassAsignmentContext?) {
                        modifiedClass = unescapeJavaString((ctx ?: return).StringLiteral().text)
                    }
                }, ctx)
            }
        }
    }

    fun patch(internalName: String, configurator: NullablePatchDef.() -> Unit) {
        val nullablePatchDef = NullablePatchDef()
        nullablePatchDef.configurator()
        val patchDef = nullablePatchDef.toNonNullable() ?: return
        mutablePatchDefs[internalName] = patchDef
    }
}

package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class Parser {
    private val mutablePatchDefs: MutableMap<String, PatchDef> = mutableMapOf()
    val patchDefs: Map<String, PatchDef>
        get() = mutablePatchDefs

    fun parse(string: String) {
        val lexer = PatchDefLexer(CharStreams.fromString(string))
        val tokens = CommonTokenStream(lexer)
        val parser = PatchDefParser(tokens)
        val parserContext = parser.rootParser()
    }
}
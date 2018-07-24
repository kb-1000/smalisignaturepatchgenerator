package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

class Parser {
    private val mutablePatchDefs: MutableMap<String, PatchDef> = mutableMapOf()
    val patchDefs: Map<String, PatchDef>
        get() = mutablePatchDefs

    fun parse(string: String) {
        //val lexer = com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser.PatchDefLexer(ANTLRStringStream(string))
    }
}
package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

class PatchDefListenerImpl(val patchDef: NullablePatchDef) : PatchDefBaseListener() {
    override fun exitHumanNameAssignment(ctx: PatchDefParser.HumanNameAssignmentContext) {
        patchDef.humanName = unqouteUnescapeJavaString(ctx.StringLiteral().text)}

    override fun exitModifiedClassAssignment(ctx: PatchDefParser.ModifiedClassAssignmentContext) {
        patchDef.modifiedClass = unqouteUnescapeJavaString(ctx.StringLiteral().text)}

    override fun exitModifiedMethodAssignment(ctx: PatchDefParser.ModifiedMethodAssignmentContext) {
        patchDef.modifiedMethod = unqouteUnescapeJavaString(ctx.StringLiteral().text)}
}

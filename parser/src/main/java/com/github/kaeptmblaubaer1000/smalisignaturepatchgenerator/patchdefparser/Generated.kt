package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

class PatchDefListenerImpl(val patchDef: NullablePatchDef) : PatchDefBaseListener() {
    override fun exitHumanNameAssignment(ctx: PatchDefParser.HumanNameAssignmentContext) {
        patchDef.humanName = unquoteUnescapeJavaString(ctx.StringLiteral().text)}

    override fun exitModifiedClassAssignment(ctx: PatchDefParser.ModifiedClassAssignmentContext) {
        patchDef.modifiedClass = unquoteUnescapeJavaString(ctx.StringLiteral().text)}

    override fun exitModifiedMethodAssignment(ctx: PatchDefParser.ModifiedMethodAssignmentContext) {
        patchDef.modifiedMethod = unquoteUnescapeJavaString(ctx.StringLiteral().text)}
}

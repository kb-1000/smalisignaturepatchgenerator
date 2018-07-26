package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

data class NullablePatchDef(var humanName: String? = null, var modifiedClass: String? = null) {
    fun toNonNullable(): PatchDef? {
        return PatchDef(humanName = humanName ?: return null, modifiedClass = modifiedClass ?: return null)
    }
}

data class PatchDef(val humanName: String, val modifiedClass: String)

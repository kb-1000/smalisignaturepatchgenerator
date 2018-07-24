package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

data class NullablePatchDef(var humanName: String? = null) {
    fun toNonNullable(): PatchDef? {
        return PatchDef(humanName = humanName ?: return null)
    }
}

data class PatchDef(val humanName: String)

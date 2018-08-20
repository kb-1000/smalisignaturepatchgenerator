package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser

// If you change this class, please run the updateParser:run Gradle task.
data class PatchDef(
        val humanName: String,
        val modifiedClass: String,
        val modifiedMethod: String
)

package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import org.jf.dexlib2.iface.DexFile

data class DexFileWrapper(val dexFile: DexFile) : DexFile by dexFile

@Suppress("NOTHING_TO_INLINE")
inline fun DexFile.wrap() = DexFileWrapper(this)

package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.writer.io.MemoryDataStore
import org.jf.dexlib2.writer.pool.DexPool
import java.io.ByteArrayInputStream


fun rebuildDexFile(dexFile: DexFile): DexFile {
    val dataStore = MemoryDataStore()
    DexPool.writeTo(dataStore, dexFile)
    return DexBackedDexFile.fromInputStream(dexFile.opcodes, ByteArrayInputStream(dataStore.data))
}

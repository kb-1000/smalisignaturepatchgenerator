package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.rewriter.DexRewriter
import org.jf.dexlib2.writer.io.MemoryDataStore
import org.jf.dexlib2.writer.pool.DexPool
import java.io.ByteArrayInputStream

fun identifySignatureVerificationTypes(dexFile: DexFile): Pair<DexFile, SignatureVerificationTypes> {
    val signatureVerificationTypes = SignatureVerificationTypes()
    val rebuiltDexFile = rebuildDexFile(DexRewriter(IdentificationRewriterModule(signatureVerificationTypes)).rewriteDexFile(dexFile))
    return Pair(rebuiltDexFile, signatureVerificationTypes)
}

fun rebuildDexFile(dexFile: DexFile): DexFile {
    val dataStore = MemoryDataStore()
    DexPool.writeTo(dataStore, dexFile)
    return DexBackedDexFile.fromInputStream(dexFile.opcodes, ByteArrayInputStream(dataStore.data))
}

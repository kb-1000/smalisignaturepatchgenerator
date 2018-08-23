package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import org.jf.dexlib2.iface.DexFile
import org.jf.dexlib2.rewriter.DexRewriter

fun identifySignatureVerificationTypes(dexFile: DexFile): Pair<DexFile, SignatureVerificationTypes> {
    val signatureVerificationTypes = SignatureVerificationTypes()
    val rebuiltDexFile = DexRewriter(IdentificationRewriterModule(signatureVerificationTypes)).rewriteDexFile(dexFile)
    return Pair(rebuiltDexFile, signatureVerificationTypes)
}

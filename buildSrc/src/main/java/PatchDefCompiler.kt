import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

object PatchDefCompiler {
    @JvmStatic
    fun generate(file: File) {
        val signatureVerificationTypes = listOf("firstPatch")
        val signatureVerificationTypesClassConstructor = FunSpec.constructorBuilder()
                .addParameters(signatureVerificationTypes.map { ParameterSpec.builder(it, Boolean::class).defaultValue("false").build() })

        val signatureVerificationTypesClass = TypeSpec.classBuilder(ClassName("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated", "SignatureVerificationTypes"))
                .primaryConstructor(signatureVerificationTypesClassConstructor
                        .build())
                .addModifiers(KModifier.DATA)
                .addProperties(signatureVerificationTypes.map { PropertySpec.varBuilder(it, Boolean::class).initializer(it).build() })
                .build()

        val identificationClassDefRewriterClass = TypeSpec.classBuilder(ClassName("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated", "IdentificationClassDefRewriter"))
                .superclass(ClassName("org.jf.dexlib2.rewriter", "ClassDefRewriter"))
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter("rewriters", ClassName("org.jf.dexlib2.rewriter", "Rewriters"))
                        .build())
                .addSuperclassConstructorParameter("rewriters")
                .build()


        val fileSpec = FileSpec.builder("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated", "CompiledPatchDef")
                .addType(identificationClassDefRewriterClass)
                .addType(signatureVerificationTypesClass)
                .build()
        fileSpec.writeTo(file)
    }
}

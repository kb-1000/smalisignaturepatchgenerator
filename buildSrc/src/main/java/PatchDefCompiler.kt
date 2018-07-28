import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser.Parser
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
    fun generate(input: File, output: File) {
        val patchDefs = parseFiles(input).patchDefs
        val signatureVerificationTypes = patchDefs.keys
        if (signatureVerificationTypes.isEmpty()) {
            throw EmptyPatchDefListNotAllowedException("You haven't added any PatchDefs, or they are all invalid. If you *have* added PatchDefs, look above for parser errors")
        }
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
        fileSpec.writeTo(output)
    }

    class EmptyPatchDefListNotAllowedException : Throwable {
        constructor(message: String?, cause: Throwable?) : super(message, cause)
        constructor(message: String?) : super(message)
        constructor(cause: Throwable?) : super(cause)
        constructor() : super()
    }

    fun parseFiles(dir: File): Parser {
        val parser = Parser()
        for (file: File in dir.walk().filter { it.isFile }) {
            parser.parse(file.readText(Charsets.UTF_8))
        }
        return parser
    }
}

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

object GenerateIdentificationClassDefRewriter {
    @JvmStatic
    fun generate(file: File) {
        val identificationClassDefRewriterClass = TypeSpec.classBuilder(ClassName("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated", "IdentificationClassDefRewriter"))
                .superclass(ClassName("org.jf.dexlib2.rewriter", "ClassDefRewriter"))
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter("rewriters", ClassName("org.jf.dexlib2.rewriter", "Rewriters"))
                        .build())
                .addSuperclassConstructorParameter("rewriters")
                .build()
        val fileSpec = FileSpec.builder("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated", "IdentificationClassDefRewriter")
                .addType(identificationClassDefRewriterClass)
                .build()
        fileSpec.writeTo(file)
    }
}

package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.updateParser

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser.NullablePatchDef
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser.PatchDef
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser.PatchDefBaseListener
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import java.nio.file.Paths
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun main(vararg args: String) {
    val kClass = PatchDef::class

    val props = kClass.memberProperties.map(KProperty1<PatchDef, *>::name)

    val patchDefListenerBuilder = com.squareup.kotlinpoet.TypeSpec
            .classBuilder(ClassName("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser", "PatchDefListenerImpl"))
            .superclass(PatchDefBaseListener::class)
            .addProperty(PropertySpec
                    .builder("patchDef", NullablePatchDef::class)
                    .initializer("patchDef")
                    .build())
            .primaryConstructor(FunSpec
                    .constructorBuilder()
                    .addParameter("patchDef", NullablePatchDef::class)
                    .build())

    for (prop in props) {
        patchDefListenerBuilder.addFunction(FunSpec
                .builder("exit${prop.capitalize()}Assignment")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("ctx", ClassName("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser", "PatchDefParser", "${prop.capitalize()}AssignmentContext"))
                .addCode("patchDef.%1N = unquoteUnescapeJavaString(ctx.StringLiteral().text)", prop)
                .build())
    }

    val fileSpec = FileSpec
            .builder("com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser", "Generated")
            .addType(patchDefListenerBuilder.build())
            .build()

    fileSpec.writeTo(Paths.get("..", "parser", "src", "main", "java"))

}
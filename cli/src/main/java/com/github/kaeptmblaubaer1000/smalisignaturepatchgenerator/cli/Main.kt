package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.cli

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.ChangeMainApk
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.ChangeSignatureApk
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.Generate
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.PatchGenerator
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.Stop
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.mainlib.IMain
import java.io.File

class Main {
    companion object : IMain {
        @JvmStatic
        override fun main(args: Array<String>) {
            if (args.size != 3) {
                printUsage()
            } else {
                val patchGenerator = PatchGenerator()
                patchGenerator.start()
                patchGenerator.inputQueue.put(ChangeMainApk(File(args[0])))
                patchGenerator.inputQueue.put(ChangeSignatureApk(File(args[1])))
                patchGenerator.inputQueue.put(Generate(File(args[2])))
                patchGenerator.inputQueue.put(Stop)
                patchGenerator.join()
                println(patchGenerator.identifiedSignatureVerificationTypes)
            }
        }

        override val description: String
            get() = "The basic command line interface"

        private fun printUsage() {
            println("Usage: <apk-file> <patch-zip-file>")
        }
    }
}
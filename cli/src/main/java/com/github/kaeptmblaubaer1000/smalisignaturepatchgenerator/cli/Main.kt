package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.cli

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.PatchGenerator
import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.mainlib.IMain
import java.io.File

class Main {
    companion object : IMain {
        @JvmStatic
        override fun main(args: Array<String>) {
            if (args.size != 2) {
                //TODO: print usage
            } else {
                val patchGenerator = PatchGenerator()
                val dexFile = patchGenerator.loadApkFile(File(args[0]))
            }
        }

        override val description: String
            get() = "The basic command line interface"
    }
}
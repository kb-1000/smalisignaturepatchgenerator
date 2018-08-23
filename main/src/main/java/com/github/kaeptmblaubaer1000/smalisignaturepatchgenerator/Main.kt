package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.mainlib.IMain

class Main {
    companion object : IMain {
        val mains: Map<String, IMain> = mapOf(
                "main" to Main
        )

        @JvmStatic
        override fun main(vararg args: String) {
            if (args.isEmpty()) {
                println("Please give a command name as the first parameter to this program.")
                printCommandNames()
            }
        }

        private fun printCommandNames() {
            println("Available commands:")
            for((name, command) in mains) {
                println("\t$name")
            }
        }
    }
}

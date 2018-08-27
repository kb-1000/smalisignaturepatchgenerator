package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.mainlib.IMain

class Main {
    companion object : IMain {
        val mains: Map<String, IMain> = mapOf(
                "main" to Main,
                "cli" to com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.cli.Main
        )

        @JvmStatic
        override fun main(args: Array<String>) {
            when {
                args.isEmpty() -> {
                    println("Please give a command name as the first parameter to this program.")
                    printCommandNames()
                }
                args[0] !in mains -> {
                    println("Command \"${args[0]}\" not found.")
                    printCommandNames()
                }
                else -> mains[args[0]]?.main(args.drop(1).toTypedArray())
            }
        }

        private fun printCommandNames() {
            println("Available commands:")
            for ((name, command) in mains) {
                println("\t$name: ${command.description}")
            }
        }

        override val description: String
            get() = "This program itself"
    }
}

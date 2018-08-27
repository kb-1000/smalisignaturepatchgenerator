package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.cli

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.mainlib.IMain

class Main {
    companion object : IMain {
        @JvmStatic
        override fun main(args: Array<String>) {
        }

        override val description: String
            get() = "The basic command line interface"
    }
}
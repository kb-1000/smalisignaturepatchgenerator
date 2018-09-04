package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import java.io.File

interface Message
interface InputMessage : Message
interface OutputMessage : Message

object Stop : InputMessage
data class ChangeMainApk(val file: File) : InputMessage
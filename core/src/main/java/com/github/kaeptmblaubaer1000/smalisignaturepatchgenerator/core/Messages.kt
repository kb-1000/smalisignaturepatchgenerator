package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

import com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core.generated.SignatureVerificationTypes
import java.io.File

interface Message
interface InputMessage : Message
interface OutputMessage : Message

object Stop : InputMessage
data class ChangeMainApk(val file: File) : InputMessage
data class ChangeSignatureApk(val file: File) : InputMessage
data class Generate(val file: File, val signatureVerificationTypes: SignatureVerificationTypes) : InputMessage

object Stopped : OutputMessage
data class Error(val throwable: Throwable) : OutputMessage

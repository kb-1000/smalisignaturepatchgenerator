package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.core

interface Message
interface InputMessage : Message
interface OutputMessage : Message

object Stop : InputMessage
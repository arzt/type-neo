package de.berlin.arzt.neotrainer

import java.nio.CharBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.StandardOpenOption.READ
import java.nio.file.{Files, Path, Paths}

object RandomWordCharProviderFile {

  def fromFile(filename: String): CharProvider = apply(Paths.get(filename))

  def apply(resource: String): CharProvider = apply(Paths.get(getClass.getResource(resource).toURI))

  def apply(path: Path): CharProvider = {
    val fileChannel = Files.newByteChannel(path, READ).asInstanceOf[FileChannel]
    apply(fileChannel)
  }

  def apply(fileChannel: FileChannel): CharProvider = {
    val byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size)
    val decoder = UTF_8.newDecoder
    val charBuffer = decoder.decode(byteBuffer)
    new RandomWordCharProviderFile(charBuffer)
  }
}

class RandomWordCharProviderFile extends CharProvider {
  private var buf: CharBuffer = null
  private var length: Int = 0

  def this(charBuffer: CharBuffer) {
    this()
    this.buf = charBuffer
    this.length = charBuffer.length
    nextRandomWord
  }

  private def nextRandomWord {
    val pos: Int = (Math.random * length).toInt
    buf.position(pos)
    var c: Char = buf.get
    while (c != '\n') {
      c = buf.get
    }
  }

  def getNextChar: Char = {
    val c: Char = buf.get
    if (!Character.isAlphabetic(c)) {
      nextRandomWord
      return ' '
    }
    return c
  }

  def reward(c: Char) {
  }

  def penalize(c: Char) {
  }
}
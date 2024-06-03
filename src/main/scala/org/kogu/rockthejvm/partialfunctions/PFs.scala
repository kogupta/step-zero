package org.kogu.rockthejvm.partialfunctions

object PFs {
  private val pf: PartialFunction[Int, Int] = {
    case 1 => 21
    case 2 => 42
    case 10 => 50
  }

  private val pf2: PartialFunction[Int, Int] = {
    case 11 => 55
  }

  def main(args: Array[String]): Unit = {
    println(pf.isDefinedAt(20))

    val pfLifted: Int => Option[Int] = pf.lift

    println(pfLifted(20))

    val pfChain: PartialFunction[Int, Int] = pf.orElse(pf2)
  }
}

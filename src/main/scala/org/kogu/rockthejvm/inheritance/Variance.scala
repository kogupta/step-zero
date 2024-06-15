package org.kogu.rockthejvm.inheritance

object Variance {
  def main(args: Array[String]): Unit = {
    println("COVARIANT, INVARIANT, CONTRAVARIANT ... oh my!")
  }

  // lists => covariant
  //   - Dog extends Animal
  //   - List[Dog] extends List[Animal]
  // arrays => nonvariant

}

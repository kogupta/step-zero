package org.kogu.rockthejvm.init.lazyEval

import scala.util.Random

//noinspection ScalaWeakerAccess
object Main {
  // call by need = call by name + lazy values
  def byName(n: => Int): Int = n + n + n + 1

  def byNeed(n: => Int): Int = {
    val lazyN = n
    lazyN + lazyN + lazyN + 1
  }

  def expensiveVal(): Int = {
    println("waiting ....")
    Thread.sleep(2_000)
    2
  }

  def main(args: Array[String]): Unit = {
    println(byName(expensiveVal()))
    println("----------------")

    println(byNeed(expensiveVal()))
    println("----------------")

    // filter
    println(xs.filter(isMoreThan5).filter(isLessThan8))
    println("----------------")

    // with filter
    println(xs.withFilter(isMoreThan5).withFilter(isLessThan8).map(identity))
  }


  val xs: Seq[Int] = Random.shuffle(1 to 10)
  val isMoreThan5: Int => Boolean = n => {
    println(s"$n is more than 5?")
    n > 5
  }
  val isLessThan8: Int => Boolean = n => {
    println(s"$n is less than 8?")
    n < 8
  }


}

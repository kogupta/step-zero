package org.kogu.rockthejvm.init.currying

//noinspection TypeAnnotation,ScalaWeakerAccess
object Main {
  def concat(a: String, b: String, c: String): String = a + b + c

  val insertName: String => String = concat("Hello, my name is ", _:String, ", how are you?")

  val add: (Int, Int) => Int = (x: Int, y: Int) => x + y
  def addM(x: Int, y: Int): Int = x + y
  def addC(x: Int)(y: Int): Int = x + y

  // methods - functions - by-name - 0-lambdas oh my!
  def byName(n: => Int) = n + 1
  def byLambda(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  def main(args: Array[String]): Unit = {
    val add7: Int => Int = add(7, _)
    val add7_v2: Int => Int = x => add(7, x)
    val add7_v3: Int => Int = add.curried(7)

    val addM7: Int => Int = addM(7, _)
    val addM7_v2: Int => Int = x => addM(7, x)

    val addC7: Int => Int = addC(7)

    // ------
    byName(23)
    byName(method)
    byName(parenMethod())
    //  byName(parenMethod)
    // byName(() => 42)
    byName((() => 42)())  // invoke the lambda

    // byLambda(23)
    byLambda(() => 23)
    // byLambda(method)
    byLambda(parenMethod)
    byLambda(() => parenMethod())
  }
}

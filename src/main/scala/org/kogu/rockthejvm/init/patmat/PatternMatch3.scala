package org.kogu.rockthejvm.init.patmat

object PatternMatch3 {
  def main(args: Array[String]): Unit = {
    val dan = new Person("Daniel", 102)

    // type of `dan` is parameter to `unapply` method
    dan match
      // case <name of Object with unapply method>
      // s => type parameter of `Matcher`/return type of `get`
      case PersonMatcher(s) => println(s"Hello! $s")
      case PersonMatcher2(n) => println(s"Hey! you are $n years old")
  }

  final class Person(val name: String, val age: Int)

  sealed trait Matcher[T] {
    def isEmpty: Boolean

    def get: T
  }

  object PersonMatcher {
    def unapply(p: Person): Matcher[String] = new Matcher[String] {
      override def isEmpty: Boolean = false

      override def get: String = p.name
    }
  }

  object PersonMatcher2 {
    def unapply(p: Person): Matcher[Int] = new Matcher[Int] {
      override def isEmpty: Boolean = false

      override def get: Int = p.age
    }
  }
}

package org.kogu.rockthejvm.patmat

object PatternMatching {
  def main(args: Array[String]): Unit = {

  }

  final class Person(val name: String, val age: Int)
  object Person {
    def apply(name: String, age: Int): Person = new Person(name, age)

    // 1. parameter match { case $Object(option type) => ... }
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21)
        None
      else Some((person.name, person.age))

    // 2. parameter match { case $Object(option type) => ... }
    def unapply(n: Int): Option[String] =
      if (n < 21)
        Some("minor")
      else
        Some("legally allowed to drink")
  }

  private val user = Person("daniel", 102)

  // 1. parameter: Person, case: Person(String, Int)
  user match
    case Person(name, n) => println(s"Hi $name, have a drink!")

  // 2. parameter: Int, case: Person(String)
  user.age match
    case Person(status) => println(s"Legal drinking status: $status")

}

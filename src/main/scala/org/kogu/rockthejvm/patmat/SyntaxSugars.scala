package org.kogu.rockthejvm.patmat

object SyntaxSugars {
  // 1. methods with single argument
  private def singleArgMethod(n: Int): Int = n + 1

  // 2. single abstract method
  trait Action {
    def act(n: Int): Int
  }

  // 4. multi word identifiers
  final class Talker(name: String) {
    infix def `and then said`(gossip: String): Unit = println(s"$name said: $gossip")
  }

  def main(args: Array[String]): Unit = {
    // example 1
    singleArgMethod {
      // create an expression that returns an Int
      Thread.sleep(10_000)
      1
    }

    // example 2
    val action: Action = n => n + 1
    val aThread = new Thread(() => println("aa"))

    // 3. BULLSHIT
    // right associativity of methods ending with colon/:
    val bullshittery: List[Int] = 0 :: 1 :: 2 :: List(3, 4)
    // :: is a method on List - NOT on integer

    // example 4
    val kogu = new Talker("kogu")
    kogu `and then said` "lets have coffee!"
    // widely used in http libraries
    object `Content-Type` {
      val `application/json`: String = "application/JSON"
    }
  }
}

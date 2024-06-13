package org.kogu.rockthejvm.inheritance

//noinspection ScalaWeakerAccess
object Main {
  // trait stacking => type linearization
  trait Animal {
    def name: String
  }

  trait Lion extends Animal {override def name: String = "Lion"}
  trait Tiger extends Animal {override def name: String = "Tiger"}
  class Liger extends Lion with Tiger

  trait Cold {
    def print(): Unit = println("cold")
  }

  trait Green extends Cold {
    override def print(): Unit = {
      println("green")
      super.print()
    }
  }

  trait Blue extends Cold {
    override def print(): Unit = {
      println("blue")
      super.print()
    }
  }

  class Red {
    def print(): Unit = println("red")
  }

  class White extends Red with Green with Blue {
    override def print(): Unit = {
      println("white")
      super.print()
    }
  }

  def main(args: Array[String]): Unit = {
    // trait stacking => type linearization
    assert((new Liger).name == "Tiger")
    assert((new Lion with Tiger).name == "Tiger")
    assert((new Tiger with Lion).name == "Lion")

    (new White).print()
    //  white
    //  blue
    //  green
    //  cold
  }
}

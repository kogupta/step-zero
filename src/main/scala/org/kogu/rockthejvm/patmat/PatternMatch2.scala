package org.kogu.rockthejvm.patmat

object PatternMatch2 {
  def main(args: Array[String]): Unit = {
    val xs: List2[Int] = Cons(1, Cons(2, Cons(3, Empty)))

    xs match
      case List2(1, _*) => println("list starting with 1")
      case _ => println("list does not start with 1")


  }
  sealed trait List2[+A] {
    def head: A = throw new NoSuchElementException

    def tail: List2[A] = throw new NoSuchElementException
  }

  object List2 {
    def apply[A](xs: Seq[A]): List2[A] =
      if (xs.isEmpty) {
        Empty
      } else {
        Cons(xs.head, apply(xs.tail))
      }

    def unapplySeq[A](xs: List2[A]): Option[Seq[A]] =
      if (xs == Empty) Some(Seq.empty)
      else unapplySeq(xs.tail).map(rest => xs.head +: rest)
  }

  //noinspection ScalaWeakerAccess
  case object Empty extends List2[Nothing]

  //noinspection ScalaWeakerAccess
  final case class Cons[A](override val head: A, override val tail: List2[A]) extends List2[A]
}

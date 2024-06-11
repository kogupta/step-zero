package org.kogu.rockthejvm

import scala.annotation.tailrec


object LazyList {
  def main(args: Array[String]): Unit = {
    val naturals = LzList.generate(1)(_ + 1)
    println(naturals.take(10).toList)

    println(naturals.flatMap(x => LzList(x, x + 1)).takeAsList(100_000))

//    println(naturals.filter(_ < 10).takeAsList(100))

    val fibsNaive: LzList[(Int, Int)] = LzList.generate((1, 2)) { case (a, b) => (b, a + b)}
    
    val fibs: LzList[Int] = {
      def helper(a: Int, b: Int): LzList[Int] =
        new Cons(a, helper(b, a + b))

      helper(1, 2)
    }

    println(fibs.takeAsList(45))
  }
}

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
sealed trait LzList[+A] {
  def isEmpty: Boolean

  def head: A

  def tail: LzList[A]

  // utilities
  def #:: [B >: A](element: B): LzList[B] = new Cons(element, this)

  infix def ++ [B >: A](another: => LzList[B]): LzList[B] =
    if (isEmpty) another
    else if (another.isEmpty) this
    else new Cons(head, tail ++ another)

  // classics
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): LzList[B]
  def flatMap[B](f: A => LzList[B]): LzList[B]
  def filter(predicate: A => Boolean): LzList[A]
  def withFilter(predicate: A => Boolean): LzList[A] = filter(predicate)

  def take(n: Int): LzList[A] // takes the first n elements from this lazy list
  def takeAsList(n: Int): List[A] = take(n).toList
  def toList: List[A] = {
    @tailrec
    def loop(acc: List[A], rest: LzList[A]): List[A] = {
      if(rest.isEmpty) acc.reverse
      else loop(rest.head::acc, rest.tail)
    }
    loop(Nil, this)
  }
}

object LzList {
  def empty[A]: LzList[A] = Empty

  def generate[A](start: A)(generator: A => A): LzList[A] =
    new Cons(start, generate(generator(start))(generator))

  //noinspection ScalaWeakerAccess
  def from[A](xs: Seq[A]): LzList[A] =
    if (xs.isEmpty) empty
    else new Cons(xs.head, from(xs.tail))

  def apply[A](values: A*): LzList[A] = from(values.toList)
}

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
case object Empty extends LzList[Nothing] {
  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): LzList[B] = this.asInstanceOf

  override def flatMap[B](f: Nothing => LzList[B]): LzList[B] = this.asInstanceOf

  override def filter(predicate: Nothing => Boolean): LzList[Nothing] = this

  override def isEmpty: Boolean = true

  override def head: Nothing = throw new NoSuchElementException

  override def tail: LzList[Nothing] = throw new NoSuchElementException

  override def take(n: Int): LzList[Nothing] =
    if (n == 0) this
    else throw new RuntimeException("`take` on an empty list")

  override def toList: List[Nothing] = List.empty
}

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
final class Cons[A](hd: => A, tl: => LzList[A]) extends LzList[A] {
  override def isEmpty: Boolean = false

  override infix def ++ [B >: A](another: => LzList[B]): LzList[B] = new Cons(hd, tl ++ another)

  override def foreach(f: A => Unit): Unit = {
    @tailrec
    def loop(xs: LzList[A]): Unit = {
      if (xs.isEmpty) ()
      else {
        f(xs.head)
        loop(xs.tail)
      }
    }

    loop(this)
  }

  override def map[B](f: A => B): LzList[B] = new Cons(f(head), tail.map(f))

  override def flatMap[B](f: A => LzList[B]): LzList[B] = f(head) ++ tail.flatMap(f)

  override def filter(predicate: A => Boolean): LzList[A] =
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)

  override def take(n: Int): LzList[A] =
    if (n <= 0) LzList.empty
    else if (n == 1) new Cons(head, LzList.empty)
    else new Cons(head, tail.take(n - 1))

  override lazy val head: A = hd

  override lazy val tail: LzList[A] = tl
}
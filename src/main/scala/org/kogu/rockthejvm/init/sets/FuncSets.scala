package org.kogu.rockthejvm.init.sets

import scala.annotation.tailrec

object FuncSets {
  def main(args: Array[String]): Unit = {
    val set: FuncSet[Int] = FuncSet(1, 2, 3, 4, 5)
    // contains
    assert(set.contains(1))
    assert(set.contains(2))
    assert(set.contains(3))
    assert(set.contains(4))
    assert(set.contains(5))
    assert(!set.contains(10))

    // map
    val doubled = set.map(_ * 2)
    assert(!doubled.contains(1))
    assert(doubled.contains(10))

    // flatMap
    val xs = set.flatMap(n => FuncSet(n))
    assert(xs.contains(1))
    assert(xs.contains(2))
    assert(xs.contains(3))
    assert(xs.contains(4))
    assert(xs.contains(5))
    assert(!xs.contains(10))

    // filter
    val even = set.filter(_ % 2 == 0)
    assert(!even.contains(1))
    assert(even.contains(2))
    assert(!even.contains(3))
    assert(even.contains(4))
    assert(!even.contains(5))

    even.display()
  }
}

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
sealed trait FuncSet[A] extends (A => Boolean) {
  def contains(a: A): Boolean
  def apply(a: A): Boolean = contains(a)

  infix def +(a: A): FuncSet[A]
  infix def -(a: A): FuncSet[A]
  infix def --(anotherSet: FuncSet[A]): FuncSet[A]
  infix def union(anotherSet: FuncSet[A]): FuncSet[A]
  infix def intersect(anotherSet: FuncSet[A]): FuncSet[A]
  def unary_! : FuncSet[A]

  def map[B](f: A => B): FuncSet[B]
  def flatMap[B](f: A => FuncSet[B]): FuncSet[B]
  def filter(predicate: A => Boolean): FuncSet[A]
  def foreach(f: A => Unit): Unit

  def display(): Unit = foreach(println)
}

object FuncSet {
  def apply[A](xs: A*): FuncSet[A] = {
    @tailrec
    def loop(ys: Seq[A], acc: FuncSet[A]): FuncSet[A] =
      if (ys.isEmpty) acc
      else loop(ys.tail, acc + ys.head)

    loop(xs, EmptySet())
  }
}

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
final case class EmptySet[A]() extends FuncSet[A] {
  override def contains(a: A): Boolean = false
  override infix def +(a: A): FuncSet[A] = Cons(a, this)
  override infix def union(anotherSet: FuncSet[A]): FuncSet[A] = anotherSet
  override def map[B](f: A => B): FuncSet[B] = EmptySet()
  override def flatMap[B](f: A => FuncSet[B]): FuncSet[B] = EmptySet()
  override def filter(predicate: A => Boolean): FuncSet[A] = this
  override def foreach(f: A => Unit): Unit = ()

  override infix def -(a: A): FuncSet[A] = this

  override infix def --(anotherSet: FuncSet[A]): FuncSet[A] = this

  override infix def intersect(anotherSet: FuncSet[A]): FuncSet[A] = this

  override def unary_! : FuncSet[A] = UniversalSet()
}

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
class PBSet[A](property: A => Boolean) extends FuncSet[A] {
  override def contains(a: A): Boolean = property(a)

  override infix def +(a: A): FuncSet[A] = new PBSet(x => x == a || property(x))

  override infix def -(a: A): FuncSet[A] = filter(x => x != a)

  override infix def --(anotherSet: FuncSet[A]): FuncSet[A] = filter(!anotherSet)

  override infix def union(anotherSet: FuncSet[A]): FuncSet[A] = new PBSet(x => property(x) || anotherSet(x))

  override infix def intersect(anotherSet: FuncSet[A]): FuncSet[A] =
    new PBSet(x => property(x) && anotherSet(x))

  override def unary_! : FuncSet[A] = new PBSet(x => !contains(x))

  override def map[B](f: A => B): FuncSet[B] = ???

  override def flatMap[B](f: A => FuncSet[B]): FuncSet[B] = ???

  override def filter(predicate: A => Boolean): FuncSet[A] = new PBSet(x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = ???
}




//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
final case class UniversalSet[A]() extends PBSet[A](_ => true)

//noinspection NoTargetNameAnnotationForOperatorLikeDefinition
final case class Cons[A](head: A, tail: FuncSet[A]) extends FuncSet[A] {
  override def contains(a: A): Boolean = head == a || tail.contains(a)

  override infix def +(a: A): FuncSet[A] =
    if (contains(a)) this
    else Cons(a, this)

  override infix def union(anotherSet: FuncSet[A]): FuncSet[A] = Cons(head, tail union anotherSet)

  override def map[B](f: A => B): FuncSet[B] = tail.map(f) + f(head)

  override def flatMap[B](f: A => FuncSet[B]): FuncSet[B] = f(head) union tail.flatMap(f)

  override def filter(predicate: A => Boolean): FuncSet[A] = {
    val rest = tail.filter(predicate)
    if (predicate(head))
      rest + head
    else
      rest
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  override infix def -(a: A): FuncSet[A] = filter(e => e != a)

  override infix def --(anotherSet: FuncSet[A]): FuncSet[A] = filter(!anotherSet) // using unary_! operator

  override infix def intersect(anotherSet: FuncSet[A]): FuncSet[A] = filter(anotherSet)

  override def unary_! : FuncSet[A] = ???
}
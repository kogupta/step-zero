package org.kogu.rockthejvm.hkt

import scala.util.Try

//noinspection DuplicatedCode,ScalaWeakerAccess
object CrossProduct {
  // generalize cross product
  def combineList[A, B](la: List[A], lb: List[B]): List[(A, B)] =
    for {
      a <- la
      b <- lb
    } yield (a, b)
  def combineOption[A, B](la: Option[A], lb: Option[B]): Option[(A, B)] =
    for {
      a <- la
      b <- lb
    } yield (a, b)
  def combineTry[A, B](la: Try[A], lb: Try[B]): Try[(A, B)] =
    for {
      a <- la
      b <- lb
    } yield (a, b)


  def combineTry2[A, B](la: Try[A], lb: Try[B]): Try[(A, B)] =
    la.flatMap(a =>
      lb.map(b => (a, b))
    )

  // must have map and flatMap

  // step 1: typeclass definition
  sealed trait HKT[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  }

  // step 2: typeclass instances
  given listHKT: HKT[List] with
    override def map[A, B](list: List[A])(f: A => B): List[B] = list.map(f)
    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)

  given optionHKT: HKT[Option] with
    override def map[A, B](maybe: Option[A])(f: A => B): Option[B] = maybe.map(f)
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)

  given tryHKT: HKT[Try] with
    override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
    override def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f)

  // extension method
  extension [F[_], A](container: F[A])(using hkt: HKT[F])
    def map[B](f: A => B): F[B] = hkt.map(container)(f)
    def flatMap[B](f: A => F[B]): F[B] = hkt.flatMap(container)(f)

  // "user facing" api
  def verboseCombiner[F[_], A, B](fa: F[A], fb: F[B])(using hkt: HKT[F]): F[(A, B)] =
    hkt.flatMap(fa) { a =>
      hkt.map(fb)(b => (a, b))
    }

  def combiner[F[_] : HKT, A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    for {
      a <- fa
      b <- fb
    } yield (a, b)

  def main(args: Array[String]): Unit = {
    assert(
      combiner(List(1,2,3), List("a", "b")) ==
        List((1,"a"), (1,"b"), (2,"a"), (2,"b"), (3,"a"), (3,"b"))
    )

    assert(
      combiner(Option(1), None).isEmpty
    )

    assert(
      combiner(Try(1), Try("a")) ==
        Try((1, "a"))
    )
  }
}


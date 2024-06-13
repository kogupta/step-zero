package org.kogu.rockthejvm.hkt

import scala.util.Try

//noinspection ScalaWeakerAccess
object Main {
  class HKT[F[_]]
  class HKT2[F[_, _]]
  class HKT3[F[_], G[_], A]

  def main(args: Array[String]): Unit = {
    // samples
    val hkt = HKT[List]

    val hkt2 = HKT2[Map]
    val anotherHkt2 = HKT2[Either]

    val hkt3 = HKT3[Try, Option, String]
  }

}

//noinspection SpellCheckingInspection
private object ProblemStatement {
  def do10xList(xs: List[Int]): List[Int] = xs.map(_ * 10)
  def do10xOption(maybeInt: Option[Int]): Option[Int] = maybeInt.map(_ * 10)
  def do10xTry(xs: Try[Int]): Try[Int] = xs.map(_ * 10)

  // What: abstract over map
  //      => create typeclass for functor hkt

  import Functors.*

  // step 3: "user facing" api
  def do10x[F[_]](container: F[Int])(using functor: Functor[F]): F[Int] =
    functor.map(container)(_ * 10)

  def do10xAgain[F[_]](container: F[Int])(using functor: Functor[F]): F[Int] =
    container.map(_ * 10)

  // `functor` instance is not used, define it via context bound
  //                  VVVVVVVV
  def do10xAgain2[F[_]: Functor](container: F[Int]): F[Int] =
    container.map(_ * 10)
}

//noinspection DuplicatedCode
private object Functors {
  // step 1: typeclass definition
  sealed trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  // step 2: typeclass instances
  given listFunctor: Functor[List] with
    override def map[A, B](list: List[A])(f: A => B): List[B] = list.map(f)

  given optionFunctor: Functor[Option] with
    override def map[A, B](maybe: Option[A])(f: A => B): Option[B] = maybe.map(f)

  // step 4: extension methods
  extension [F[_], A](container: F[A])(using functor: Functor[F])
    def map[B](f: A => B): F[B] = functor.map(container)(f)

}
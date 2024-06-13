package org.kogu.rockthejvm.init.monads

//noinspection ScalaWeakerAccess
object Main {

  def monadLawsList(): Unit = {
    val f: Int => List[Int] = n => List(n, n + 1)
    val g: Int => List[Int] = n => List(n, n * 2)
    val pure: Int => List[Int] = n => List(n)

    // prop 1: left identity
    assert(pure(42).flatMap(f) == f(42))

    // prop 2: right identity
    val xs: List[Int] = ???
    assert(xs.flatMap(pure) == xs)

    // prop 3: associativity
    assert(xs.flatMap(f).flatMap(g) == xs.flatMap(x => f(x).flatMap(g)))
  }

  def main(args: Array[String]): Unit = {
    monadLaws()
  }

  final case class PossiblyMonad[A](unsafeRun: () => A) {
    def map[B](f: A => B): PossiblyMonad[B] = PossiblyMonad(() => f(unsafeRun()))

    def flatMap[B](f: A => PossiblyMonad[B]): PossiblyMonad[B] =
      PossiblyMonad(() => f(unsafeRun()).unsafeRun())
  }

  object PossiblyMonad {
    def pure[A](value: => A): PossiblyMonad[A] = PossiblyMonad(() => value)
  }

  def monadLaws(): Unit = {
    val f: Int => PossiblyMonad[Int] = n => PossiblyMonad(() => n + 1)
    val g: Int => PossiblyMonad[Int] = n => PossiblyMonad(() => n * 2)
    val x = PossiblyMonad(() => 42)

    import PossiblyMonad.pure
    // monad laws
    // prop 1 - left identity
//    assert(pure(42).flatMap(f) == f(42), "left identity failed")
    assert(pure(42).flatMap(f).unsafeRun() == f(42).unsafeRun(), "left identity failed")

    // prop 2 - right identity
//    assert(x.flatMap(pure) == x, "right identity failed")
    assert(x.flatMap(pure).unsafeRun() == x.unsafeRun(), "right identity failed")

    // prop 3 - associativity
//    assert(x.flatMap(f).flatMap(g) == x.flatMap(p => f(p).flatMap(g)), "associativity failed")
    assert(x.flatMap(f).flatMap(g).unsafeRun() == 
      x.flatMap(p => f(p).flatMap(g)).unsafeRun(), 
      "associativity failed")
  }
}

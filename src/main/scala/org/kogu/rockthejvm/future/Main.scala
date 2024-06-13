package org.kogu.rockthejvm.future

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

//noinspection ScalaWeakerAccess
object Main {
  def immediate[A](a: A): Future[A] = Future.successful(a)

  def inSequence[A, B](first: Future[A], second: Future[B])(implicit ec: ExecutionContext): Future[B] =
    first.flatMap(_ => second)

  def first[A](f1: Future[A], f2: Future[A])(implicit ec: ExecutionContext): Future[A] =
    Future.firstCompletedOf(Seq(f1, f2))

  def last[A](f1: Future[A], f2: Future[A])(implicit ec: ExecutionContext): Future[A] =
    Future.reduceLeft(Seq(f1, f2))((_, current) => current)

  def retryUntil[A](action: () => Future[A], predicate: A => Boolean)(implicit ec: ExecutionContext): Future[A] =
    action().filter(predicate).recoverWith(_ => retryUntil(action, predicate))

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    assert(immediate(42).isCompleted)

    val lastFt = last(Future.successful(1), Future.successful(2))
    assert(Await.result(lastFt, 1.second) == 2)

    val anInt = AtomicInteger(0)
    val action = () => Future {
      Thread.sleep(100)
      anInt.incrementAndGet()
      println(s"step: ${anInt.get()}")
      anInt.get()
    }
    val value = Await.result(retryUntil(action, n => n > 5), 10.second)
    assert(value == 6)
  }
}

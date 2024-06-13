package org.kogu.rockthejvm.init.funcCollections

object Main {
  def main(args: Array[String]): Unit = {
    // sets - total function: T => Boolean
    // sequences on other hand: partial function - index => value
    //    - Int => T
    //    - for an invalid index, function is not total
    val aSeq = Seq(0, 1, 2, 3, 4)
    assert(aSeq(2) == 2)  // => "function" of Int => T
    // assert(aSeq(200) == 2) => partial function

    // similarly, for Map[K,V], it is a partial function of K => V
  }
}

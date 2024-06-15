### What
Working through [Advanced Scala3](https://rockthejvm.com/courses/enrolled/1514231) course.


### Variance
  - [Martin Odersky Variance](https://www.youtube.com/watch?v=QDzPNv4UIkY) lecture from FuncProgScala Coursera course.
    ```scala
    // arguments CONTRA-variant
    // results   CO-variant
    trait Fn[-T, +U] {
      def apply(x: T): U
    }
    ```
    - roughly
      | type parameter | position |
      |-- | -- |
      | `covariant` | method return type |
      | `contravariant` | method parameters |
      | `invariant` | anywhere |
    - immutable `List`s are covariant
      ```scala
      trait List[+T]
      
      object Nil extends List[Nothing]
      
      val xs: List[String] = Nil // compile for covariant List only
      ```
  - [Scala variance](https://blog.rockthejvm.com/scala-variance-positions/) article and [video](https://www.youtube.com/watch?v=aUmj7jnXet4)
  - [Contravariance hard!](https://blog.rockthejvm.com/contravariance/) article and [video](https://www.youtube.com/watch?v=b1ftkK1zhxI)

### HKT
  - hit a wall with red scala book using Java? [continue with Scala](https://typelevel.org/blog/2016/08/21/hkts-moving-forward.html)
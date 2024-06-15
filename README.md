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
  - Similar explanation for Java: https://stackoverflow.com/a/8482286
    ```java
    class A {
      public S f(U u) { ... }
    }

    class B extends A {
      @Override
      public T f(V v) { ... }
    }

    B b = new B();
    T t = b.f(v);
    A a = ...; // Might have type B
    S s = a.f(u); // and then do V v = u;
    ```
    - We can see:
      - T must be subtype S (**covariant**, as B is subtype of A).
      - V must be supertype of U (**contravariant**, as contra inheritance direction).
      
      This is also referred to as `covariance for producers`, `contravariance for consumers`

  - [Scala variance](https://blog.rockthejvm.com/scala-variance-positions/) article and [video](https://www.youtube.com/watch?v=aUmj7jnXet4)

### HKT
  - hit a wall with red scala book using Java? [continue with Scala](https://typelevel.org/blog/2016/08/21/hkts-moving-forward.html)
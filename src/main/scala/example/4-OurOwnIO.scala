package example

object OurOwnIO extends App {

  trait MyIO[A] {
    def unsafeRun(): A
    def unit(a: A): MyIO[A]
    def map[B](f: A => B): MyIO[B]
    def flatMap[B](f: A => MyIO[B]): MyIO[B]
  }

  object MyIO {

    // The main idea is as simple as this. We provide a lazily evaluated
    // argument (or 'thunk') to the constructor...
    def apply[A](thunk: => A): MyIO[A] = new MyIO[A] {

      // And provide a method tha allows us to evaluate the thunk at the time
      // of our choosing.
      override def unsafeRun(): A =
        thunk

      override def unit(a: A): MyIO[A] =
        apply(a)

      def map[B](f: A => B): MyIO[B] =
        apply(f(this.unsafeRun()))

      def flatMap[B](f: A => MyIO[B]): MyIO[B] =
        f(this.unsafeRun())
    }

  }

  val hello = MyIO[String]("Hello")
  val sayHello = hello.map(println)
  val sayHelloWorld =
    for {
      h <- hello
      hw <- MyIO(s"$h World!")
    } yield println(hw)

  hello.unsafeRun()
  sayHello.unsafeRun()
  sayHelloWorld.unsafeRun()

  // The IO provided by cats-effect however, is much more powerful than this.
  //
  // It is important to remember that the main power from using cats-effect
  // comes from the runtime rather than IO itself. The cats-effect runtime
  // automatically handles the scheduling of IOs on threads for us, and only
  // ever blocks threads sematically, meaning threads are never actually
  // blocked, but free to continue doing other work. This means that managing
  // threads as a scarce resource is not something that the developer has to
  // worry about. High level combinators for IO are provided that allow us to
  // run IOs sequentially, in parallel, in races, etc, which we will see a
  // short demo of in the next section.
  //
  // Check out the API docs for a fuller taste of what is on offer: -
  // https://typelevel.org/cats-effect/api/3.x/cats/effect/IO.html

}

package example

import scala.concurrent.Future

import cats.effect.IO

object ReferentialTransparency extends App {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  // "An expression is called referentially transparent if it can be replaced
  // with its corresponding value (and vice-versa) without changing the
  // program's behavior."
  //   - https://en.wikipedia.org/wiki/Referential_transparency
  //
  // In our example program below, we use Future to print 'foo' to the console
  // twice.

  for {
    _ <- Future(println("foo"))
    _ <- Future(println("foo"))
  } yield ()


  // If future were referentially transparent, we would expect the below
  // program to behave in the same manner, printing 'bar' twice to the console,
  // however it does not.
  val bar: Future[Unit] = Future {
    println("bar")
  }

  for {
    _ <- bar
    _ <- bar
  } yield ()

  // Instead 'bar' prints only once to the console, and to make matters worse,
  // because Future is eagerly evaluated, the side effect would be produced
  // whether or not the for comprehension was present.
  //
  // Since the side effects are produced when the Future is created, the for
  // comprehension is in effect equivalent to the below useless program:

  for {
    _ <- Future(())
    _ <- Future(())
  } yield ()

  // IO however, is referentially transparent. We observe that the two programs
  // below each have the same semantics.
  import cats.effect.unsafe.implicits.global
  {
    for {
      _ <- IO(println("qux"))
      _ <- IO(println("qux"))
    } yield ()
  }.unsafeRunSync()

  val qux: IO[Unit] = IO(println("qux"))

  {
    for {
      _ <- qux
      _ <- qux
    } yield ()
  }.unsafeRunSync()

  // This demonstrates an important distinction between Future and IO. Whilst
  // Future represents an asynchronous computation that will be performed only
  // once, is scheduled immediately and will eventually hold a result, an IO
  // represents a description of a computation that can be repeatedly executed
  // only when we tell it to run.

}

package example

import scala.concurrent.Future

import cats.effect.IO

object Evaluation extends App {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  // This will print to the console immediately, since when the Future is
  // instantiated the computation is immediately scheduled for execution in the
  // execution context.
  //
  // Since in this case the body of the future is not a pure function, we see
  // that Future can yield side effects.
  val foo: Future[Unit] = Future {
    println("foo")
  }

  // This will not print to the console unless we explicitly tell it to do so.
  // Instantiation of an IO is decoupled from its execution, that is to say
  // that IO is purely a data structure.
  //
  // Since we control when the computation runs, IO does not produce side
  // effects, but rather it produces 'effects'. This is why cats-effect, monix
  // IO, zio, etc are referred to as 'effect systems'.
  val bar: IO[Unit] = IO {
    println("bar")
  }

  // We can explicitly run IO instances by calling the unsafeRunSync() or
  // unsafeRunAsync() methods, though typically we do not wish to do this
  // directly and use IOApp instead.
  //
  // import cats.effect.unsafe.implicits.global
  // bar.unsafeRunSync()

}

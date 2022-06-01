package example

import scala.concurrent.Future

import cats.effect.IO

object Compositionality extends App {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  // Because Future has map and flatMap, we can compose futures using for
  // comprehensions.

  // for {
  //   _ <- Future {
  //     println("Going to sleep!")
  //     Thread.sleep(1000)
  //     println("foo")
  //   }
  //   _ <- Future(println("bar"))
  // } yield ()

  // However, because Future is eagerly evaluated and not referentially
  // transparent, this can result in some unexpected behaviour if we use side
  // effecting Futures that have been declared somewhere else.

  // val foo = Future {
  //   println("Going to sleep!")
  //   Thread.sleep(1000)
  //   println("foo")
  // }
  // val bar = Future {
  //   println("bar")
  // }
  // for {
  //   _ <- foo
  //   _ <- bar
  // } yield ()

  // The behaviour above means that Future only behaves like a lawful Monad
  // (where map and flatMap come from) when their body contains only a pure
  // function. As soon as the future contains side-effecting code the Monad
  // laws break down.
  //
  // See this stack overflow post for further discussion by somebody who
  // (presumably) actually knows what they're talking about with this stuff:
  //   - https://stackoverflow.com/questions/27454798/is-future-in-scala-a-monad
  //   - (note that 'bind' in this post is equivalent to 'map', and that
  //      flatMap can be implemented in terms of unit and bind)

  // IO also can be composed in a for comprehension

  // val fooIO = IO {
  //     println("Sleeping in an IO!")
  //     Thread.sleep(1000)
  //     println("foo")
  //   }
  // val barIO = IO(println("bar"))
  // val program =
  //   for {
  //     _ <- fooIO
  //     _ <- barIO
  //     _ <- fooIO
  //   } yield ()
  // import cats.effect.unsafe.implicits.global
  // program.unsafeRunSync()

}


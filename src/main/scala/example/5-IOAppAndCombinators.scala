package example

import cats.effect.{ IO, IOApp }
import scala.concurrent.duration._

// IOApp provides all that we need in order to run IO, including an execution
// context, implicit instances, and a trait for our app to extend that will
// automatically call unsafeRunSync()
object IOAppAndCombinators extends IOApp.Simple {

  def run = {
    val fooIO = IO("foo")
    val barIO = IO("bar")
    val threadAwareFoo = threadAware(fooIO.map(println))
    val threadAwareBar = threadAware(barIO.map(println))

    // Several combinators are provided to combine IO instances. The below >>
    // is equivalent to a.flatmap(_ => b), running the first IO (a) and
    // disregarding its resulting value before running the second IO (b) and
    // returning its value.
    runSequentially(threadAwareFoo, threadAwareBar) >>
      runInParallel(threadAwareFoo, threadAwareBar) >>
      returnFastest(fooIO.delayBy(10.millis), barIO.delayBy(20.millis)).void >>
      returnFastest(fooIO.delayBy(20.millis), barIO.delayBy(10.millis)).void
  }

  def threadAware[A](ioa: IO[A]): IO[A] =
    IO(println(s"${Thread.currentThread()}")) >> ioa

  // The &> below tells the cats-effect runtime to run the IOs in parallel
  def runInParallel[A](foo: IO[A], bar: IO[A]): IO[A] =
    IO(println("Running on different threads (probably)...")) >> foo &> bar

  def runSequentially[A](foo: IO[A], bar: IO[A]): IO[A] =
    IO(println("Running on the same thread...")) >> foo >> bar

  // IOs can be raced, such that they execute on different threads and the
  // result of the first to complete is returned in an Either. The losing
  // thread is cancelled.
  def returnFastest[A, B](ioa: IO[A], iob: IO[B]): IO[Either[A, B]] =
    IO.race(ioa, iob).flatTap(either => IO(println(either)))

}

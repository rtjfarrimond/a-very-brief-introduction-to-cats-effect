package example

object OurOwnIO extends App {

  // The main idea is as simple as this. The constructor is passed a lazily
  // evaluated function that produces a value that conforms to the generic
  // type.
  class MyIO[A](thunk: => A) {
    def unsafeRun(): A = thunk
  }

  val sayHello = new MyIO[Unit](println("Hello!"))

  // We can then control exactly when the function is evaluated.

  // sayHello.unsafeRun()

  // The IO provided by cats-effect however, is much more powerful than this.
  // Check out the API docs for a taste of what is on offer:
  //   - https://typelevel.org/cats-effect/api/3.x/cats/effect/IO.html

}

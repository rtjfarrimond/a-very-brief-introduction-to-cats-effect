# A Very Brief Introduction to cats-effect

This repository contains introductory material for scala programmers who have
previously worked with `Future`, but have not used cats-effect before. The
focus of the examples is to introduce `IO`, demonstrating how it is similar to,
and how it differs from, `Future`. Some combinators for IO are briefly
introduced to show how IOs can be used to perform tasks in parallel and
sequentially.

Note that cats-effect offers much more than `IO`, though in practice `IO` will
be the most commonly used construct. For a comprehensive overview of
cats-effect you can refer to [the official documentation][ce-docs]. For further
learning material I can recommend [the course offered by Rock the
JVM][rtjvm-course].


[ce-docs]: https://typelevel.org/cats-effect/
[rtjvm-course]: https://rockthejvm.com/p/cats-effect

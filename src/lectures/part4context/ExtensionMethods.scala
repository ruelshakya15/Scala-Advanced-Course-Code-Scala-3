package lectures.part4context

import scala.jdk.Accumulator

object ExtensionMethods {

  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name, nice to meet you!"
  }

  extension (string: String) // adding method to "[String]" type , NOTE: only take one(1) arg
    def greetAsPerson: String = Person(string).greet

  val danielGreeting = "Daniel".greetAsPerson

  // generic extension methods
  extension [A](list: List[A])
    def ends: (A, A) = (list.head, list.last)

  val aList = List(1, 2, 3, 4)
  val firstLast = aList.ends

  // reason: make APIs very expressive
  // reason 2: enhance "ONLY" CERTAIN types with new capabilities
  // => used in Cats, ZIO...super powerful code
  trait Combinator[A] {  // ***Refactored in SEMI GROUP in video as it is the "MATH term"
    def combine(x: A, y: A): A
  }

  extension [A](list: List[A])
    def combineAll(using combinator: Combinator[A]): A =
      list.reduce(combinator.combine)

  given intCombinator: Combinator[Int] with
    override def combine(x: Int, y: Int) = x + y

  val firstSum = aList.combineAll // work, sum is 10
  val someStrings = List("I", "love", "Scala")
  // val stringSum = someStrings.combineAll // does not compile -  no given Combinator[String] in scope

  //  grouping extensions
  object GroupedExtension {
    extension [A](list: List[A]) {
      def ends: (A, A) = (list.head, list.last)
      def combineAll(using combinator: Combinator[A]): A = // this 2 methods of above define in one extension
        list.reduce(combinator.combine)
    }
  }

  // call extension methods directly
  val firstLast_v2 = ends(aList) // same as aList.ends


  /**
   * Exercises
   * 1. Add an isPrime method to the Int type.
   * You should be able to write 7.isPrime
   * 2. Add extensions to Tree:
   *    - map(f: A => B): Tree[B]
   *    - forall(predicate: A => Boolean): Boolean
   *    - sum => sum of all elements of the tree
   */

  // "library code" = cannot change
  sealed abstract class Tree[A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]


  //  1 Ans.
  extension (number: Int) {
    def isPrime: Boolean = {
      def isPrimeAux(potentialDivider: Int): Boolean = {
        if (potentialDivider > number / 2) true
        else if (number % potentialDivider == 0) false
        else isPrimeAux(potentialDivider + 1)
      }

      assert(number >= 0) // dnt allow non negative (throws excepetion else)
      if (number == 0 | number == 1) false
      else isPrimeAux(2)

    }
  }


  //  2 Ans.
  extension [A](tree: Tree[A]) {
    def map[B](f: A => B): Tree[B] =
      tree match
        case Leaf(x) => Leaf(f(x))
        case Branch(l, r) => Branch(l.map(f), r.map(f)) // *** IMP l.map... lekhda auto complete hunna tara we CAN DO IT !!!!!!!! same goes below

    def forall(predicate: A => Boolean): Boolean =
      tree match
        case Leaf(x) => predicate(x)
        case Branch(l, r) => l.forall(predicate) && r.forall(predicate)

    def combineAll(using combinator: Combinator[A]): A = tree match
      case Leaf(value) => value
      case Branch(left, right) => combinator.combine(left.combineAll, right.combineAll)
  }

  extension (tree: Tree[Int])
    def sum: Int = {
      tree match
        case Leaf(x) => x
        case Branch(l, r) => l.sum + r.sum
    }


  def main(args: Array[String]): Unit = {
    println(danielGreeting)
    println(firstSum)
    println(2023.isPrime)

    val aTree: Tree[Int] = Branch(Branch(Leaf(3),Leaf(1)), Leaf(10))
    println(aTree.map(_ + 1))
    println(aTree.forall(_ % 2 == 0)) //  false
    println(aTree.sum) // 14
    println(aTree.combineAll) // 14
  }
}

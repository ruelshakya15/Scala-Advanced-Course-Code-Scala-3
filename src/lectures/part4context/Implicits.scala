package lectures.part4context

import scala.language.implicitConversions

object Implicits {
  // 1) givens/using clauses ->(IN SCALA 2) the ability to pass arguments automatically (implicitly) by the compiler
  //   implicit arg -> using clause
  //   implicit val -> given declaration
  trait SemiGroup[A]{ // same as "combinator" from prev chapter
    def combine(x: A, y: A): A
  }

  def combineALl[A](list: List[A])(implicit semigroup: SemiGroup[A]): A =
    list.reduce(semigroup.combine)

  implicit val intSemigroup: SemiGroup[Int] = new SemiGroup[Int]{
    override def combine(x: Int, y: Int): Int = x + y
  }
  val sumOf10 = combineALl((1 to 10).toList)

  // 2) extension methods ->(IN SCALA 2) => "Implicit classes"
  implicit class MyRichInteger(number : Int){
    // extension methods her
    def isEven = number % 2 == 0
  }

  val questionOfMyLife = 23.isEven // compiler rewrites to -> new MyRichInteger(23).isEven

  // 3) IMPLICIT CONVERSIONS ->(IN SCALA 2)(was most dangerous in scala 2) [***NOTE: Heavily discouraged to use]
  case class Person(name: String){
    def greet(): String = s"Hi, my name is $name."
  }

  // implicit conversion - SUPER DANGEROUS
  implicit def string2Person(x: String): Person = Person(x)

  val danielSaysHi = "Daniel".greet() // string2Person("Daniel").greet()

  //  implicit def were used to  => synthesize NEW implicit values
  implicit def semigroupOfOption[A](implicit semiGroup: SemiGroup[A]): SemiGroup[Option[A]] = new SemiGroup[Option[A]]{
    override def combine(x: Option[A], y: Option[A]): Option[A] = for {
      valueX <- x
      valueY <- y
    } yield(semiGroup.combine(valueX, valueY))
  }

  // Normally we would synthesize in Scala3 (same as prev chap) below
  /*
    Equivalent:
      given semigroupOfOption[A](using semigroup: Semigroup[A]): Semigroup[Option[A]] with ...
   */

  // organizing implicits == organizing contextual abstractions (same principles)
  // import yourPackage.* // also imports implicits

  /*
    Why implicits will be phased out:
    - the implicit keyword has many different meanings
    - conversions are easy to abuse
    - implicits are very hard to track down while debugging (givens also not trivial, but they are explicitly imported)
   */


  /*
     The right way of doing contextual abstractions in Scala 3:
     - given/using clauses
     - extension methods
     - explicitly declared implicit conversions
    */

  def main(args: Array[String]): Unit = {
    println(sumOf10)
  }
}

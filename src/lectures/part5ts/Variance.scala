package lectures.part5ts

object Variance {

  class Animal
  class Dog(name: String) extends Animal

  // Variance question for List: if Dog extends Animal, then should a List[Dog] "extend" List[Animal]?

  // 1) for List, YES - List is COVARIANT
  val lassie = new Dog("Lassie")
  val hachi = new Dog("Hachi")
  val laika = new Dog("Laika")

  val anAnimal: Animal = lassie // ok, Dog<: Animal (Dog Subtype Animal)
        //    LEFT(provide min req) ===> RIGHT(more specialized list)
  val myDogs: List[Animal] = List(lassie, hachi, laika) // ok - List is COVARIANT[+A] : a list of dogs is list of animals

  //  define covariant types
  class Mylist[+A] // Mylist is COVARIANT in A
  val aListOfAnimals: Mylist[Animal] = new Mylist[Dog]

  // 2) if NO, then type is INVARIANT
  trait SemiGroup[A]{ // no marker = INVARIANT
    def combine(x: A, y:A): A
  }

  // java generics(everything in JAVA is INVARIANT)
  // val aJavaList: java.util.ArrayList[Animal] = new java.util.ArrayList[Dog] // not ok,type mismatch : java generics are all INVARIANT

  //  3) Hell No - CONTRAVARIANCE
  // OPPOSITE!!! if Dog <: Animal, then Vet[Animal] <: Vet[Dog]
  trait Vet[-A]{ // contravariant in A
    def heal(animal: A): Boolean
  }

  //      LEFT(Vet of Dog req)==> RIGHT(Vet of all/any animals[so better])
  val myVet: Vet[Dog] = new Vet[Animal] {
    override def heal(animal: Animal): Boolean = {
      println("hey there, you're all good....")
      true
    } 
  }
  // if the vet can treat any animal, she/he can treat my dog too
  val healLaika = myVet.heal(laika) // ok

  /*
      Rule of thumb: ***[AlSO CHECK VARIANCE EXERCISE RULE TO BETTER UNDERSTAND]
      - if your type PRODUCES or RETRIEVES a value (e.g. a list), then it should be COVARIANT
      - if your type ACTS ON or CONSUMES a value (e.g. a vet), then it should be CONTRAVARIANT [Acting on animal  by healing eg above]
      - otherwise, INVARIANT
     */

  /**
   * Exercises
   */
  // 1 - which types should be invariant, covariant, contravariant
  class RandomGenerator[+A] // produces values: Covariant
  class MyOption[+A] //  similar to Option[A] (at most only value stored and can be retrieved)
  class JSONSerializer[-A]  // consumes values and turns them into Strings: Contravariant
  trait MyFunction[-A, +B] // similar to Function1[A, B] [consumes element of type A, produces element of type B]


  //  2 - add variance modifiers to this "library"
  abstract class LList[+A] {
    def head : A
    def tail :  LList[A]
  }

  case object EmptyList extends LList[Nothing] {  // because "Nothing" is at bottom of type hierarchy
    override def head = throw new NoSuchElementException()
    override def tail = throw new NoSuchElementException()
  }

  case class Cons[+A](override val head: A, override val tail:LList[A]) extends LList[A]

  val aList: LList[Int] = EmptyList // fine
  val anotherList: LList[String] = EmptyList // also fine
  //  Nothing <: A, then LList[Nothing] <: LList[A]

  def main(args: Array[String]): Unit = {

  }
}

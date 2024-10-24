package lectures.part4context

object OrganizingCAs {

  val aList = List(2, 3, 1, 4)
  val anOrderedList = aList.sorted

  // How compiler fetches givens/ EMs(extension methods)? (In below Order)
  // 1 - local scope
  given reverseOrdering: Ordering[Int] with
    override def compare(x: Int, y: Int): Int = y - x

  //  2 - imported scope
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 67)
  )

  object PersonGivens {
    given ageOrdering: Ordering[Person] with
      override def compare(x: Person, y: Person): Int = y.age - x.age

    extension (p: Person)
      def greet(): String = s"Hello, I'm ${p.name}, I' m so glad to meet you"
  }
  //  a - import explicitly
  //  import PersonGivens.ageOrdering

  // b - import a given for a particular type( when you dont know the name of given)
  // import PersonGivens.{given Ordering[Person]}

  // c - import all givens
  //  import PersonGivens.given

  // warning: import PersonGivens.* does NOT also import given instances

  // 3 - companions of all types involved in method signature
  /*
    - Ordering
    - List
    - Person
   */
  // def sorted[B >: A](using ord: Ordering[B]): List[B]

  object Person {
    given byNameOrdering: Ordering[Person] with
      override def compare(x: Person, y: Person): Int =
        x.name.compareTo(y.name)

    extension (p: Person)
      def greet(): String = s"Hello, I'm ${p.name}"
  }

  val sortedPersons = persons.sorted

  /*** IMP
     GOOD PRACTICE TIPS TO USE IMPLICITS:
     1) When you have a "default" given (only ONE that makes sense) add it in the companion object of the type.
     2) When you have MANY possible givens, but ONE that is dominant (used most), add that in the companion and the rest in other objects.
     3) When you have MANY possible givens and NO ONE is dominant, add them in separate objects and import them explicitly.
    */

  // SAME PRINCIPLES APPLY TO EXTENSION METHODS AS WELL.

  /**
   * Exercises. Create given instances for Ordering[Purchase]
   * - ordering by total price, descending = 50% of code base
   * - ordering by unit count, descending = 25% of code base
   * - ordering by unit price, ascending = 25% of code base
   */

  def main(args: Array[String]): Unit = {
    println(anOrderedList)
    println(sortedPersons)
    import PersonGivens.* // includes extension methods <-- this takes PRECEDENCE over companion object extension methods as it is placed here
    println(Person("Daniel",99).greet())
  }
}

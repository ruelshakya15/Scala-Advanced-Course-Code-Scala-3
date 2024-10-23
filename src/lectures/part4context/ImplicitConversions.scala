package lectures.part4context

//SPECIAL IMPORT
import scala.language.implicitConversions

object ImplicitConversions {

  case class Person(name: String){
    def greet() : String = s"Hi , I am $name how are you?"
  }

  val daniel = Person("Daniel")
  val danielSaysHi = daniel.greet()

  //  special conversion instance
  given string2Person: Conversion[String, Person] with
    override def apply(x: String): Person = Person(x)

  val danielSaysHi_v2 = "Daniel".greet() // auto-box(auto convert String -> Person)

  def processPerson(person: Person): String =
    if (person.name.startsWith("J")) "OK"
    else "NOT OK"

  val isJaneOK = processPerson("Jane") // ok - compiler rewrites  to processPerson(Person("Jane"))

  /*
    USE-CASES FOR IMPLICIT CONVERSIONS
      - auto-box types
      - use multiple (often unrelated) types with the same meaning in your code, interchangeably
   */



  def main(args: Array[String]): Unit = {

  }

}

package lectures.part4context

object TypeClasses {  //***IMP TYPE CLASSES(TC) can be thought of as a design pattern!!
  /*
    Small library to serialize some data to standard format (HTML)
   */

  //  V1: the OO way
  trait HTMLWritable{
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable{
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/></div>"
  }

  val bob = User("Bob", 43, "bob@rockthejvm.com")
  val bob2Html = bob.toHTML
  // same for other data structures that we want to serialize

  /*
    Drawbacks:
    - only available for the types WE write
    - can only provide ONE implementation
   */

  // V2: pattern matching ( Scala Way)
  object HTMLSerializerPM {
    def serializeToHtml(value: Any): String = value match
      case User(name, age, email) => s"<div>$name ($age yo) <a href=$email/></div>"
      case _ => throw new IllegalArgumentException("data struct not supported")
  }
  /*
      Drawbacks:
      - lost type safety
      - need to modify a SINGLE piece of code every time
      - still ONE implementation
     */

  // V3 - type class
  // part 1 - type class definition
  trait HTMLSerializer[T]{
    def serialize(value: T): String
  }

  // part 2 - type class instance for the supported types
  given userSerializer: HTMLSerializer[User] with{
    override def serialize(value: User): String = {
      val User(name, age, email) = value                                                                    //***IMP different SUGAR on how to deconstruct User using pattern matching (WEIRD)
      s"<div>$name ($age yo) <a href=$email/></div>"
    }
  }

  val bob2Html_v2 = userSerializer.serialize(bob)

  /*
    Benefits:
    - can define serializers for other types OUTSIDE the "library"
    - multiple serializers for the same type, pick whichever you want
   */
  import java.util.Date
  given dateSerializer: HTMLSerializer[Date] with{
    override def serialize(date: Date): String =  s"<div>${date.toString}</div>"          // WE can so define serializers(any TYPE) outside of typeclass DEFn
  }

  object SomeOtherSerializerFunctionality{ // organize givens properly
    given partialUserSerializer : HTMLSerializer[User] with{
      override def serialize(user: User): String = s"<div>${user.name}</div>"     //defined another "USER" type serializer
    }
  }

 // part 3 - using the type class (user-facing API)
   object HTMLSerializer{ //defined in companion object (same principle explained in ORGANIZINGCAs.scala code)
     def serialize[T](value: T)(using serializer: HTMLSerializer[T]): String =
       serializer.serialize(value)

     def apply[T](using serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
   }

  val bob2Html_v3= HTMLSerializer.serialize(bob)
  val bob2Html_v4= HTMLSerializer[User].serialize(bob)        // from "apply" method above NOTE: it does not take parameters so "HTMLSerializer[User]" etti part le nai -> serializer fyalcha
  

  //  part 4
  object HTMLSyntax {
    extension [T](value: T)
      def toHTML(using serializer: HTMLSerializer[T]): String = serializer.serialize(value) // different from top .toHtml not ALL CAPS
  }

  import HTMLSyntax.*
  val bob2Html_v5 = bob.toHTML

  /*
      Cool!
      - extend functionality to new types that we want to support
      - flexibility to add TC instances in a different place than the definition of the TC      TC = Type class
      - choose implementations (by importing the right givens)
      - super expressive! (via extension methods)
     */



  def main(args: Array[String]): Unit = {
    println(bob2Html)
    println(bob2Html_v2)
    println(bob2Html_v3)
    println(bob2Html_v4)
    println(bob2Html_v5)
  }
}

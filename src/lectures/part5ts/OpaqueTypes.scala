package lectures.part5ts

import lectures.part5ts.OpaqueTypes.SocialNetwork

object OpaqueTypes {
  //*** NOTE: SCALA 3 Specific!!!!!!!!!!!

  object SocialNetwork {
    // usually applied inside tooling/libraries managing data types
    // domain = all the type definitions for your business use case
    opaque type Name = String

    // API entry point #1 - companion objects, useful for factory methods
    object Name {
      def apply(str: String): Name = str
    }

    // API entry point #2 - extension methods, give you control of the methods YOU want to expose
    extension (name: Name)
      def length: Int = name.length // using String API

    // inside, Name <-> String interchangeable within class
    def addFriend(person1: Name, person2: Name): Boolean =
      person1.length == person2.length //   use the entire String API
  }

  // outside SocialNetwork, Name and String are NOT related even after importing (DUE TO OPAQUE)
  import SocialNetwork.*
//  val name: Name = "Daniel" //  will not compile, Name != String

  // why opaque types: when you don't need (or want) to offer access to the entire String API for the Name type

  object Graphics {
    opaque type Color = Int // in hex
    opaque type ColorFilter <: Color = Int

    val Red: Color = 0xFF000000
    val Green: Color = 0x00FF0000
    val Blue: Color = 0x0000FF00
    val halfTransparency: ColorFilter = 0x80 // 50%
  }

  import Graphics.*
  case class OverLayFilter(c: Color) // we have access to "Color" type but dont know implementation so if used like in line 19 might cause compiler errors
  val fadeLayer = OverLayFilter(halfTransparency) // because ColorFilter <: Color

  // how can we create instances of opaque types + how to access their APIs
  // 1 - companion objects
  val aName = Name("Daniel") // ok , using the companion object
  // 2 - extension methods
  val nameLength = aName.length

  def main(args: Array[String]): Unit = {

  }

}

package lectures.part5ts

object PathDependentTypes {

  class Outer{
    class Inner
    object InnerObject
    type InnerType

    def process(arg: Inner) = println(arg)
    def processGeneral(arg: Outer#Inner) = println(arg)
  }

  val outer = new Outer
  val inner = new outer.Inner // outer.Inner is a separate TYPE = path-dependent type

  val outerA = new Outer
  val outerB = new Outer
  //  val inner2: outerA.Inner = new outerB.Inner // does not compile , each Inner its own type // path-dependent types are DIFFERENT
  val innerA = new outerA.Inner
  val innerB = new outerB.Inner

  // outerA.process(innerB) // same type mismatch
  outer.process(inner) // ok

  // Parent-type: Outer#Inner
  outerA.processGeneral(innerA) // ok
  outerA.processGeneral(innerB) // ok, outerB.Inner <: Outer#Inner [PARENT TYPE] of all inner

  /*
      Why: used by diff libraries
      - type-checking/type inference, e.g. Akka Streams: Flow[Int, Int, NotUsed]#Repr
      - type-level programming
     */


  // methods with dependent types: return a different COMPILE-TIME type depending on the argument
  //  no need for generics
  trait Record{
    type Key
    def defaultValue: Key
  }

  class StringRecord extends Record{
    override type Key = String
    override def defaultValue: String = ""
  }

  class IntRecord extends Record{
    override type Key = Int
    override def defaultValue: Int = 0
  }

  // user facing API                        path dependent type depending on provided instance
  def getDefaultIdentifier(record: Record): record.Key = record.defaultValue
  
  val aString: String = getDefaultIdentifier(new StringRecord) // a string [ because compiler auto finds out that type => record.Key is String here
  val anInt: Int = getDefaultIdentifier(new IntRecord) // an int, ok
  
  //  functions with dependent types
  val getIdentifierFunc: Record => Record#Key = getDefaultIdentifier // what kind of function do we have?


  def main(args: Array[String]): Unit = {

  }

}

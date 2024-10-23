package lectures.part5ts

object LiteralUnionIntersectionTypes {


  // 1 - literal types
  val aNumber = 3
  val three: 3 = 3

  def passNumber(n: Int) = println(n)
  passNumber(45) // ok
  passNumber(three) // ok, 3 <: Int

  def passStrict(n: 3) = println(n)
  passStrict(three) // ok
  // passStrict(45) // not ok, Int >: 3

  // available for double, boolean, strings
  val pi: 3.14 = 3.14
  val truth: true = true
  val favLang: "Scala" = "Scala"

  // literal types can be used as type arguments (just like any other types)
  def doSomethingWithYourLife(meaning: Option[42]) = meaning.foreach(println)

  //  2 - union types
  val truthOr42: Boolean | Int = 43

  def ambivalentMethod(arg: String | Int): String = arg match
    case _: String => "a String"
    case _: Int => "a number" // PM complete

  val aNumberDesc = ambivalentMethod(56) // ok
  val aStringDesc = ambivalentMethod("Scala") // ok

  // type inference chooses a LCA(lowest common ancestor)[i.e : Any assigned below ] of the 2 types instead of String | Int
  val stringOrInt = if (43 > 0) " a string" else 45
  val stringOrInt_v2: String | Int = if (43 > 0) " a string" else 45 // ok

  // union type + [USING nulls]
  type Maybe[T] = T | Null // not null
  def handleMaybe(someValue: Maybe[String]): Int =
    if(someValue != null) someValue.length //*** flow typing -> NOTE: eta auto complete hudaina .length  because of  | type inference & we checked that value is not null
    else 0

  //  union type + except NUll doesnt work
  //  type ErrorOr[T] = T | "error"
  //  def handleResource(arg: ErrorOr[Int]): Unit =
  //    if(arg != "error") println(arg + 1) // flow typing doesnt work here
  //    else println("Error")

  //3 - intersection types
  class Animal
  trait Carnivore
  class Crocodile extends Animal with Carnivore
  val carnivoreAnimal: Animal & Carnivore = new Crocodile

  trait Gadget{
    def use(): Unit
  }

  trait Camera extends Gadget {
    def takePicture() = println("smile!")
    override def use() = println("snap")
  }

  trait Phone extends Gadget {
    def makePhoneCall() = println("calling ....")
    override def use() = println("ring")
  }

  def useSmartDevice(sp: Camera & Phone): Unit = {
    sp.takePicture()
    sp.makePhoneCall()
    sp.use() // which use() is being called ? [SIMILAR TO DIAMOND PROBLEM in AdvancedInheritance] => can't tell
  }

  class SmartPhone extends Phone with Camera // diamond problem <-- acc to last inherited .use is printed
  class CameraWithPhone extends Camera with Phone

  // intersection types + covariance
  trait HostConfig
  trait HostController{
    def get: Option[HostConfig]
  }

  trait PortConfig
  trait PortController{
    def get: Option[PortConfig]
  }
                                                              // because Option is Covariant
  def getConfigs(controller: HostController & PortController): Option[HostConfig & PortConfig] = controller.get // compiles

  def main(args: Array[String]): Unit = {
    useSmartDevice(new SmartPhone) // "snap"
    useSmartDevice(new CameraWithPhone) // "ring"
  }

}

package lectures.part5ts

//***IMP import
import reflect.Selectable.reflectiveSelectable

object StructuralTypes {

  type SoundMaker = { // structural type
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }
  class Car{
    def makeSound(): Unit = println("vroom!")
  }

  val dog: SoundMaker = new Dog // ok
  val car: SoundMaker = new Car // ok
  //  compile-time duck typing

  //**************************
  //  type refinements
  abstract class Animal {
    def eat(): String
  }

  type WalkingAnimal = Animal { // refined type( We extend animal here hence refined)
    def walk(): Int
  }

  //****************************************
  // why (USE of Structural Types) : creating type-safe APIs for existing types following the same structure, but no connection to each other
  type JavaCloseable = java.io.Closeable
  class CustomCloseable {
    def close(): Unit = println("ok, ok I'm closing")
    def closeSilently(): Unit = println("not making a sound, I promise")
  }

  //  def closeResource(closeable: JavaCloseable | CustomCloseable): Unit =
  //    closeable.close() // not ok

  // solution: structural type
  type UnifiedCloseable = {
    def close(): Unit
  }

  def closeResource(closeable: UnifiedCloseable): Unit = closeable.close()
  val jCloseable = new JavaCloseable{
    override def close(): Unit = println("closing Java resource")
  }
  val cCloseable = new CustomCloseable

  //  instead of defining type "UnifiedCloseable" we can also include it in arg defn
  def closeResource_v2(closeable: { def close(): Unit}): Unit = closeable.close()

  def main(args: Array[String]): Unit = {
    dog.makeSound() // invoking method os "STRUCTURAL TYPE" are not done like thi but through -> reflection(slow)
    car.makeSound()

    closeResource(jCloseable)
    closeResource(cCloseable)
  }

}

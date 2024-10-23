package lectures.part5ts

object TypeMembers {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection{
    //  val, var, def, class, trait, object
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // abstract type member with a type bound
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalAlias = Cat // type alias
    type NestedOption = List[Option[Option[Int]]] // often used to alias complex/nested types
  }

  class MoreConcreteAnimalCollection extends AnimalCollection{
    override type AnimalType = Dog
  }


  // using Type member
  val ac = new AnimalCollection
  val anAnimal: ac.AnimalType = ???

  // val cat: ac.BoundedAnimal = new Cat // not compile, BoundedAnimal might be Dog(ambiguous must be defined Precisely look at video to understand) [MUST DEFINE ABSTRACT TYPE as done in line 18 or line 43]
  val aDog: ac.SuperBoundedAnimal = new Dog // ok, Dog <: SuperBoundedAnimal
  val aCat: ac.AnimalAlias = new Cat // ok, Cat == AnimalAlias

  // establish relationships between types
  // alternative to generics
  class LList[T]{
    def add(element: T): LList[T] = ???
  }

  class MyList{
    type T
    def add(element: T): MyList = ???
  }

  // somewhat esari use garne abstract type must be defined before use
  val x =new MyList{
    override type T = Int
  }.add(5)

  //  .type
  type CatType = aCat.type //so now "CatType" refers to whatever type aCat is (i.e. Cat in this case)
  val newCat: CatType = aCat


  def main(args: Array[String]): Unit = {

  }
}

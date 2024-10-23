package lectures.part5ts

object VariancePositions {
//  *** READ PDF FOR SUMMARY
//********* IMP!!!!!!!!! => any confusion look at scala theory or ask GPT good explanations given

  class Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // 1 - TYPE BOUNDS
  class Cage[A <: Animal] // A must extend Animal(subtype of Animal)
  val aRealCage = new Cage[Dog] // ok, Dog <: Animal
  // val aCage = new Cage[String] // not ok, String is not a subtype of animal

  class WeirdContainer[A >: Animal] // A must be supertype Animal

  // *********************************************************************************

  //  2 - variance positions
  //a) :
  // class Vet[-T](val favoriteAnimal: T) // "TYPES OF VAL FIELDS" are in COVARIANT position [***IMP "BUT COVARIANT can take VAL FIELD like Option Type"]

  /*
  // LOOK AT VIDEO TO UNDERSTAND
    val garfield = new Cat
    val theVet: Vet[Animal] = new Vet[Animal](garfield)
    val aDogVet: Vet[Dog] = theVet // possible, theVet is Vet[Animal]
    val aDog: Dog = aDogVet.favoriteAnimal // must be a Dog - type conflict!
   */

  // b) : "TYPE OF VAR FIELD" are in COVARIANT position
  // (same reason)

  // c) : "TYPES OF VAR FIELDS" are in CONTRAVARIANT positions
  // class MutableOption[+T](var contents: T)

  /*
    val maybeAnimal: MutableOption[Animal] = new MutableOption[Dog](new Dog)
    maybeAnimal.contents = new Cat // type conflict!
   */

  // d) : "TYPES OF METHOD ARGUMENTS" are in CONTRAVARIANT positions
  //  class MyList[+T]{
  //    def add(element: T): MyList[T] = ???
  //  }

  /*
      val animals: MyList[Animals] = new MyList[Cat] // <-- adding "dog" to this list of "Cats" type conflict
      val biggerListOfAnimals = animals.add(new Dog) // type conflict ! [***IMP: because we are adding a "dog" to a list of "cats", tyo Type T nai diff diff vo]
     */

  // e) same as above d) fine if our class type is CONTRAVARIANT(can define method arguments normally for CONTRAVARIANT types)
  class Vet[-T] {
    def heal(animal: T): Vet[T] = ???
  }

  //  f) "METHOD RETURN TYPES"  are in COVARIANT position
  //  abstract class Vet2[-T] {
  //    def rescueAnimal(): T
  //  }

  /*
    val vet: Vet2[Animal] = new Vet2[Animal] {
      override def rescueAnimal(): Animal = new Cat  // ***Because Cat is subtype yo ta naturally possible cha ni sub garna MyList[Animal] -> MyList[Cat] yo sub garne po question ho ni
    }
    val lassiesVet: Vet2[Dog] = vet // Vet2[Animal]
    val rescueDog: Dog = lassiesVet.rescueAnimal() // ***IMP: must return Dog,[as lassiesVet is of type Vet2[Dog] acc to compiler], but returns a Cat because of our def above - type conflict !
   */


//***********************************************************************************************************

  /**
   * 3 - Solving variance positions problems
   */
  //  a) for COVARIANCE:
  abstract class LList[+A] {
    def head: A 
    def tail: LList[A]
    def add[B >:A](element: B): LList[B] // WIDEN the type
  }

 

  // val animals: List[Cat] = list of cats    // WATCH VIDEO if hard( compiler auto pick common SUPERTYPE of assign type to B )
  // val newAnimals: List[Animals] = animals.add(new Dog)


  // b) for CONTRAVARIANCE:
  class Vehicle
  class Car extends Vehicle
  class Supercar extends Car
  class RepairShop[-A <: Vehicle] {
    def repair[B <: A](vehicle: B): B = vehicle // NARROWING the type
  }

  val myRepairShop: RepairShop[Car] = new RepairShop[Vehicle]
  val myBeatupVw = new Car
  val freshCar = myRepairShop.repair(myBeatupVw) // work, return a car
  val damagedFerrari = new Supercar
  val freshFerrari = myRepairShop.repair(damagedFerrari) // works, return a Supercar

  // val ownTestRepair = myRepairShop.repair(new Vehicle) // ***IMP note that myRepairShop ma [Vehicle] type pass gareni annotation ma [Car] type vayera this does not work because [B <: A] vaneko Car ko subtype or Car only passable (not [Vehicle ko type])

  

  def main(args: Array[String]): Unit = {

  }

}

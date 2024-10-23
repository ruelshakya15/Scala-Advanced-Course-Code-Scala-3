package lectures.part5ts

object FBoundedPolymorphism {

  object Problem {
    trait Animal{
      def breed: List[Animal]
    }

    class Cat extends Animal{
      override def breed: List[Animal] = List(new Cat, new Dog) // <--problem !!! can define Dog here (so lose type safety)
    }

    class Dog extends Animal{
      override def breed: List[Animal] = List(new Dog, new Dog, new Dog)
    }

    // losing type safety
  }

//****************************************

  object NaiveSolution{
    trait Animal {
      def breed: List[Animal]
    }

    class Cat extends Animal {
      override def breed: List[Cat] = List(new Cat, new Cat)
    }//                  ^^ Because List is COVARIANT

    class Dog extends Animal {
      override def breed: List[Animal] = List(new Dog, new Dog, new Dog)
    }

    // I have to write the proper type signature
    // problem: want the compiler to help
  }

  //********************************************
  object FBP {
    trait Animal[A <: Animal[A]]{ // recursive type. F-bounded polymorphism
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = List(new Cat, new Cat)
    }//                             ^^ eta auto Cat aaucha

    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = List(new Dog, new Dog, new Dog)
    }
    // if I want to mess up FBP
    class Crocodile extends Animal[Dog]{ // <-- can mess up here
      override def breed = ??? // list of dogs
    }
  }


  //FBP used in
  // example: some ORM libraries
  trait Entity[E <: Entity[E]]
  // example: Java sorting library
  class Person extends Comparable[Person]{ // FBP
    override def compareTo(o: Person): Int = ???
  }

  //***************************************
  //  FBP + self types
  object FBPSelf {
    trait Animal[A <: Animal[A]]{ self: A => // indicate for : type must extend themselves => Cat must extend Animal[Cat]
       def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] { // Cat == Animal[Cat] same as
      override def breed: List[Animal[Cat]] = List(new Cat, new Cat)
    }

    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = List(new Dog, new Dog, new Dog)
    }

    //    class Crocodile extends Animal[Dog] { // not ok, I must also extend Dog (because of self: A)
    //      override def breed = ??? // list of dogs
    //    }

    // I can go one level deeper
    trait Fish extends Animal[Fish]
    class Cod extends Fish {
      override def breed = List(new Cod, new Cod)
    }

    class Shark extends Fish {
      override def breed: List[Animal[Fish]] = List(new Cod) // wrong
    }

    // solution level 2
    trait FishL2[A <: FishL2[A]] extends Animal[FishL2[A]] { self: A => }
    class Tuna extends FishL2[Tuna]{
      override def breed = List(new Tuna)
    }
  //    // not ok
  //    class SwordFish extends FishL2[SwordFish]{
  //      override def breed = List(new Tuna) // tuna cannot be included now
  //    }
  }



  def main(args: Array[String]): Unit = {

  }
}

package lectures.part5ts

object Scala2_VarianceExercise {


  /** EXERCISE from SCALA 2 course

   * 1. Invariant, covariant, contravariant
   * Parking[T](things: List[T]) {
   * park(vehicle: T)
   * impound(vehicles: List[T])
   * checkVehicles(conditions: String): List[T]
   * }
   *
   * 2. used someone else's API: IList[T]
   * 3. Parking = monad!
   *     - flatMap
   */
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]):IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]):CParking[S] = ??? // because List is also Covariant
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[B <: T](conditions: String): List[B] = ??? // List is Covariant but T being contravariant make List also contravariant so need to change

    // def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???  // Can be written as below
    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): XParking[S] = ??? // Since Function1 is CONTRAVARIANT, & T also CONTRAVARIANT = as a Whole becomes COVARIANT THAT OCCURS IN CONTRAVARIANT Postion
  }

  /*
      Rule of thumb
      - use covariance = COLLECTION OF THINGS
      - use contravariance = GROUP OF ACTIONS you want to perform on your type
     */



  class CParking_v2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking_v2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking_v2[S] = ??? // invariant position[ BETTER TO WATCH VIDEO FOR THESE 2]
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XParking_v2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking_v2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking_v2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }




  def main(args: Array[String]): Unit = {

  }

}

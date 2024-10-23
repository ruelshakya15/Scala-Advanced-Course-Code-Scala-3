package lectures.part5ts

object AdvancedInheritance {
  // INHERITANCE PROBLEMS

  // 1 - composite types can be used on their own
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Stream[T] {
    def foreach(f : T => Unit): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }
  // class MyDataStream extends Writer[String] with Stream[String] with Closeable {..}

  //Type annotation
  def processStream[T](stream: Writer[T] with Stream[T] with Closeable): Unit = {  // if u dont know the name of class like above"MyDataStream" that implements all the trait u can declare it as so
    stream.foreach(println)
    stream.close(0)
  }

  //  2 - diamond problem

  trait Animal{ def name: String}
  trait Lion extends Animal{ override def name: String = "Lion" }
  trait Tiger extends Animal{ override def name: String = "Tiger" }
  class Liger extends Lion with Tiger

  def demoLiger(): Unit = {
    val liger = new Liger
    println(liger.name)
  }

  /*
  Pseudo-definition:

    class Liger extends Animal
    with { override def name = "Lion"
    with { override def name = "Tiger"}

    Last override always WINS!!
   */

  //  3 - the super problem

  trait Cold { // cold colors
    def print() = println("cold")
  }

  trait Green extends Cold{
    override def print(): Unit = {
      println("green")
      super.print()
    }
  }

  trait Blue extends Cold{
    override def print(): Unit = {
      println("blue")
      super.print()
    }
  }

  class Red{
    def print(): Unit = println("red")
  }

  class White extends Red with Green with Blue{
    override def print(): Unit = {
      println("white")
      super.print()
    }
  }

  /*
    Expected result
    - white
    - red

    Actual result
    - white
    - blue
    - green
    - cold
    NO RED!!!
    See the slide explanation on why this happens.
   */

  def demoColorInheritance(): Unit = {
    val white = new White
    white.print()
  }

  def main(args: Array[String]): Unit = {
    val liger = new Liger
    demoLiger()
    demoColorInheritance()
  }

}

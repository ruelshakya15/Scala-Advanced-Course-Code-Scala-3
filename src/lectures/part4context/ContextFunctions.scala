package lectures.part4context

import scala.concurrent.{ExecutionContext, Future}

object ContextFunctions {

  val aList = List(2,1,3,4)
  val sortedList = aList.sorted

  // defs can take using claused
  def methodWithoutContextArguments(nonContextArg: Int)(nonContextArg2: String) : String = ???
  def methodWithContextArguments(nonContextArg: Int)(using nonContextArg2: String) : String = ???

  // eta - expansion (convert methods -> function values)
  val func1 = methodWithoutContextArguments
  // val func2 = methodWithContextArguments // doesnt' work

  //SCALA3 SPECIFIC -> context function           ?=> means "given"
  val functionWithContextArguments: Int => String ?=> String = methodWithContextArguments // now IT WORKS
  val someResult = functionWithContextArguments(2)(using  "scala") // another way

  /*
      Use cases:
        - convert methods with using clauses to function values
        - HOF with function values taking given instances as arguments
        - requiring given instances at CALL SITE, not at DEFINITION [explained below]
     */
  // execution context(EC)[Keyword] here
  // val incrementAsync: Int => Future[Int] = x => Future(x + 1) // doesn't work w/o a given EC in scope(problem = need to be in scope of defn)

  val incrementAsync: ExecutionContext ?=> Int => Future[Int] = x => Future(x + 1)
  def main(args: Array[String]): Unit = {

  }

}

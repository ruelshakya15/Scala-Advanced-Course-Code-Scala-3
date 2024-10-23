package lectures.part4context

import java.util.Date

object JSONSerialization {

    /*
      Users, blog-post, feeds
       Serialize to JSON
     */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, post: List[Post])

  /*
    1 - Intermediate data: numbers, strings, list, objects
    2 - type class to convert data to intermediate data
    3 - serialize to JSON
   */

  sealed trait JSONValue{
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue{
    override def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue{
    override def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue{
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify: String = values.
      map{
      case (key, value) => "\"" + key + "\":" + value.stringify
    }
      .mkString("{",",","}")
  }

  /*
     {
       "name": "John",
       "age": 22,
       "friends": [...],
       "latestPost" : { ... }
     }
    */
  val data = JSONObject(Map(
    "user" -> JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala is Awesome"),
      JSONNumber(42)
    ))
  ))

  //  part 2 - type class pattern ***IMP follow belo 4 steps to implement TC(also mentioned in TypeClasse.scala)
  //  step 1 - TC definition
  trait JSONConverter[T] {
    def convert(value: T) : JSONValue
  }

  //  step 2 - TC instances for (String, Int, Date)<- primitive types in -> (User, Post, Feed)
  given stringConverter: JSONConverter[String] with
    override def convert(value: String): JSONValue = JSONString(value)

  given intConverter: JSONConverter[Int] with
    override def convert(value: Int): JSONValue = JSONNumber(value)

  given dateConverter: JSONConverter[Date] with
    override def convert(value: Date): JSONValue = JSONString(value.toString)

  given userConverter: JSONConverter[User] with
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONConverter[String].convert(user.name),             //instead of manual JSONString(user.name) we use the givens defined above "stringConverted,intConverter...."
      "age" -> JSONConverter[Int].convert(user.age),                                          //|--> tyo vanda ni better use JSONConverter[Some Type] so given of that type cha vane we just use that instead of explicitly using "stringConverter...
      "email" -> JSONConverter[String].convert(user.name)                                         ///|--> this is done so we can use other givens if multiple defined
    ))

  given postConverter: JSONConverter[Post] with
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONConverter[String].convert(post.content),
      "createAt" -> JSONConverter[String].convert(post.createdAt.toString)
    ))

  given feedConverter: JSONConverter[Feed] with
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> JSONConverter[User].convert(feed.user),
      "posts" -> JSONArray(feed.post.map(post => JSONConverter[Post].convert(post)))
    ))

  //  step 3 - user facing API
  object JSONConverter{
    def convert[T](value: T)(using converter: JSONConverter[T]): JSONValue =
      converter.convert(value)

    def apply[T](using instance: JSONConverter[T]): JSONConverter[T] = instance
  }

  // example
  val now= new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@rockthejvm.com")
  val feed = Feed(john, List(
    Post("Hello, I'm learning type clases", now),
    Post("Look at this cute puppy!", now)
  ))

  //  step 4 - extension methods
  object JSONSyntax{
    extension [T](value: T){
      def toIntermediate(using converter: JSONConverter[T]): JSONValue =
        converter.convert(value)

      def toJSON(using converter: JSONConverter[T]): String =
        toIntermediate.stringify
    }
  }


  def main(args: Array[String]): Unit = {
    println(data.stringify)
    println(JSONConverter.convert(feed))
    println(JSONConverter.convert(feed).stringify) // still clunky so we add step 4 above

    import JSONSyntax.*
    println(feed.toIntermediate.stringify)

    println(feed.toJSON) // much more cleaner by adding 2 methods in "JSONSyntax" extensions
  }
}

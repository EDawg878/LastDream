import java.nio.file.{Paths, Files}

import play.api.libs.json.Json

import scala.util.Try

case class Question(id: String, prompt: Seq[String], text: Seq[String], options: Seq[String])

object LastDream {

  implicit val questionFormat = Json.format[Question]
  val files = Seq("matt.json", "christian.json", "terry.json")

  def main(args: Array[String]): Unit = {
    val root = Paths.get(getClass.getResource("stories").toURI)
    val questions = files.map(root.resolve) flatMap { path =>
      val bytes = Files.readAllBytes(path)
      Json.parse(bytes).as[Seq[Question]]
    }
    val questionsById = (questions map { q => (q.id, q) }).toMap
    val start = questionsById("start")
    var next = ask(start, questionsById)
    while (next.nonEmpty) {
      next = ask(next.get, questionsById)
    }
  }

  def ask(question: Question, questions: Map[String, Question]): Option[Question] = {
    println()
    question.text.foreach(println)
    println()
    val options = question.options.map(questions)
    options.zipWithIndex.foreach { case (q, i) =>
        println(s"${i + 1}) ${q.prompt.mkString("\n")}")
    }
    print("> ")
    val n = readInt()
    val choice = Try(options.toSeq(n - 1)).toOption
    choice map { q =>
       if (q.id.endsWith("_end")) None
       else Some(q)
    } getOrElse {
      println("Invalid option!")
      Some(question)
    }
  }

}


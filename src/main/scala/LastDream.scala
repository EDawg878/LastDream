import java.nio.file.{Paths, Files}

import play.api.libs.json.Json

import scala.util.Try

case class Question(id: String, prompt: Seq[String], text: Seq[String], options: Seq[String])

object LastDream {

  implicit val questionFormat = Json.format[Question]

  def main(args: Array[String]): Unit = {
    val path = Paths.get(getClass.getResource("stories/story.json").toURI)
    val bytes = Files.readAllBytes(path)
    val questions = Json.parse(bytes).as[Seq[Question]].map { q => (q.id, q) }.toMap
    val start = questions("start")
    ask(start, questions).map(ask(_, questions))
  }

  def ask(question: Question, questions: Map[String, Question]): Option[Question] = {
    question.text.foreach(println)
    val options = question.options.map(questions)
    options.zipWithIndex.foreach { case (q, i) =>
        println(s"${i + 1}) ${q.prompt.mkString("\n")}")
    }
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


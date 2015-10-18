import java.awt.{Container, BorderLayout, Dimension}
import java.awt.event.{ActionEvent, ActionListener}
import java.nio.file.{Paths, Files}
import javax.swing._

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
    createAndShowGUI(questionsById)
    /*
    val start = questionsById("start")
    var next = ask(start, questionsById)
    while (next.nonEmpty) {
      next = ask(next.get, questionsById)
    }*/
  }

  def ask(question: Question, questionsById: Map[String, Question]): Option[Question] = {
    println()
    question.text.foreach(println)
    println()
    val options = question.options.map(questionsById)
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

  def createAndShowGUI(questionsById: Map[String, Question]): Unit = {
    val frame = new JFrame("Title")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    def setContent(pane: Container, question: Question): Unit = {
      val options = question.options.map(questionsById)
      val buttons = options.zipWithIndex.map { case (q, i) =>
        val button = new JButton(q.prompt.mkString("\n"))
        button.addActionListener(new ActionListener() {
          override def actionPerformed(e: ActionEvent): Unit = {
            val choice = options.toSeq(i)
            if (!choice.id.endsWith("_end")) {
              pane.removeAll()
              setContent(pane, choice)
            }
          }
        })
        button
      }

      val buttonPanel = new JPanel
      buttons.foreach(buttonPanel.add)

      val textArea = new JTextArea(question.text.mkString("\n"))
      textArea.setEditable(false)
      textArea.setLineWrap(true)
      textArea.setWrapStyleWord(true)
      textArea.setColumns(20)
      textArea.setRows(10)
      val scrollPane = new JScrollPane(textArea)
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)

      pane.add(scrollPane, BorderLayout.PAGE_START)
      pane.add(buttonPanel, BorderLayout.CENTER)

      val min = new Dimension(250, 300)
      val max = new Dimension(500, 300)

      frame.setMinimumSize(min)
      frame.setMaximumSize(max)
      frame.setSize(min)
      frame.setVisible(true)
    }

    val firstQuestion = questionsById("start")
    setContent(frame.getContentPane, firstQuestion)
  }

}


package Project

import java.util.Date
import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object IO_Utils {

  def showMainPrompt(): Unit = { print("\nNote manager (n), Task manager (t), Diary manager (d), Player manager (p) or Quit (q): ") }

  def showNotePrompt(): Unit = { print("\nCreate note -> (c) \nEdit note -> (e) \nOrganize notes by date -> (do) \nOrganize notes by title -> (to)" +
    " \nShow note manager -> (s) \nShow archived notes -> (sa) \nDelete note by ID -> (d) \nQuit -> (q) \nChoose your command:") }

  def showTaskPrompt(): Unit = { print("\nCreate task -> (c) \nEdit task -> (e) \nMark task done -> (m) \nOrganize tasks by date -> (do) \nOrganize tasks by title -> (to) \nShow task manager -> (s) " +
    "\nDelete task by ID -> (d) \nQuit -> (q) \nChoose your command:") }

  def showDiaryPrompt(): Unit = { print("\nCompare date with tasks date -> (s) \nShow today's tasks -> (t) \nQuit -> (q) \nChoose your command:") }

  def showPlayerPrompt(): Unit = {println("\nShow player status -> (s) \nQuit -> (q) \nChoose your command: ")}

  def showTitle(): Unit = { print("\nTitle: ") }

  def show(s: String): Unit = println(s)

  @tailrec
  def showList(lst: List[String]): Unit = {
    if (lst.nonEmpty) {
      println(lst.head)
      showList(lst.tail)
    }
  }

  def showContent(): Unit = { print("Content: " ) }

  def showID(): Unit = { print("ID: " ) }

  def showArquived(): Unit = { print("Archive note? (y/n): " ) }

  def showTaskDate(): Unit = { print("\nTask Date (dd/MM/yyyy HH:mm): ") }

  def showTaskTime(): Unit = { print("\nTime the task take (hours): ") }

  def showTaskID(): Unit = { print("\nTask ID: ") }

  def showTaskDone(): Unit = { print("\nMark task as done? (y/n): ") }

  def showDiaryDate(): Unit = { print("\nDate (dd/MM/yyyy): ") }

  def getUserInput(): String = readLine.trim

  def myToInt(s:String): Int = {
    if (Try(s.toInt).isSuccess) s.toInt else -1
  }

  // Usar se for necessário dar parse de texto para date
  def parseDate(s: String): Try[Date]= {
    val format = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
    val date = Try(format.parse(s))
    if(date.isFailure)
      println(s"Wrong data format, use dd/MM/yyyy HH:mm")
    date
  }

  def parseDiary(s: String): Try[Date]= {
    val format = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
    val date = Try(format.parse(s + " 00:00"))
    if(date.isFailure)
      println(s"Wrong data format, use dd/MM/yyyy")
    date
  }

  // Usar para ler respostas boolean da interface textual
  def myToBoolean(s: String): Boolean = {
    s match {
      case "y" => true
      case "n" => false
    }
  }

  //Diary
  def notifyIsDueToday(): Unit = print("There are Task(s) expiring today. Get to it!")

}
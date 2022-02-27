package Project

import java.io.{File, FileWriter}
import java.nio.file.{Files, Paths}
import java.util.Date
import java.text.SimpleDateFormat

import Project.TaskManager.{Task, Task_List}

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.io.Source

case class Task_DB(taskManager: TaskManager) {

  def save(): Unit = Task_DB.save(taskManager)

  def load(): TaskManager = Task_DB.load()

}

object Task_DB {

  def save(taskManager: TaskManager): Unit = {
    val f = new File("Tasks.txt")
    f.delete()

    @tailrec
    def aux(task_List: Task_List): Unit = {
      task_List match {
        case Nil => Nil
        case x :: xs => writeFile("Tasks.txt", x)
          aux(xs)
      }
    }

    aux(taskManager.task_list)
  }

  def writeFile(filename: String, task: Task): Unit = {
    val fw = new FileWriter(filename, true)
    val nt = (task._1, task._2, task._3.get, task._4.getTime, task._5, task._6)
    fw.write(nt.toString() + "\n")
    fw.close()
  }

  def load(): TaskManager = {
    if (!Files.exists(Paths.get("Tasks.txt"))) {
      new FileWriter(new File("Tasks.txt"))
    }
    val lines = Source.fromFile("Tasks.txt")
    val file_list = lines.getLines.toList
    lines.close()

    def aux(lst: List[String]): List[Task] = {
      lst match {
        case Nil => List()
        case x :: xs => stringToNote(x) :: aux(xs)
      }
    }

    TaskManager(aux(file_list))
  }

  def stringToNote(s: String): Task = {
    val str: Array[String] = s.split(",")
    val id: String = str(0).substring(1, str(0).length)
    val title: String = str(1)
    val content: String = str(2)
    val task_date: String = str(3)
    val task_time: String = str(4)
    val done: String = str(5).substring(0, str(5).length - 1)
    (id.toInt, title, Some(content), new Date(task_date.toLong), task_time.toInt, done.toBoolean)
  }

  def toInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case e: Exception => 0
    }
  }
}



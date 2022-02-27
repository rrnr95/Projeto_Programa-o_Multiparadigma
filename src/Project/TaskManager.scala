package Project

import java.util.Date

import Project.NoteManager.{Note, Note_list}
import Project.TaskManager._

import scala.annotation.tailrec

case class TaskManager(task_list: Task_List) {

  def createTask(title: String, content: String, taskDate: Date, task_time: String): TaskManager = TaskManager.createTask(task_list, title, content, taskDate, task_time)

  def searchTask(id: ID): Option[Task] = TaskManager.searchTask(task_list, id)

  def deleteTask(id: ID): TaskManager = TaskManager.deleteTask(task_list, id)

  def editTask(id: ID, title: String, content: String, taskDate: Date, task_time: String, taskDone: Boolean): TaskManager = TaskManager.editTask(task_list, id, title, content, taskDate, task_time, taskDone)

  def markTaskDone(id: ID, taskDone: Boolean): TaskManager = TaskManager.markTaskDone(task_list, id, taskDone)

  def organizeByDate(): TaskManager = TaskManager.organizeByDate(task_list)

  def organizeByTitle(): TaskManager = TaskManager.organizeByTitle(task_list)

  def showTasks(): String = TaskManager.showTasks(task_list)

}

object TaskManager {

  type ID = Int
  type Title = String
  type Content = Option[String]
  type Task_Date = Date
  type Task_Time = Int
  type Done = Boolean
  type Task = (ID, Title, Content, Task_Date, Task_Time, Done)
  type Task_List = List[Task]

  def createTask(lst: Task_List, title: String, content: String, taskDate: Date, task_time: String): TaskManager = {
    if (lst.isEmpty)
      TaskManager(List((1, title, Some(content), taskDate, task_time.toInt, false)))
    else
      TaskManager(lst ::: List((lst.last._1 + 1, title, Some(content), taskDate, task_time.toInt, false)))
  }

  @tailrec
  def searchTask(lst: Task_List, id: ID): Option[Task] =
    lst match {
      case Nil => None
      case List() => None
      case x :: xs => if (x._1 == id) Some(x) else searchTask(xs, id)
    }

  def editTask(lst: Task_List, id: ID, title: String, content: String, taskDate: Date, task_time: String, taskDone: Boolean): TaskManager = {
    val newTask = (id, title, Some(content), taskDate, task_time.toInt, taskDone)
    val index = lst.indexWhere(x => x._1 == id)
    TaskManager(lst.updated(index, newTask))
  }

  def showTasks(lst: Task_List): String = lst match {
    case Nil => ""
    case x :: xs =>
      "\nID: " + x._1 + "\nTitle: " + x._2 + "\nContent: " + x._3.get + "\nTask Date: " + x._4 + "\nTask Time: " +
        x._5 + "\nDone: " + x._6 + "\n" + showTasks(xs)
  }

  def deleteTask(lst: Task_List, id: ID): TaskManager = {
    TaskManager(lst.filterNot(t => t._1 == id))
  }

  def insertInOrder(f: (Task, Task) => Boolean, x: Task, lst: Task_List): Task_List = {
    lst match {
      case Nil => List(x)
      case y :: ys => if (f(x, y)) x :: y :: ys else y :: insertInOrder(f, x, ys)
    }
  }

  //organize by date (crescent order)
  def organizeByDate(lst: Task_List): TaskManager = TaskManager((lst foldRight List[Task]()) ((x, lst) => insertInOrder(orderByDate, x, lst)))

  //organize by title (crescent order)
  def organizeByTitle(lst: Task_List): TaskManager = TaskManager((lst foldRight List[Task]()) ((x, lst) => insertInOrder(orderByTitle, x, lst)))

  //Aux function for organizeByDate
  def orderByDate(x: Task, y: Task): Boolean = x._4.getTime < y._4.getTime

  //Aux function for organizeByTitle
  def orderByTitle(x: Task, y: Task): Boolean = x._2 < y._2

  def markTaskDone(lst: Task_List, id: ID, taskDone: Boolean): TaskManager = {
    val task = searchTask(lst, id).get
    val newTask = (id, task._2, task._3, task._4, task._5, taskDone)
    val index = lst.indexWhere(x => x._1 == id)
    TaskManager(lst.updated(index, newTask))
  }
}

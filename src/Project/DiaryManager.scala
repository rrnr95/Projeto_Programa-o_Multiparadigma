package Project

import java.util.{Calendar, Date}

import Project.TaskManager.{Task, Task_List}


case class DiaryManager(taskManager: TaskManager) {

  def tasksWithDate(taskDate: Date): List[Task] = DiaryManager.tasksWithDate(taskDate)(taskManager)

  def showList(taskDate: Date): List[String] = DiaryManager.showList(tasksWithDate(taskDate))

  def taskListForToday(): List[Task] = DiaryManager.taskListForToday(taskManager)

  def showTodayList(): List[String] = DiaryManager.showList(taskListForToday())
}

object DiaryManager {

  //returns a list of tasks with a determined date
  def tasksWithDate(taskDate: Date)(taskManager: TaskManager): List[Task] = {
    val cal1 = Calendar.getInstance
    val cal2 = Calendar.getInstance
    cal1.setTime(taskDate)

    def aux(task_list: Task_List): List[Task] = {
      task_list match {
        case Nil => List()
        case x :: xs => cal2.setTime(x._4)
          if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) x :: aux(xs) else aux(xs)
      }
    }

    aux(taskManager.task_list)
  }

  //returns a list of tasks with today's date
  def taskListForToday(taskManager: TaskManager): List[Task] = {
    val todayTasks = tasksWithDate(Calendar.getInstance.getTime) _
    todayTasks(taskManager)
  }

  //Shows the list of tasks
  def showList(lst: Task_List): List[String] = lst match {
    case Nil => List()
    case x :: xs =>
      lst.map(x => x._1 + " - " + x._2)
  }
}



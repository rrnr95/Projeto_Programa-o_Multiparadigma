package Project

import Project.TaskManager.Task

import java.util.Date
import scala.annotation.tailrec

object Main {

  def main(args: Array[String]): Unit = {
    val nm = Note_DB(NoteManager(List())).load()
    val tm = Task_DB(TaskManager(List())).load()
    mainLoop(nm, tm)

    //Main Loop
    @tailrec
    def mainLoop(nm: NoteManager, tm: TaskManager) {

      IO_Utils.showMainPrompt()
      val userInput = IO_Utils.getUserInput()

      userInput match {
        //Go to the Task menu
        case "t" => taskLoop(nm, tm)
        //Go to the Note menu
        case "n" => noteLoop(nm, tm)
        //Go to the Diary menu
        case "d" => diaryLoop(DiaryManager(tm), nm, tm)
        //Check player status
        case "p" => playerLoop(PlayerManager(tm, (50, 0.0, 1)), nm, tm)
        //exit from the main loop
        case "q" =>
          Note_DB(nm).save()
          Task_DB(tm).save()
          println("All saved. Exit sucessful.")
        //Wrong command Entered
        case _ =>
          println("\nCommand not found. Choose one of this: ")
          mainLoop(nm, tm)
      }
    }

    //Note menu
    @tailrec
    def noteLoop(nm: NoteManager, tm: TaskManager) {

      IO_Utils.showNotePrompt()
      val userInput = IO_Utils.getUserInput()

      // handle the user input
      userInput match {
        //create a new note
        case "c" =>
          IO_Utils.showTitle()
          val title = IO_Utils.getUserInput()
          IO_Utils.showContent()
          val content = IO_Utils.getUserInput()
          val newNoteManager = nm.createNote(title, content)
          noteLoop(newNoteManager, tm)
        //edit a note
        case "e" =>
          IO_Utils.showID()

          @tailrec
          def auxNote(): Int = {
            val id = IO_Utils.myToInt(IO_Utils.getUserInput())
            val note = nm.searchNote(id)
            if (note.isDefined) {
              id
            } else {
              IO_Utils.show("Note with this ID doesn't exist, try again: \n")
              auxNote()
            }
          }

          val id = auxNote()
          IO_Utils.showTitle()
          val title = IO_Utils.getUserInput()
          IO_Utils.showContent()
          val content = IO_Utils.getUserInput()
          IO_Utils.showArquived()
          val archived = IO_Utils.myToBoolean(IO_Utils.getUserInput())
          val newNoteManager = nm.editNote(id, title, content, archived)
          noteLoop(newNoteManager, tm)
        //delete a note
        case "d" =>
          IO_Utils.showID()

          @tailrec
          def auxNote(): Int = {
            val id = IO_Utils.myToInt(IO_Utils.getUserInput())
            val note = nm.searchNote(id)
            if (note.isDefined) {
              id
            } else {
              IO_Utils.show("Note with this ID doesn't exist, try again:")
              auxNote()
            }
          }

          val id = auxNote()
          val newNoteManager = nm.deleteNote(id)
          noteLoop(newNoteManager, tm)
        //show the list of notes
        case "s" =>
          IO_Utils.show(nm.showNoteManager().showNotes())
          noteLoop(nm, tm)
        //show the list of archived notes
        case "sa" =>
          IO_Utils.show(nm.showArchivedNotes().showNotes())
          noteLoop(nm, tm)
        //Organize by Date
        case "do" =>
          val newNoteManager = nm.organizeByDate()
          IO_Utils.show(newNoteManager.showNoteManager().showNotes())
          noteLoop(newNoteManager, tm)
        //Organize by Title
        case "to" =>
          val newNoteManager = nm.organizeByTitle()
          IO_Utils.show(newNoteManager.showNoteManager().showNotes())
          noteLoop(newNoteManager, tm)
        //exit from the note loop
        case "q" =>
          println("Exit from notes.")
          mainLoop(nm, tm)
        //Wrong command Entered
        case _ =>
          println("\nCommand not found. Choose one of this: ")
          noteLoop(nm, tm)
      }
    }

    //Task menu
    @tailrec
    def taskLoop(nm: NoteManager, tm: TaskManager) {

      IO_Utils.showTaskPrompt()
      val userInput = IO_Utils.getUserInput()

      // handle the result
      userInput match {
        //Create new task
        case "c" =>
          IO_Utils.showTitle()
          val title = IO_Utils.getUserInput()
          IO_Utils.showContent()
          val content = IO_Utils.getUserInput()
          IO_Utils.showTaskDate()

          @tailrec
          def aux(): Date = {
            val taskDate = IO_Utils.parseDate(IO_Utils.getUserInput())
            if (taskDate.isFailure)
              aux()
            else
              taskDate.get
          }

          val taskDate = aux()

          IO_Utils.showTaskTime()
          val task_time = IO_Utils.getUserInput()
          val newTaskManager = tm.createTask(title, content, taskDate, task_time)
          taskLoop(nm, newTaskManager)
        //Edit task by ID
        case "e" =>
          IO_Utils.showTaskID()

          @tailrec
          def auxTask(): Task = {
            val id = IO_Utils.myToInt(IO_Utils.getUserInput())
            val task = tm.searchTask(id)
            if (task.isDefined) {
              task.get
            } else {
              IO_Utils.show("Task with this ID doesn't exist, try again:")
              auxTask()
            }
          }

          val task = auxTask()
          IO_Utils.showTitle()
          val title = IO_Utils.getUserInput()
          IO_Utils.showContent()
          val content = IO_Utils.getUserInput()
          IO_Utils.showTaskDate()

          @tailrec
          def aux(): Date = {
            val taskDate = IO_Utils.parseDate(IO_Utils.getUserInput())
            if (taskDate.isFailure)
              aux()
            else
              taskDate.get
          }

          val taskDate = aux()
          IO_Utils.showTaskTime()
          val task_time = IO_Utils.getUserInput()
          IO_Utils.showTaskDone()
          val taskDone = IO_Utils.myToBoolean(IO_Utils.getUserInput())
          val newTaskManager = tm.editTask(task._1, if (title.isEmpty) task._2 else title, content, taskDate, task_time, taskDone)
          taskLoop(nm, newTaskManager)
        //Delete task by ID
        case "d" =>
          IO_Utils.showTaskID()

          @tailrec
          def auxTask(): Int = {
            val id = IO_Utils.myToInt(IO_Utils.getUserInput())
            val task = tm.searchTask(id)
            if (task.isDefined) {
              id
            } else {
              IO_Utils.show("Task with this ID doesn't exist, try again:")
              auxTask()
            }
          }

          val id = auxTask()
          val newTaskManager = tm.deleteTask(id)
          taskLoop(nm, newTaskManager)
        //Mark task as done/not done
        case "m" =>
          IO_Utils.showTaskID()

          @tailrec
          def auxTask(): Int = {
            val id = IO_Utils.myToInt(IO_Utils.getUserInput())
            val task = tm.searchTask(id)
            if (task.isDefined) {
              id
            } else {
              IO_Utils.show("Task with this ID doesn't exist, try again:")
              auxTask()
            }
          }

          val id = auxTask()
          IO_Utils.showTaskDone()
          val taskDone = IO_Utils.myToBoolean(IO_Utils.getUserInput())
          val newTaskManager = tm.markTaskDone(id, taskDone)
          taskLoop(nm, newTaskManager)
        //Organize by Date
        case "do" =>
          val newTaskManager = tm.organizeByDate()
          IO_Utils.show(newTaskManager.showTasks())
          taskLoop(nm, newTaskManager)
        //Organize by Title
        case "to" =>
          val newTaskManager = tm.organizeByTitle()
          IO_Utils.show(newTaskManager.showTasks())
          taskLoop(nm, newTaskManager)
        //Show task list
        case "s" =>
          IO_Utils.show(tm.showTasks())
          taskLoop(nm, tm)
        //exit from the task loop
        case "q" =>
          println("Exit from tasks")
          mainLoop(nm, tm)
        //Wrong command Entered
        case _ =>
          println("\nCommand not found. Choose one of this: ")
          taskLoop(nm, tm)
      }
    }

    //Diary menu
    @tailrec
    def diaryLoop(dm: DiaryManager, nm: NoteManager, tm: TaskManager) {

      IO_Utils.showDiaryPrompt()
      val userInput = IO_Utils.getUserInput()

      // handle the result
      userInput match {
        //compares the input date with the task list date
        case "s" =>
          IO_Utils.showDiaryDate()

          @tailrec
          def aux(): Date = {
            val taskDate = IO_Utils.parseDiary(IO_Utils.getUserInput())
            if (taskDate.isFailure)
              aux()
            else
              taskDate.get
          }

          val taskDate = aux()
          dm.tasksWithDate(taskDate)
          IO_Utils.showList(dm.showList(taskDate))
          diaryLoop(dm, nm, tm)
        //Check for tasks that start on the actual date
        case "t" =>
          IO_Utils.showList(dm.showTodayList())
          diaryLoop(dm, nm, tm)
        //exit from the task loop
        case "q" =>
          println("Exit from diary")
          mainLoop(nm, tm)
        //Wrong command Entered
        case _ =>
          println("\nCommand not found. Choose one of this: ")
          diaryLoop(dm, nm, tm)
      }
    }

    @tailrec
    def playerLoop(pm: PlayerManager, nm: NoteManager, tm: TaskManager) {

      IO_Utils.showPlayerPrompt()
      val userInput = IO_Utils.getUserInput()

      // handle the result
      userInput match {
        //Show player status
        case "s" =>
          val pm1 = pm.addExperience()
          val pm2 = pm1.takeHealthBar()
          IO_Utils.show(pm2.showPlayer())
          playerLoop(pm, nm, tm)
        //exit from the task loop
        case "q" =>
          println("Exit from player status")
          mainLoop(nm, tm)
        //Wrong command Entered
        case _ =>
          println("\nCommand not found. Choose one of this: ")
          playerLoop(pm, nm, tm)
      }
    }
  }
}
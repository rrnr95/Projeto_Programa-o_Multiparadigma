package Project

import java.util.Calendar

import Project.PlayerManager.Player
import Project.TaskManager.Task_List

import scala.annotation.tailrec

case class PlayerManager(taskManager: TaskManager, player: Player) {

  def addExperience(): PlayerManager = PlayerManager.addExperience(taskManager, player)

  def takeHealthBar(): PlayerManager = PlayerManager.takeHealthBar(taskManager, player)

  def showPlayer(): String = PlayerManager.showPlayer(player)
}

object PlayerManager {

  type HealthBar = Int
  type Experience = Double
  type Level = Int
  type Player = (HealthBar, Experience, Level)

  //Add experience for every task done, even if it wasnt done on time
  def addExperience(taskManager: TaskManager, player: Player): PlayerManager = {
    @tailrec
    def aux(lst: Task_List, player: Player): Player = {
      lst match {
        case Nil => player
        case x :: xs =>
          if (x._6) {
            aux(xs, (player._1, player._2 + (player._3 * 5.5), player._3))
          }
          else {
            aux(xs, player)
          }
      }
    }

    val p = aux(taskManager.task_list, player)
    if (p._2 >= p._3 * 15) {
      PlayerManager(taskManager, (50, p._2 - (p._3 * 15), p._3 + 1))
    }
    else {
      PlayerManager(taskManager, p)
    }
  }

  //Show player status
  def showPlayer(player: Player): String = {
    "\nPlayer HealthBar: " + player._1 + "\nPlayer Experience: " + player._2 + "\nExperience needed to get to the next level: " +
      ((player._3 * 15) - player._2) + "\nPlayer Level: " + player._3

  }

  //removes points of health if the player doesn't do tasks on time
  def takeHealthBar(taskManager: TaskManager, player: Player): PlayerManager = {
    val cal1 = Calendar.getInstance
    val cal2 = Calendar.getInstance

    @tailrec
    def aux(lst: Task_List, p: Player): Player = {
      lst match {
        case Nil => p
        case x :: xs => cal2.setTime(x._4)
          if (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)
            && cal1.get(Calendar.YEAR) >= cal2.get(Calendar.YEAR) && !x._6) {
            aux(xs, (p._1 - 5, p._2, p._3))

          }
          else {
            aux(xs, p)
          }
      }
    }

    val p = aux(taskManager.task_list, player)
    if (p._1 < 0) {
      PlayerManager(taskManager, (50, 0.0, if (p._3 > 1) p._3 - 1 else 1))
    } else {
      PlayerManager(taskManager, p)
    }
  }
}

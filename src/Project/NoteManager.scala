package Project

import java.util.Date

import Project.NoteManager.{Note_list, _}

import scala.annotation.tailrec

case class NoteManager(note_list: Note_list) {

  def createNote(title: String, content: String): NoteManager = NoteManager.createNote(note_list, title, content)

  def searchNote(id: ID): Option[Note] = NoteManager.searchNote(note_list, id)

  def editNote(id: ID, title: String, content: String, archived: Boolean): NoteManager = NoteManager.editNote(note_list, id, title, content, archived)

  def deleteNote(id: ID): NoteManager = NoteManager.deleteNote(note_list, id)

  def showNoteManager(): NoteManager = NoteManager.showNoteManager(note_list)

  def showArchivedNotes(): NoteManager = NoteManager.showArchivedNotes(note_list)

  def showNotes(): String = NoteManager.showNotes(note_list)

  def organizeByDate(): NoteManager = NoteManager.organizeByDate(note_list)

  def organizeByTitle(): NoteManager = NoteManager.organizeByTitle(note_list)

}

object NoteManager {

  //Atributes
  type ID = Int
  type Title = Option[String]
  type Content = Option[String]
  type Archived = Boolean
  type Edit_Date = Date
  type Note = (ID, Title, Content, Edit_Date, Archived)
  type Note_list = List[Note]

  //creates a new note
  def createNote(lst: Note_list, title: String, content: String): NoteManager = {
    if (lst.nonEmpty)
      NoteManager(lst ::: List((lst.last._1 + 1, Option(title), Option(content), new Date(), false)))
    else NoteManager(List((1, Option(title), Option(content), new Date(), false)))
  }

  @tailrec
  def searchNote(lst: Note_list, id: ID): Option[Note] = {
    lst match {
      case Nil => None
      case List() => None
      case x :: xs => if (x._1 == id) Some(x) else searchNote(xs, id)
    }
  }

  //edits a note by the id
  def editNote(lst: Note_list, id: ID, title: String, content: String, archived: Boolean): NoteManager = {
    def aux(lst: Note_list): Note_list = {
      lst match {
        case Nil => List()
        case x :: xs => if (x._1 == id) {
          (x._1, Option(if (title.isEmpty) x._2.get else title), Option(if (content.isEmpty) x._3.get else content), new Date(), archived) :: aux(xs)
        }
        else
          x :: aux(xs)
      }
    }

    NoteManager(aux(lst))
  }


  //deletes note by the id
  def deleteNote(lst: Note_list, id: ID): NoteManager = {
    def aux(lst: Note_list): Note_list = {
      lst match {
        case Nil => List()
        case x :: xs => if (x._1 == id) {
          aux(xs)
        }
        else
          x :: aux(xs)
      }
    }

    NoteManager(aux(lst))
  }

  //shows all notes we have (String)
  def showNoteManager(lst: Note_list): NoteManager = {
    def aux(lst1: Note_list): Note_list = {
      lst1 match {
        case Nil => List()
        case x :: xs =>
          if (!x._5) {
            x :: aux(xs)
          } else {
            aux(xs)
          }
      }
    }

    NoteManager(aux(lst))
  }

  //shows all archived notes we have
  def showArchivedNotes(lst: Note_list): NoteManager = {
    def aux(lst1: Note_list): Note_list = {
      lst1 match {
        case Nil => List()
        case x :: xs =>
          if (x._5) {
            x :: aux(xs)
          } else {
            aux(xs)
          }
      }
    }

    NoteManager(aux(lst))
  }

  def showNotes(lst: Note_list): String = {
    lst match {
      case Nil => ""
      case x :: xs =>
        "\nID: " + x._1 + "\nTitle: " + x._2.get + "\nContent: " + x._3.get + "\nDate: " + x._4 + "\n" + showNotes(xs)
    }
  }


  def insertInOrder(f: (Note, Note) => Boolean, x: Note, lst: Note_list): Note_list = {
    lst match {
      case Nil => List(x)
      case y :: ys => if (f(x, y)) x :: y :: ys else y :: insertInOrder(f, x, ys)
    }
  }

  //organize by date (crescent order)
  def organizeByDate(lst: Note_list): NoteManager = NoteManager((lst foldRight List[Note]()) ((x, lst) => insertInOrder(orderByDate, x, lst)))

  //organize by title (crescent order)
  def organizeByTitle(lst: Note_list): NoteManager = NoteManager((lst foldRight List[Note]()) ((x, lst) => insertInOrder(orderByTitle, x, lst)))

  //Aux function for organizeByDate
  def orderByDate(x: Note, y: Note): Boolean = x._4.getTime < y._4.getTime

  //Aux function for organizeByTitle
  def orderByTitle(x: Note, y: Note): Boolean = x._2.toString < y._2.toString
}


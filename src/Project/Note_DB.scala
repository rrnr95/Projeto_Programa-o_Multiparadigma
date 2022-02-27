package Project

import java.io.{File, FileWriter}
import java.nio.file.{Files, Paths}
import java.util.Date
import java.text.SimpleDateFormat

import Project.NoteManager.{Note, Note_list}

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.io.Source


case class Note_DB(noteManager: NoteManager) {

  def save(): Unit = Note_DB.save(noteManager)

  def load(): NoteManager = Note_DB.load()


}

object Note_DB {

  def save(noteManager: NoteManager): Unit = {
    val f = new File("Notes.txt")
    f.delete()

    @tailrec
    def aux(note_list: Note_list): Unit = {
      note_list match {
        case Nil => Nil
        case x :: xs => writeFile("Notes.txt", x)
          aux(xs)
      }
    }

    aux(noteManager.note_list)
  }

  def writeFile(filename: String, note: Note): Unit = {
    val fw = new FileWriter(filename, true)
    val nn = (note._1, note._2.get, note._3.get, note._4.getTime, note._5)
    fw.write(nn.toString() + "\n")
    fw.close()
  }

  def load(): NoteManager = {
    if (!Files.exists(Paths.get("Notes.txt"))) {
      new FileWriter(new File("Notes.txt"))
    }
    val lines = Source.fromFile("Notes.txt")
    val file_list = lines.getLines.toList
    lines.close()

    def aux(lst: List[String]): List[Note] = {
      lst match {
        case Nil => List()
        case x :: xs => stringToNote(x) :: aux(xs)
      }
    }

    NoteManager(aux(file_list))
  }

  def stringToNote(s: String): Note = {
    val st: Array[String] = s.split(",")
    val id: String = st(0).substring(1, st(0).length)
    val title: String = st(1)
    val content: String = st(2)
    val date: String = st(3)
    val arch: String = st(4).substring(0, st(4).length - 1)
    (id.toInt, Some(title), Some(content), new Date(date.toLong), arch.toBoolean)
  }

}


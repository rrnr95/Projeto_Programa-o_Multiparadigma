package Project

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import Project.FxApp.tm
import Project.NoteManager.Note
import Project.TaskManager.{Task, Title}
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, DatePicker, Label, ListView, ProgressBar, TextArea, TextField}
import javafx.scene.layout.Pane

import java.time.LocalDate
import java.util
import scala.jdk.CollectionConverters._

class Controller {

  //Main Menu screen Attributes
  @FXML
  private var main_pane: Pane = _
  @FXML
  private var main_list_show: ListView[String] = _
  @FXML
  private var main_health: ProgressBar = _
  @FXML
  private var main_experience: ProgressBar = _
  @FXML
  private var main_level: Label = _

  @FXML
  def initialize(): Unit = {
    mainScreen()
  }

  def mainScreen(): Unit = {
    cancelDiary()
    cancelNote()
    cancelTask()
    main_list_show.getItems.clear()
    showMainScreen()
  }

  def showMainScreen(): Unit = {
    main_list_show.getItems.clear()
    listShowTodayMain()
    FxApp.pm = PlayerManager(tm, (50, 0.0, 1)).takeHealthBar().addExperience()
    main_health.setProgress(FxApp.pm.player._1 / 50.0)
    main_experience.setProgress(FxApp.pm.player._2 / (FxApp.pm.player._3 * 15))
    main_level.setText(FxApp.pm.player._3.toString)
    main_pane.setVisible(true)
  }


  def cancelMain(): Unit = {
    main_list_show.getItems.clear()
    main_pane.setVisible(false)
  }

  def clearAll(): Unit = {
    cancelNote()
    cancelTask()
    cancelDiary()
    cancelMain()
  }

  //show today's task list
  def listShowTodayMain(): Unit = {
    val lst = DiaryManager(FxApp.tm).taskListForToday()
    main_list_show.getItems.addAll(taskListToStringList(lst))
  }


  //-------------------------------------------------------------------------//

  //Tasks Attributes
  //Tasks menu
  @FXML
  private var tasks_pane: Pane = _

  //createTask
  @FXML
  private var task_create_pane: Pane = _
  @FXML
  private var task_create_title: TextField = _
  @FXML
  private var task_create_content: TextArea = _
  @FXML
  private var task_create_date: DatePicker = _
  @FXML
  private var task_create_hour: TextField = _
  @FXML
  private var task_create_time: TextField = _

  //editTask
  @FXML
  private var task_edit_pane: Pane = _
  @FXML
  private var task_edit_show: ListView[String] = _
  @FXML
  private var task_edit_title: TextField = _
  @FXML
  private var task_edit_content: TextArea = _
  @FXML
  private var task_edit_date: DatePicker = _
  @FXML
  private var task_edit_hour: TextField = _
  @FXML
  private var task_edit_time: TextField = _
  @FXML
  private var task_edit_done: CheckBox = _

  //deleteTask
  @FXML
  private var task_delete_pane: Pane = _
  @FXML
  private var task_delete_show: ListView[String] = _

  //Organize by Title
  @FXML
  private var task_TO_pane: Pane = _
  @FXML
  private var task_TO_list: ListView[String] = _


  //Organize by Date
  @FXML
  private var task_DO_pane: Pane = _
  @FXML
  private var task_DO_list: ListView[String] = _

  //showTasks
  @FXML
  private var task_show_pane: Pane = _
  @FXML
  private var task_show_list: ListView[String] = _

  //show Tasks Done
  @FXML
  private var task_show_done_pane: Pane = _
  @FXML
  private var task_show_done_list: ListView[String] = _

  //TASKMANAGER METHODS


  //createTask
  def menuCreateTask(): Unit = {
    clearAll()
    tasks_pane.setVisible(true)
    task_create_pane.setVisible(true)
  }

  //editTask
  def menuEditTask(): Unit = {
    clearAll()
    listEditTask()
    tasks_pane.setVisible(true)
    task_edit_pane.setVisible(true)
  }

  //deleteTask
  def menuDeleteTask(): Unit = {
    clearAll()
    listDeleteTask()
    tasks_pane.setVisible(true)
    task_delete_pane.setVisible(true)
  }

  //Organize by Title
  def menuTOTask(): Unit = {
    clearAll()
    listTOTask()
    tasks_pane.setVisible(true)
    task_TO_pane.setVisible(true)
  }

  //Organize by Date
  def menuDOTask(): Unit = {
    clearAll()
    listDOTask()
    tasks_pane.setVisible(true)
    task_DO_pane.setVisible(true)
  }

  //showTasks
  def menuShowTask(): Unit = {
    clearAll()
    listShowTask()
    tasks_pane.setVisible(true)
    task_show_pane.setVisible(true)
  }

  //showTasksDone
  def menuShowTasksDone(): Unit = {
    clearAll()
    listShowTasksDone()
    tasks_pane.setVisible(true)
    task_show_done_pane.setVisible(true)
  }


  //on Panes

  //cancel Button
  def cancelTask(): Unit = {
    //clear create pane
    task_create_title.clear()
    task_create_content.clear()
    task_create_time.clear()
    //clear edit pane
    task_edit_show.getItems.clear()
    task_edit_title.clear()
    task_edit_content.clear()
    task_edit_date.setValue(null)
    task_edit_hour.clear()
    task_edit_time.clear()
    task_edit_done.setSelected(false)
    //clear delete pane
    task_delete_show.getItems.clear()
    //clear organizer lists
    task_TO_list.getItems.clear()
    task_DO_list.getItems.clear()
    //clear show pane
    task_show_list.getItems.clear()
    //clear show done pane
    task_show_done_list.getItems.clear()
    //go to homepage
    task_create_pane.setVisible(false)
    task_edit_pane.setVisible(false)
    task_delete_pane.setVisible(false)
    task_TO_pane.setVisible(false)
    task_DO_pane.setVisible(false)
    task_show_pane.setVisible(false)
    tasks_pane.setVisible(false)
    task_show_done_pane.setVisible(false)
    showMainScreen()
  }

  //createTask save
  val originFormat = new SimpleDateFormat("yyyy-MM-dd")
  val targetFormat = new SimpleDateFormat("dd/MM/yyyy")

  def saveCreateTask(): Unit = {
    val d = targetFormat.format(originFormat.parse(task_create_date.getValue.toString))
    val date = IO_Utils.parseDate(d + " " + task_create_hour.getText()).get
    val task = FxApp.tm.createTask(task_create_title.getText(), task_create_content.getText(),
      date, task_create_time.getText)
    FxApp.tm = task
    cancelTask()
  }

  def saveEditTask(): Unit = {
    val id = getIDEditTask
    val ut = FxApp.tm.searchTask(id).get

    def auxDate(): Date = {
      if (task_edit_hour.getText().isEmpty) {
        ut._4
      } else {
        val d = targetFormat.format(originFormat.parse(task_edit_date.getValue.toString))
        IO_Utils.parseDate(d + " " + task_edit_hour.getText()).get
      }
    }

    val date = auxDate()
    val task = FxApp.tm.editTask(id, if (task_edit_title.getText().isEmpty) ut._2 else task_edit_title.getText(), if (task_edit_content.getText().isEmpty) ut._2 else task_edit_content.getText(),
      date, if (task_edit_time.getText().isEmpty) ut._5.toString else task_edit_time.getText(), task_edit_done.isSelected)
    FxApp.tm = task
    cancelTask()
  }

  def onEditTaskListPreview(): Unit = {
    val id = getIDEditTask
    val ut = FxApp.tm.searchTask(id).get
    task_edit_title.setText(ut._2)
    task_edit_content.setText(ut._3.get)
    val calendar = Calendar.getInstance()
    calendar.setTime(ut._4)
    // add +1 to month because they start at 0 in Calendar
    task_edit_date.setValue(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)))
    task_edit_hour.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE))
    task_edit_time.setText(ut._5.toString)
    task_edit_done.setSelected(ut._6)
  }

  //deleteNote save
  def saveDeleteTask(): Unit = {
    val id: Int = getIDDeleteTask
    val task = FxApp.tm.deleteTask(id)
    FxApp.tm = task
    cancelTask()
  }

  //Auxiliar methods

  //show note list on editTask
  def listEditTask(): Unit = {
    val lst = FxApp.tm.task_list
    task_edit_show.getItems.addAll(taskListToStringList(lst))
  }

  //show task list on deleteTask
  def listDeleteTask(): Unit = {
    val lst = FxApp.tm.task_list
    task_delete_show.getItems.addAll(taskListToStringList(lst))
  }

  //show task list organized by Title
  def listTOTask(): Unit = {
    val lst = FxApp.tm.organizeByTitle().task_list
    task_TO_list.getItems.addAll(taskListToStringList(lst))
  }

  //show task list organized by Date
  def listDOTask(): Unit = {
    val lst = FxApp.tm.organizeByDate().task_list
    task_DO_list.getItems.addAll(taskListToStringList(lst))
  }

  def listShowTask(): Unit = {
    val lst = FxApp.tm.task_list
    task_show_list.getItems.addAll(taskListToStringList(lst))
  }

  def listShowTasksDone(): Unit = {
    val lst = FxApp.tm.task_list.filter(x => x._6)
    task_show_done_list.getItems.addAll(taskListToStringList(lst))
  }

  //convert task list to a string list for display
  def taskListToStringList(lst: List[Task]): util.List[String] = {
    lst.map(x=>"ID: " + x._1 +
      "\nTitle: " + x._2 + "\nContent: " + x._3.get +
      "\nTask date: " + x._4 + "\nTask time: " + x._5 + "h" + "\nDone: " + x._6).toSeq.asJava
  }

  //get the note ID (Edit)
  def getIDEditTask: Int = {
    val id = IO_Utils.myToInt(task_edit_show.getItems.get(task_edit_show.getSelectionModel.getSelectedIndex).charAt(4).toString)
    id
  }

  //get the note ID (Edit)
  def getIDDeleteTask: Int = {
    val id = IO_Utils.myToInt(task_delete_show.getItems.get(task_delete_show.getSelectionModel.getSelectedIndex).charAt(4).toString)
    id
  }


  //------------------------------------------------------------------------------------------------------------------//

  //Notes Attributes

  //Note menu
  @FXML
  private var note_pane: Pane = _

  //createNote
  @FXML
  private var note_create_pane: Pane = _
  @FXML
  private var note_create_title: TextField = _
  @FXML
  private var note_create_content: TextArea = _

  //editNote
  @FXML
  private var note_edit_pane: Pane = _
  @FXML
  private var note_edit_show: ListView[String] = _
  @FXML
  private var note_edit_title: TextField = _
  @FXML
  private var note_edit_content: TextArea = _
  @FXML
  private var note_edit_archived: CheckBox = _

  //deleteNote
  @FXML
  private var note_delete_pane: Pane = _
  @FXML
  private var note_delete_show: ListView[String] = _

  //Organize by Title
  @FXML
  private var note_TO_pane: Pane = _
  @FXML
  private var note_TO_list: ListView[String] = _


  //Organize by Date
  @FXML
  private var note_DO_pane: Pane = _
  @FXML
  private var note_DO_list: ListView[String] = _

  //showNote
  @FXML
  private var note_show_pane: Pane = _
  @FXML
  private var note_show_list: ListView[String] = _

  //showArchivedNotes
  @FXML
  private var note_archived_pane: Pane = _
  @FXML
  private var note_archived_list: ListView[String] = _


  //NOTEMANAGER METHODS

  //onMenu

  //createNote
  def menuCreateNote(): Unit = {
    clearAll()
    note_pane.setVisible(true)
    note_create_pane.setVisible(true)
  }

  //editNote
  def menuEditNote(): Unit = {
    clearAll()
    listEditNote()
    note_pane.setVisible(true)
    note_edit_pane.setVisible(true)
  }

  //deleteNote
  def menuDeleteNote(): Unit = {
    clearAll()
    listDeleteNote()
    note_pane.setVisible(true)
    note_delete_pane.setVisible(true)
  }

  //showNote
  def menuShowNote(): Unit = {
    clearAll()
    listShowNote()
    note_pane.setVisible(true)
    note_show_pane.setVisible(true)
  }

  //showNote
  def menuArchivedNote(): Unit = {
    clearAll()
    listArchivedNote()
    note_pane.setVisible(true)
    note_archived_pane.setVisible(true)
  }

  //Organize by Title
  def menuTONote(): Unit = {
    clearAll()
    listTONote()
    note_pane.setVisible(true)
    note_TO_pane.setVisible(true)
  }

  //Organize by Date
  def menuDONote(): Unit = {
    clearAll()
    listDONote()
    note_pane.setVisible(true)
    note_DO_pane.setVisible(true)
  }


  //on Panes

  //cancel Button
  def cancelNote(): Unit = {
    //clear create pane
    note_create_title.clear()
    note_create_content.clear()
    //clear edit pane
    note_edit_show.getItems.clear()
    note_edit_title.clear()
    note_edit_content.clear()
    note_edit_archived.setSelected(false)
    //clear delete pane
    note_delete_show.getItems.clear()
    //clear TO pane
    note_TO_list.getItems.clear()
    //clear DO pane
    note_DO_list.getItems.clear()
    //clear archived list
    note_archived_list.getItems.clear()
    //clear show pane
    note_show_list.getItems.clear()
    //go back to home pane
    note_create_pane.setVisible(false)
    note_edit_pane.setVisible(false)
    note_delete_pane.setVisible(false)
    note_TO_pane.setVisible(false)
    note_DO_pane.setVisible(false)
    note_archived_pane.setVisible(false)
    note_show_pane.setVisible(false)
    note_pane.setVisible(false)
    showMainScreen()
  }

  //createNote save
  def saveCreateNote(): Unit = {
    val note = FxApp.nm.createNote(note_create_title.getText(), note_create_content.getText())
    FxApp.nm = note
    cancelNote()
  }

  //editNote save
  def saveEditNote(): Unit = {
    val id: Int = getIDEditNote
    val note = FxApp.nm.editNote(id, note_edit_title.getText(), note_edit_content.getText(), note_edit_archived.isSelected)
    FxApp.nm = note
    cancelNote()
  }

  //deleteNote save
  def saveDeleteNote(): Unit = {
    val id: Int = getIDDeleteNote
    val note = FxApp.nm.deleteNote(id)
    FxApp.nm = note
    cancelNote()
  }

  //Auxiliar methods

  //show note list on editNote
  def listEditNote(): Unit = {
    val lst = FxApp.nm.note_list
    note_edit_show.getItems.addAll(noteListToStringList(lst))
  }

  def onEditNoteListPreview(): Unit = {
    val id = getIDEditNote
    val note = FxApp.nm.searchNote(id).get
    note_edit_title.setText(note._2.get)
    note_edit_content.setText(note._3.get)
    note_edit_archived.setSelected(note._5)
  }

  //show note list on deleteNote
  def listDeleteNote(): Unit = {
    val lst = FxApp.nm.note_list
    note_delete_show.getItems.addAll(noteListToStringList(lst))
  }

  //show note list organized by Title
  def listTONote(): Unit = {
    val lst = FxApp.nm.showNoteManager().organizeByTitle().note_list
    note_TO_list.getItems.addAll(noteListToStringList(lst))
  }

  //show note list organized by Date
  def listDONote(): Unit = {
    val lst = FxApp.nm.showNoteManager().organizeByDate().note_list
    note_DO_list.getItems.addAll(noteListToStringList(lst))
  }

  //show note list
  def listShowNote(): Unit = {
    val lst = FxApp.nm.showNoteManager().note_list
    note_show_list.getItems.addAll(noteListToStringList(lst))
  }

  def listArchivedNote(): Unit = {
    val lst = FxApp.nm.showArchivedNotes().note_list
    note_archived_list.getItems.addAll(noteListToStringList(lst))
  }

  //convert task list to a string list for display
  def noteListToStringList(lst: List[Note]): util.List[String] = {
    lst.map(x=>"ID: " + x._1 +
      "\nTitle: " + x._2.get + "\nContent: " + x._3.get +
      "\nDate: " + x._4 + "\nArchived: " + x._5).toSeq.asJava
  }

  //get the note ID (Edit)
  def getIDEditNote: Int = {
    val id = IO_Utils.myToInt(note_edit_show.getItems.get(note_edit_show.getSelectionModel.getSelectedIndex).charAt(4).toString)
    id
  }

  //get the note ID (Delete)
  def getIDDeleteNote: Int = {
    val id = IO_Utils.myToInt(note_delete_show.getItems.get(note_delete_show.getSelectionModel.getSelectedIndex).charAt(4).toString)
    id
  }

  //-------------------------------------------------------------------------------------------------------------------//

  //Diary Attributes

  //Diary menu
  @FXML
  private var diary_pane: Pane = _

  //Show today's tasks
  @FXML
  private var diary_today_pane: Pane = _
  @FXML
  private var diary_today_list: ListView[String] = _

  //Show scheduled tasks
  @FXML
  private var diary_scheduled_pane: Pane = _
  @FXML
  private var diary_scheduled_list: ListView[String] = _
  @FXML
  private var diary_scheduled_date: DatePicker = _


  //NOTEMANAGER METHODS

  //onMenu

  //showTodayTasks
  def menuShowTodayDiary(): Unit = {
    clearAll()
    listShowTodayDiary()
    diary_pane.setVisible(true)
    diary_today_pane.setVisible(true)
  }

  //showScheduledTasks
  def menuShowScheduledDiary(): Unit = {
    clearAll()
    diary_pane.setVisible(true)
    diary_scheduled_pane.setVisible(true)
  }

  //on Panes

  //cancel Diary
  def cancelDiary(): Unit = {
    //clear today's diary
    diary_today_list.getItems.clear()
    //clear scheduled diary
    diary_scheduled_date.setValue(null)
    diary_scheduled_list.getItems.clear()
    //go back to home page
    diary_today_pane.setVisible(false)
    diary_scheduled_pane.setVisible(false)
    diary_pane.setVisible(false)
    showMainScreen()
  }

  def tasksWithDate(): Unit = {
    diary_scheduled_list.getItems.clear()
    val d = targetFormat.format(originFormat.parse(diary_scheduled_date.getValue.toString))
    val date = IO_Utils.parseDiary(d).get
    listShowScheduledDiary(date)
  }


  //Auxiliar methods

  //show today's task list
  def listShowTodayDiary(): Unit = {
    val lst = DiaryManager(FxApp.tm).taskListForToday()
    diary_today_list.getItems.addAll(taskListToStringList(lst))
  }

  def listShowScheduledDiary(taskDate: Date): Unit = {
    val lst = DiaryManager(FxApp.tm).tasksWithDate(taskDate)
    diary_scheduled_list.getItems.addAll(taskListToStringList(lst))
  }

}

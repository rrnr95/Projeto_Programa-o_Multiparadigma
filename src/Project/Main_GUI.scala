package Project

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class Main_GUI extends Application {

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Productivity App")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.setMinWidth(590)
    primaryStage.setMinHeight(400)
    primaryStage.show()
  }
}

object FxApp {
  var nm: NoteManager = Note_DB(NoteManager(List())).load()
  var tm: TaskManager = Task_DB(TaskManager(List())).load()
  var pm: PlayerManager = PlayerManager(tm, (50, 0.0, 1))

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main_GUI], args: _*)
    Note_DB(nm).save()
    Task_DB(tm).save()
  }


}


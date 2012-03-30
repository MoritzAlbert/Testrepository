package sq_gui

import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import TabbedPane._
import java.awt.{Dimension, Color}
import javax.swing.UIManager

//Begin object Object Gui

object Gui extends SimpleSwingApplication with XML with Functions with UpdateFunctions{

  //Begin MainFrame

  def top = new MainFrame() {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

    override def closeOperation() {
      exportToXML(database, "test.xml")
      System.exit(0)
    }

    title = "Gui Explorer"
    visible = true

    // New FlowPanel
    var frame = new FlowPanel()

    //Add data to datapool
    val addData = Action("Hinzufügen") {

      val fileChooser = new FileChooser() {
        fileFilter = new FileNameExtensionFilter("JPG, PDF & MP4", "jpg", "pdf", "mp4")
      }

      // EXEPTION ABFANGEN WENN FENSTER OHNE AUSWAHL GESCHLOSSEN WIRD. JR
      fileChooser.showOpenDialog(frame)
      val file = fileChooser.selectedFile
      //println(file)

      val url = file.toURI.toURL
      //println(url)

      //database.addToDataPool(url.getQuery)
      database.addToDataPool(url.getFile)

      println(url.getFile)
      //TODO Probleme bei URL to STRING, hier wird C: als Pfad ausgegeben. Linux Like


      //var stringtest = url.getPath

      //stringtest = stringtest.split("/", 1)


      updateListData(list, database)
      updateListImage(list_image, database)
      updateListDocument(list_doc, database)
      updateListVideo(list_video, database)

      exportToXML(database, "test.xml")
    }

    val addGroup = Action("add group") {
      /*
        val x = Dialog.showConfirmation(null,"Wo ist das Schätzchen!?!?","Question", Dialog.Options.YesNo, Dialog.Message.Question)
        println(x)
        println {
          x.toString
        }
      */
      val panel = new BoxPanel(Orientation.Vertical) {
        val groupname = new TextField(" ")
        contents += groupname
      }
      Dialog.showMessage(null, panel.peer, "Enter group name", Dialog.Message.Plain)

      println(panel.groupname.text)
      println(database.grouppool.size)
      database.addToGrouppool(panel.groupname.text)
      println(database.grouppool.size)
      updateListGroup(list_group, database)
    }

    //remove data JR
    val remData = Action("Löschen") {
      //todo Ausgewaehltes element erfragen, und dieses aus dem Datenbestand herausloeschen
     list.getSelectedIndex
      println("removebutton")
    }

    val searchData = Action("Suchen...") {
      println("search")
    }
    //add-Button
    var add = new Button {
      action = addData
    }

    var add_group = new Button {
      action = addGroup
    }

    //remove button JR
    var rem = new Button {
      action = remData
    }

    var search = new Button() {
      action = searchData
    }

    var searchInput = new TextField(" ") {

    }

    //filter tabs
    var tab_filter = new TabbedPane {
      pages += new Page("Alle", scroll)
      pages += new Page("Images", scroll_image)
      pages += new Page("Docs", scroll_doc)
      pages += new Page("Vids", scroll_video)
    }
    //filter Pane
    var tab = new ScrollPane(tab_filter)

    // functionPanel
    var functionPanel = new FlowPanel() {
      background = Color.BLACK
      //        FlowLayout.LEFT
      //TODO button possition setzen auf linksbuendig
      contents += add
      contents += rem

      //TODO RATING buttons einfügen siehe unten
      //contents = += rating1
    }


    //searchPanel
    var searchPanel = new FlowPanel {
      contents += searchInput
      contents += search

      //TODO Breite des Suchfelds anpassen. JR
    }

    //right-aligned Panel (containing three components)
    var box_right = new BoxPanel(Orientation.Vertical) {

      var boxSize = java.awt.Toolkit.getDefaultToolkit.getScreenSize

      val screenH = boxSize.getHeight.toInt
      val screenW = 200

      this.maximumSize = new Dimension(screenW, screenH)
      this.minimumSize = new Dimension(screenW, screenH)
      this.preferredSize = new Dimension(screenW, screenH)

      contents += functionPanel
      contents += searchPanel
      contents += tab
    }

    //panel for presentation all available groups
    var group = paintGridPanel

    //group scrollPane
    var group_new = new ScrollPane(group)


    // Grouppanel
    var groupPanel = new BoxPanel(Orientation.Vertical) {

      background = Color.RED

      // 2 buttons  auslagern, damit mit funktionen belegbar

      contents += new Button("+")
      contents += new Button("-")

    }


    //left-aligned Panel (containing two components)
    var box_left = new BoxPanel(Orientation.Vertical) {
      // this.preferredSize = new Dimension(1200, 600)
      contents += group_new
      contents += add_group
    }

    //complete window (containing left_box and right_box)
    val main = new BoxPanel(Orientation.Horizontal) {
      contents += box_left
      contents += box_right
      contents += groupPanel

    }
    contents = main

    //window maximized
    this.maximize()

  }
}
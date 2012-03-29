package sq_gui

import scala.swing._
import javax.swing.JList
import com.ebenius._
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.UIManager
import javax.swing.DropMode
import TabbedPane._
import java.awt.{Dimension, Color}


//Begin object Object Gui

object Gui extends SimpleSwingApplication with UpdateFunctions with XML with Functions{

  //Begin MainFrame

  def top = new MainFrame() {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

    override def closeOperation() {
      exportToXML(database, "test.xml")
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
      //database.addToDataPool(url.getPath)

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

    val searchData = Action("Suchen...") {
      println("search")
    }
    //add-Button
    var add = new Button {
      this.preferredSize = new Dimension(200,150)
      action = addData
    }

    var add_group = new Button {
      action = addGroup
    }

    var search = new Button() {
      action = searchData
    }

    var searchInput = new TextField(" ") {
       this.preferredSize = new Dimension(100,20)
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
    //  contents += rem
        this.maximumSize = new Dimension(200,150)
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
    }
    contents = main

    //window maximized
    this.maximize()

  }


  //Begin methods

  //paint gridPanel
  def paintGridPanel: GridPanel = {
    val size = database.grouppool.size
    val grid = new GridPanel(size, 1){
    val it = database.grouppool.iterator
    var frame = new FlowPanel()
    val playButton = Action("Play"){}
    val hinzuButton = Action("+"){
       new
    }
//      val fileChooser = new FileChooser() {
//        fileFilter = new FileNameExtensionFilter("JPG, PDF & MP4", "jpg", "pdf", "mp4")
//    }
//      fileChooser.showOpenDialog(frame)
//      val file = fileChooser.selectedFile
//    }
    val klappButton = Action("v"){}

      //GRUPPEN GENERIEREN
      while (it.hasNext) {
        val obj = it.next()
        val list = getJListFromGroup(obj)
        list.setDragEnabled(true)
        list.setDropMode(DropMode.INSERT)
        list.setTransferHandler(new ListMoveTransferHandler())

        list.setVisibleRowCount(1) //METRO STYLE bei 2, ansonsten 1
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP)

        // THUMBNAILS
        var s_list = new ScrollPane(Component.wrap(list))

        // TEXT
        var bp = new BoxPanel(Orientation.Vertical) {
          contents += new Label(obj.name)
          contents += new Label(obj.data.size + " Elements")
          contents += new Button {
            action = playButton
          }
          //border = Swing.EmptyBorder(30, 30, 10, 30)
        }

        // Buttonpanel für Gruppe
        var buttonPanel = new BoxPanel(Orientation.Vertical) {
          // 2 buttons  auslagern, damit mit funktionen belegbar
          contents += new Button {
            action = hinzuButton
          }
          //TODO ersetzen durch Icon Pfeil nach unten
          contents += new Button {
            action = klappButton
        }
        }

        val fp = new FlowPanel() {
          border = Swing.EmptyBorder(30, 30, 10, 30)
          contents += bp
          contents += s_list
          contents += buttonPanel
        }

        //Grouppanel (fp) mit buttonPanel integrieren
//        val integratedGroupPanel = new FlowPanel() {
//          contents += fp
//          contents += buttonPanel
//        }

        // Abstände Bildergalerien
        //fp.hGap = 50
        //fp.vGap = 50

        contents += fp
      }
    }
    grid
  }

}
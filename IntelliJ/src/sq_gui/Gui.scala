package sq_gui

import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import TabbedPane._
import java.awt.{Dimension, Color}
import dragndrop.MyTransferHandler
import javax.swing.{DropMode, JList, ImageIcon, UIManager}
import javax.swing.border.EtchedBorder


//Begin object Object Gui

object Gui extends SimpleSwingApplication with UpdateFunctions with XML with Functions with Search {

  var petrolHEX = new Color(0x116856)

  //Begin MainFrame

  def top = new MainFrame() {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

    override def closeOperation() {
      exportToXML(database, "test.xml")
      System.exit(0);

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
      database.addToDataPool(url.getPath)

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

    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem("Settings")
        contents += new MenuItem(Action("Exit") {
          closeOperation()
        })
      }

      contents += new Menu("Help") {
        contents += new MenuItem("Onlinehelp")
      }
    }


    val addGroup = Action("add group") {

      val panel = new BoxPanel(Orientation.Vertical) {
        val groupname = new TextField("")
        contents += groupname
      }

      Dialog.showMessage(null, panel.peer, "Enter group name", Dialog.Message.Plain)

      println(panel.groupname.text)
      println(database.grouppool.size)
      database.addToGrouppool(panel.groupname.text)
      println(database.grouppool.size)
      updateListGroup(list_group, database)
    }

    val searchData = Action("") {
      startSearch(searchInput.text)
      updateSearchListData(list, database)
      searchInput.text = ""
      updateSearchListData(searchList, database)
    }

    //add-Button
    var add = new Button {
      action = addData
    }

    var name_group = new TextField() {
      this.preferredSize = new Dimension(200, 25)
      this.maximumSize = new Dimension(200, 25)
      this.minimumSize = new Dimension(200, 25)
    }

    var add_group = new Button {
      action = addGroup
    }

    var search = new Button() {
      action = searchData
      this.icon = new ImageIcon("icons\\16x16\\search.png")
      this.preferredSize = new Dimension(40, 25)
    }

    var searchInput = new TextField("") {
       this.preferredSize = new Dimension(214,25)
    }

    //filter tabs
    var tab_filter = new TabbedPane {
      pages += new Page("All", scroll)
      pages += new Page("Images", scroll_image)
      pages += new Page("Docs", scroll_doc)
      pages += new Page("Vids", scroll_video)
    }
    //filter Pane
    var tab = new ScrollPane(tab_filter){
      this.preferredSize = new Dimension(270,500)
    }

    // functionPanel
    var functionPanel = new FlowPanel() {
      this.preferredSize = new Dimension(270,140)
      // background = petrolHEX
      //        FlowLayout.LEFT
      //TODO button position setzen auf linksbuendig
      contents += add
      //TODO RATING buttons einfügen siehe unten
    }

    //searchPanel
    var searchPanel = new FlowPanel {
      this.preferredSize = new Dimension(270, 30)
      contents += searchInput
      contents += search
    }

    var integratedPanel = new BoxPanel(Orientation.Vertical){
      contents += searchPanel
      contents += tab
    }

    //right-aligned Panel (containing three components)
    var box_right = new BoxPanel(Orientation.Vertical) {

      var boxSize = java.awt.Toolkit.getDefaultToolkit.getScreenSize

      val screenH = boxSize.getHeight.toInt
      val screenW = 270

      this.maximumSize = new Dimension(screenW, screenH)
      this.minimumSize = new Dimension(screenW, screenH)
      this.preferredSize = new Dimension(screenW, screenH)

      contents += functionPanel
      contents += integratedPanel
//      contents += searchPanel
//      contents += tab
    }

    //panel for presentation all available groups
    var group = paintGridPanel

    //group scrollPane
    var group_new = new ScrollPane(group)

    var labelLeft = new Label() {
      this.icon = new ImageIcon("logoLeft.png")
    }
    var labelRight = new Label() {
      this.icon = new ImageIcon("logoRight.png")
    }

    // LOGOAREA
    var logoLeftPanel = new BoxPanel(Orientation.Vertical) {
      contents += labelLeft
    }

    var logoRightPanel = new BoxPanel(Orientation.Vertical) {
      contents += labelRight

    }

    var newGroupTextFieldPanel = new BoxPanel(Orientation.Vertical) {

      var newGroupLabel = new Label("Creating a new Group")

      contents += newGroupLabel
      contents += name_group

    }

    var newGroupAddElementsPanel = new BoxPanel(Orientation.Vertical) {
      var testlabel = new Label("Hier sind dann die ganzen Elemente drinnen")
      contents += testlabel
    }
    var newGroupCreateButtonPanel = new BoxPanel(Orientation.Vertical) {
      contents += add_group
    }

    var newGroupPanel = new BoxPanel(Orientation.Horizontal) {

      this.preferredSize = new Dimension(900, 100)
      this.maximumSize = new Dimension(900, 100)
      this.minimumSize = new Dimension(900, 100)

      contents += newGroupTextFieldPanel
      contents += newGroupAddElementsPanel
      contents += newGroupCreateButtonPanel


    }

    //left-aligned Panel (containing two components)
    var box_left = new BoxPanel(Orientation.Vertical) {
      // this.preferredSize = new Dimension(1200, 600)

      contents += group_new
      contents += newGroupPanel

    }

    //complete window (containing left_box and right_box)
    val main = new BoxPanel(Orientation.Horizontal) {
      contents += logoLeftPanel
      contents += box_left
      contents += box_right
      contents += logoRightPanel


    }
    contents = main

    //window maximized
    this.maximize()


  }


  //Begin methods

  //paint gridPanel

  def paintGridPanel: GridPanel = {

    val playButton = Action("") {
      //TODO: Gruppe abspielen
    }
    val klappButton = Action("") {
      //TODO: Gruppe gross darstellen
    }
    val loeschButton = Action("") {
      //TODO: ausgewaehlte Gruppe entfernen
      //TODO Dialog
    }
    val hinzuButton = Action("") {

      val buttonP = new FlowPanel() {
        contents += new Button("okay")
        contents += new Button("abbrechen")
      }
      val dia = new Dialog() {
        centerOnScreen()
        resizable = true

        contents = new BoxPanel(Orientation.Vertical) {

          contents += new FlowPanel() {
            val kersIt = database.grouppool.iterator
            while (kersIt.hasNext) {
              val group = kersIt.next()
              val groupList = getJListFromGroup(group)
              groupList.setVisibleRowCount(1) //METRO STYLE bei 2, ansonsten 1
              groupList.setLayoutOrientation(JList.HORIZONTAL_WRAP)
              var scrolledGroupList = new ScrollPane(Component.wrap(groupList))
              //  contents += new FlowPanel(){
              contents += scrolledGroupList
              // }
            }
            this.preferredSize = new Dimension(400, 100)
          }
          contents += new FlowPanel() {
            var database = readFromFile(file)
            val dataList = getJListFromDatabase(database)
            val scrolledDataList = new ScrollPane(Component.wrap(dataList))
            contents += scrolledDataList
          }
          contents += buttonP
        }
      }
      dia.open()
    }


    val size = database.grouppool.size
    val grid = new GridPanel(size, 1) {
      val it = database.grouppool.iterator


      //GRUPPEN GENERIEREN
      while (it.hasNext) {
        val obj = it.next()
        val list = getJListFromGroup(obj)
        list.setDragEnabled(true)
        list.setDropMode(DropMode.INSERT)
        list.setTransferHandler(new MyTransferHandler)

        list.setVisibleRowCount(1) //METRO STYLE bei 2, ansonsten 1
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP)

        // THUMBNAILS
        var s_list = new ScrollPane(Component.wrap(list)) {
          this.preferredSize = new Dimension(730,73)
        }

        // TEXT
        var bp = new BoxPanel(Orientation.Vertical) {
          contents += new Label(obj.name) {
            this.font = new Font("TimesRoman", 0, 20)
          }
          contents += new Label("[" + obj.data.size + " Elements]")
          contents += new Button {
            action = playButton
            this.icon = new ImageIcon("icons\\24x24\\play.png")
          }
        }

        // Buttons für Gruppe
        var buttonPanel = new BoxPanel(Orientation.Vertical) {
          contents += new Button {
            action = hinzuButton
            this.icon = new ImageIcon("icons\\16x16\\add.png")
          }
          contents += new Button {
            action = loeschButton
            this.icon = new ImageIcon("icons\\16x16\\trash.png")

          }
          contents += new Button {
            action = klappButton
            this.icon = new ImageIcon("icons\\16x16\\eject.png")
          }
        }


        val fp = new FlowPanel() {
          border = Swing.EmptyBorder(30,30,30,30)
          contents += bp
          contents += s_list
          contents += buttonPanel
        }
        contents += fp
      }
    }
    grid
  }

}
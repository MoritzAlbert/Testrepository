package sq_gui

import scala.swing._
import event.{MousePressed, ButtonClicked}
import javax.swing.filechooser.FileNameExtensionFilter
import TabbedPane._
import java.awt.{Dimension, Color}
import dragndrop.MyTransferHandler
import javax.swing.{DropMode, JList, ImageIcon, UIManager}
import sq_gui.Image
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

    val settingDia = new Dialog() {
      this.preferredSize = new Dimension(600,400)
      this.centerOnScreen()
      resizable = true
      contents = new BoxPanel(Orientation.Vertical) {
        contents += new FlowPanel(){
          contents += new Label("Set standard data editors")
        }
        contents += new FlowPanel(){
          val fileChooserJPEG = new FileChooser()
          contents += new Label("JPEG:")
          contents += new Button(Action("Choose..."){
            fileChooserJPEG.showOpenDialog(this)
            // newPlayer(fileChooserPDF.selectedFile.toString())
          }) {
            this.tooltip = "Set editor for .jpeg data"
          }
         // contents += new Label(fileChooserJPEG.selectedFile.toString())
        }
        contents += new FlowPanel(){
          val fileChooserMP = new FileChooser()
          contents += new Label("MP4:")
          contents += new Button(Action("Choose..."){
            fileChooserMP.showOpenDialog(this)
          }) {
            this.tooltip = "Set editor for .mp4 data"
          }
        }
        contents += new FlowPanel(){
          val fileChooserPDF = new FileChooser()
          contents += new Label("PDF:")
          contents += new Button(Action("Choose..."){
            fileChooserPDF.showOpenDialog(this)

          }) {
            this.tooltip = "Set editor for .pdf data"
          }
        }
        contents += new FlowPanel() {
          contents += new Button("") {
            this.icon = new ImageIcon("icons\\24x24\\save.png")
            this.tooltip = "Save"
          }
          contents += new Button(Action("") {
            this.tooltip = "Cancel"
            close()
          }) {
            this.icon = new ImageIcon("icons\\24x24\\delete.png")
          }
        }
      }
    }


    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("Settings") {
          settingDia.open()
        })
        contents += new MenuItem(Action("Exit") {
          closeOperation()
        })
      }
      contents += new Menu("Help") {
        contents += new MenuItem("Onlinehelp")
      }
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
        val groupname = new TextField("")
        contents += groupname
      }
      Dialog.showMessage(null, panel.peer, "Enter group name", Dialog.Message.Plain)

      println(panel.groupname.text)
      println(database.grouppool.size)
      database.addToGrouppool(panel.groupname.text)
      println(database.grouppool.size)
      updateListGroup(list_group, database)
      //group.contents.clear()
      var groupUpdated = paintGridPanel
      //group.visible = false
      group = groupUpdated
      //group.visible = true
      group.peer.validate()
      group.repaint()
      // group.contents.update(0,groupUpdated)
      // group.repaint()
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

    var add_group = new Button {
      action = addGroup
    }


    val popupMenu = new PopupMenu {
      contents += new Menu("menu 1") {
        contents += new RadioMenuItem("radio 1.1")
        contents += new RadioMenuItem("radio 1.2")
      }
      contents += new Menu("menu 2") {
        contents += new RadioMenuItem("radio 2.1")
        contents += new RadioMenuItem("radio 2.2")
      }
    }


    var search = new Button("Test"){
      action = searchData
      this.tooltip = "Search"
      //this.icon = new ImageIcon("icons\\16x16\\search.png")
      this.preferredSize = new Dimension(45, 27)
    }


    var searchInput = new TextField("") {
      this.preferredSize = new Dimension(214, 25)
    }


    //     val allLabel = new Label("All"){
    //      this.icon = new ImageIcon("icon\\16x16\\add.png")
    //    }

    //filter tabs
    var tab_filter = new TabbedPane {
      pages += new Page("All", scroll)
      pages += new Page("Images", scroll_image)
      pages += new Page("Docs", scroll_doc)
      pages += new Page("Vids", scroll_video)
    }
    //filter Pane
    var tab = new ScrollPane(tab_filter) {
      this.preferredSize = new Dimension(270, 500)
    }

    // functionPanel
    var functionPanel = new FlowPanel() {
      this.preferredSize = new Dimension(270, 140)
      // background = petrolHEX
      //        FlowLayout.LEFT
      //TODO button possition setzen auf linksbuendig
      contents += add
      //TODO RATING buttons einfügen siehe unten
    }

    //searchPanel
    var searchPanel = new FlowPanel {
      contents += searchInput
      contents += search
    }

    var integratedPanel = new BoxPanel(Orientation.Vertical) {
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


    //left-aligned Panel (containing two components)
    var box_left = new BoxPanel(Orientation.Vertical) {
      // this.preferredSize = new Dimension(1200, 600)
      contents += group_new
      contents += add_group
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


  //Begin method

  //paint gridPanel
  def paintGridPanel: GridPanel = {
    println("i was here")

    val playButton = Action("") {
      //TODO: Gruppe abspielen
    }
    val editButton = Action("") {


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
          val buttonP = new FlowPanel() {
            contents += new Button(Action("") {

            }) {
              this.tooltip = "Save Changes"
              this.icon = new ImageIcon("icons\\24x24\\save.png")
              this.preferredSize = new Dimension(60, 30)
            }
            contents += new Button(Action("") {
              close()
            }) {
              this.tooltip = "Cancel"
              this.icon = new ImageIcon("icons\\24x24\\delete.png")
              this.preferredSize = new Dimension(60, 30)
            }
          }
          contents += buttonP
        }
        open()
      }
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
          this.preferredSize = new Dimension(730, 73)
        }

        val dataSize = new Label("[" + obj.data.size + " Elements]")
        // TEXT
        var bp = new BoxPanel(Orientation.Vertical) {
          contents += new Label(obj.name) {
            this.font = new Font("TimesRoman", 0, 20)
          }
          contents += dataSize
          contents += new Button {
            action = playButton
            this.tooltip = "Play group"
            this.icon = new ImageIcon("icons\\24x24\\play.png")
          }
        }

        val saveButton = Action("") {
          //   bp.revalidate(dataSize)
        }

        // Buttons für Gruppe
        var buttonPanel = new BoxPanel(Orientation.Vertical) {
          contents += new Button {
            action = saveButton
            this.tooltip = "Save group"
            this.icon = new ImageIcon("icons\\16x16\\save.png")
          }
          contents += new Button(Action("") {
            // val selectedGroup =
            //TODO: alle dialoge in englisch
            val x = Dialog.showConfirmation(null, "Delete Group?", "Question", Dialog.Options.YesNo, Dialog.Message.Question)
            if (x.toString().equals("Ok") || x.toString().equals("Yes")) {
              //TODO: ausgewaehlte Gruppe entfernen
//              val obj = group.getSelectedValue.asInstanceOf[ImageIcon]
//              println("ImageIcon: "+obj.getDescription)
//              val url = searchURL(obj.getDescription)
//              println(url)
//              database.removeFromGroupPool(url)
              //TODO auch hier updaten
            }
          }) {
            this.tooltip = "Delete group"
            this.icon = new ImageIcon("icons\\16x16\\trash.png")
          }
          contents += new Button {
            action = editButton
            this.tooltip = "Edit group settings"
            this.icon = new ImageIcon("icons\\16x16\\edit.png")
          }
        }
        val fp = new FlowPanel() {
          border = Swing.EmptyBorder(30, 30, 30, 30)
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
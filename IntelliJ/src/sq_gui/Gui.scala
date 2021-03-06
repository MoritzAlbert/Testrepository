package sq_gui

import scala.swing._
import event.{Key, KeyPressed}
import javax.swing.filechooser.FileNameExtensionFilter
import TabbedPane._
import jBrowser.JBrowser
import javax.swing.tree.{TreeSelectionModel, DefaultTreeSelectionModel, DefaultTreeModel, DefaultMutableTreeNode}
import javax.swing.{JList, UIManager, ImageIcon, JTree, DropMode}
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import dragndrop._
import java.awt.{Font, Dimension, Color}

//Begin object Object Gui
object Gui extends SimpleSwingApplication with UpdateFunctions with XML with Functions with Search {

  //declarations
  var petrolHEX = new Color(0x116856)
  var child_name = ""
  var child_list = new JList()
  val root = new DefaultMutableTreeNode("Groups")
  val tree_model = new DefaultTreeModel(root)
  var group_new = new ScrollPane()

  //Begin MainFrame
  def top = new MainFrame() {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

    override def closeOperation() {
      exportToXML(database, "test.xml")
      exportPlayerPreferencesToXML("pref.xml")
      System.exit(0)
    }


    title = "Gui Explorer"
    visible = true

    //treeview
    val jtree = new JTree(tree_model)
    val tsm = new DefaultTreeSelectionModel()
    tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION)
    jtree.setSelectionModel(tsm)
    jtree.addTreeSelectionListener(new TreeSelectionListener {
      def valueChanged(e: TreeSelectionEvent) {
      }
    })

    jtree.setRootVisible(true)

    val it = database.sortGrouppool().iterator
    while (it.hasNext) {
      val obj = it.next()
      val child = new DefaultMutableTreeNode(obj.name)
      tree_model.insertNodeInto(child, root, root.getChildCount)

    }
    expandAll(jtree)

    // New FlowPanel
    var frame = new FlowPanel()

    //Add data to datapool (with a filechooser)
    val addData = Action("") {

      val fileChooser = new FileChooser() {
        fileFilter = new FileNameExtensionFilter("JPG, PDF & MP4", "jpg", "pdf", "mp4")
      }

      // EXEPTION ABFANGEN WENN FENSTER OHNE AUSWAHL GESCHLOSSEN WIRD. JR
      fileChooser.showOpenDialog(frame)
      val file = fileChooser.selectedFile
      val url = file.getAbsolutePath
      database.addToDataPool(url)

      println(url)

      updateListData(list, database)
      updateListImage(list_image, database)
      updateListDocument(list_doc, database)
      updateListVideo(list_video, database)

      exportToXML(database, "test.xml")
    }

    //menubar
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("Settings") {
          settingDia.open()
        })
        contents += new MenuItem(Action("Save database") {
          exportToXML(database, "test.xml")
        })
        contents += new MenuItem(Action("Load database") {
          //filechooser mit xml
        })
        contents += new MenuItem(Action("Exit") {
          closeOperation()
        })
      }
      contents += new Menu("Help") {
        contents += new MenuItem(Action("Onlinehelp") {
          val browser = new JBrowser("http://www.julianrapp.de/sq/Help.html")
          val f = new Frame() {
            this.title = "Onlinehelp"
            contents = new ScrollPane(Component.wrap(browser))
          }
          f.size = new Dimension(900, 700)
          f.peer.setLocation(370, 20)
          f.visible = true
        })

        contents += new MenuItem(Action("About") {
          val browser = new JBrowser("http://www.julianrapp.de/sq/About.html")
          val f = new Frame() {
            this.title = "About"
            contents = new ScrollPane(Component.wrap(browser))
          }
          f.size = new Dimension(900, 700)
          f.peer.setLocation(370, 20)
          f.visible = true
        })

      }
    }

    //Dialog for entering a new group name
    val addGroup = Action("") {
      val panel = new BoxPanel(Orientation.Vertical) {
        val groupname = new TextField("")
        contents += groupname
      }
      Dialog.showMessage(null, panel.peer, "Please enter a group name", Dialog.Message.Plain)

      if (panel.groupname.text == "") {
        Dialog.showMessage(null, "Please enter a correct group name", "Missing input", Dialog.Message.Error)
      } else {
        database.addToGrouppool(panel.groupname.text)
        println(database.grouppool.size)
        val child = new DefaultMutableTreeNode(panel.groupname.text)
        tree_model.insertNodeInto(child, root, root.getChildCount)
        expandAll(jtree) //JR
      }
      updateFromXML()
    }

    //remove a group from treeview and groupview
    val delete_group = Action("") {
      val tp = jtree.getLeadSelectionPath
      if (tp == null) {
        Dialog.showMessage(null, "Select data to delete", "Missing input", Dialog.Message.Error)
      }
      else {
        val node = tp.getLastPathComponent.asInstanceOf[DefaultMutableTreeNode]
        if (node != root) {
          val x = Dialog.showConfirmation(null, "Delete group?", "Question", Dialog.Options.YesNo, Dialog.Message.Question)
          if (x.toString.equals("Ok") || x.toString.equals("Yes")) {
            tree_model.removeNodeFromParent(node)
            database.removeFromGrouppool(node.toString)
            println("Grouppool Size: " + database.grouppool.size)
            expandAll(jtree) //JR
          }
        }
      }
      updateFromXML()
    }

    var add_delete_group = new FlowPanel {

      contents += new Button {
        this.preferredSize = new Dimension(70, 40)
        action = addGroup
        this.icon = new ImageIcon("icons\\24x24\\new_group.png")

      }
      contents += new Button {
        this.preferredSize = new Dimension(70, 40)
        action = delete_group
        this.icon = new ImageIcon("icons\\24x24\\trash.png")
      }
    }

    var tree = new BoxPanel(Orientation.Vertical) {
      this.maximumSize = new Dimension(400, 800)
      contents += new ScrollPane(Component.wrap(jtree))
      contents += add_delete_group
    }

    val searchData = Action("") {
      startSearch(searchInput.text)
      updateSearchListData(list, database)
      searchInput.text = ""
      updateSearchListData(searchList, database)
    }

    //buttons
    var add = new Button() {
      action = addData
      this.preferredSize = new Dimension(255, 40)
      this.maximumSize = new Dimension(255, 40)
      this.minimumSize = new Dimension(255, 40)
      this.tooltip = "Add a new data to your datapool"
      this.icon = new ImageIcon("icons\\32x32\\add.png")
    }

    //textfields
    var name_group = new TextField() {

      this.preferredSize = new Dimension(120, 25)
      this.maximumSize = new Dimension(120, 25)
      this.minimumSize = new Dimension(120, 25)
    }

    var searchInput = new TextField("") {
      this.preferredSize = new Dimension(214, 25)
    }

    // SEARCH WITH ENTER ON END WITHOUT USE OF THE BUTTON

    listenTo(searchInput.keys)

    reactions += {
      case KeyPressed(_, Key.Enter, _, _) => startSearch(searchInput.text)
      updateSearchListData(list, database)
      searchInput.text = ""
      updateSearchListData(searchList, database)
    }

    val settingDia = new Dialog() {
      this.preferredSize = new Dimension(600, 300)
      this.location = new Point(100, 100)
      resizable = true
      contents = new BoxPanel(Orientation.Vertical) {
        contents += new FlowPanel() {
          contents += new Label("Set standard data editors")
        }
        contents += new FlowPanel() {
          val fileChooserJPEG = new FileChooser() {
            fileFilter = new FileNameExtensionFilter("Executing File for JPEG", "exe")
          }
          contents += new Label("Images:") {
            this.font = new Font("TimesRoman", 0, 20)
            //this.font = Font.BOLD
            // TODO
          }

          val playerImageLBL = new Label(Gui.playerImage) //
          contents += playerImageLBL //

          contents += new Button(Action("Change...") {

            fileChooserJPEG.showOpenDialog(this)
            val file = fileChooserJPEG.selectedFile
            //val url = file.toURI.toURL
            val url = file.getAbsolutePath
            println(url)
            //playerImage = url.getPath()
            playerImage = url
            playerImageLBL.text = Gui.playerImage //


          }) {
            this.tooltip = "Set editor for .jpeg data"
          }

          //contents += new Label(fileChooserJPEG.selectedFile.toString())
        }
        contents += new FlowPanel() {
          val fileChooserMP = new FileChooser() {
            fileFilter = new FileNameExtensionFilter("Executing File for MP4", "exe")
          }
          contents += new Label("Video:") {
            this.font = new Font("TimesRoman", 0, 20)
          }

          val playerVideoLBL = new Label(Gui.playerVideo) //
          contents += playerVideoLBL //

          contents += new Button(Action("Change...") {
            fileChooserMP.showOpenDialog(this)

            val file = fileChooserMP.selectedFile
            val url = file.getAbsolutePath
            playerVideo = url
            playerVideoLBL.text = Gui.playerVideo //

          }) {
            this.tooltip = "Set editor for .mp4 data"
          }
        }
        contents += new FlowPanel() {
          val fileChooserPDF = new FileChooser() {
            fileFilter = new FileNameExtensionFilter("Executing File for PDF", "exe")
          }
          contents += new Label("PDF:") {
            this.font = new Font("TimesRoman", 0, 20)
          }

          val playerPdfLBL = new Label(Gui.playerPDF) //
          contents += playerPdfLBL //

          contents += new Button(Action("Change...") {
            fileChooserPDF.showOpenDialog(this)

            val file = fileChooserPDF.selectedFile
            val url = file.getAbsolutePath
            playerPDF = url
            playerPdfLBL.text = Gui.playerPDF //


          }) {
            this.tooltip = "Set editor for .pdf data"
          }
        }
        contents += new FlowPanel() {
          contents += new Button(Action("") {
            exportPlayerPreferencesToXML("pref.xml")
            close()
          }) {
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


    var add_group = new Button {
      action = addGroup
    }


    var search = new Button("Test") {
      action = searchData
      this.tooltip = "Search"
      this.icon = new ImageIcon("icons\\16x16\\search.png")
      this.preferredSize = new Dimension(45, 27)
    }


    //filter tabs
    var tab_filter = new TabbedPane {
      pages += new Page("All", scroll)
      pages += new Page("Images", scroll_image)
      pages += new Page("Docs", scroll_doc)
      pages += new Page("Vids", scroll_video)
    }

    //filter Pane
    var tab = new ScrollPane(tab_filter) {
      this.preferredSize = new Dimension(270, 570)
    }

    // functionPanel
    var functionPanel = new FlowPanel() {
      this.preferredSize = new Dimension(270, 80)
      // background = petrolHEX
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
    group_new = new ScrollPane(group)

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

    var newGroupTextFieldPanel = new BoxPanel(Orientation.Horizontal) {

      contents += new Label("New Group")
      contents += name_group
    }

    var newGroupAddElementsPanel = new BoxPanel(Orientation.Vertical) {
      var testlabel = new Label("Hier sind dann die ganzen Elemente drinnen")
      contents += testlabel
      this.preferredSize = new Dimension(750, 100)
      this.background = Color.RED
    }
    var newGroupCreateButtonPanel = new BoxPanel(Orientation.Vertical) {
      contents += add_group
    }

    var newGroupPanel = new BoxPanel(Orientation.Horizontal) {

      contents += newGroupTextFieldPanel
      contents += newGroupAddElementsPanel
      contents += newGroupCreateButtonPanel


    }

    //left-aligned Panel (containing two components)
    var box_left = new BoxPanel(Orientation.Horizontal) {
      // this.preferredSize = new Dimension(1200, 600)
      contents += tree
      contents += group_new
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

  def updateFromXML() {
    group_new.contents = paintGridPanel
  }

  //paint gridPanel
  def paintGridPanel: GridPanel = {
    val size = database.grouppool.size
    val grid = new GridPanel(size, 1) {
      val it = database.sortGrouppool().iterator

      //GRUPPEN GENERIEREN
      while (it.hasNext) {
        val obj = it.next()
        val list = getJListFromGroup(obj)
        list.setDragEnabled(true)
        list.setDropMode(DropMode.INSERT)
        list.setTransferHandler(new MyTransferHandlerGroup)
        list.setVisibleRowCount(1)
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP)

        // THUMBNAILS
        var s_list = new ScrollPane(Component.wrap(list)) {
          this.preferredSize = new Dimension(700, 73)
        }

        // TEXT
        var bp = new BoxPanel(Orientation.Vertical) {

          val playButton = Action("PLAY") {
            playGroup(obj)
          }

          var tmp = ""
          if (obj.name.size > 10) {
            tmp = obj.name.substring(0, 10) + "..."
          }
          else {
            tmp = obj.name
          }

          contents += new Label(tmp) {
            this.font = new Font("TimesRoman", 0, 20)
            this.minimumSize = new Dimension(120, 28)
            this.preferredSize = new Dimension(120, 28)
            this.maximumSize = new Dimension(120, 28)
            this.horizontalTextPosition = Alignment.Left
            this.horizontalAlignment = Alignment.Left
            this.tooltip = "Rating: " + obj.rating + " " + "Info: " + obj.info
          }
          contents += new Label("[" + obj.data.size + " Elements]")
          contents += new Button {
            action = playButton
            this.tooltip = "Play group"
            this.icon = new ImageIcon("icons\\24x24\\play.png")
          }
        }

        // Buttons für Gruppe
        var buttonPanel = new BoxPanel(Orientation.Vertical) {
          contents += new Button(Action("") {
            updateFromXML()
          }) {
            this.tooltip = "Save group"
            this.icon = new ImageIcon("icons\\16x16\\save.png")
          }

          contents += new Button(Action("") {
            val index = list.getSelectedIndex
            if (index == (-1)) {
              Dialog.showMessage(null, "Select data to delete", "Missing input", Dialog.Message.Error)
            }
            else {
              val x = Dialog.showConfirmation(null, "Delete data from group?", "Question", Dialog.Options.YesNo, Dialog.Message.Question)
              if (x.toString.equals("Ok") || x.toString.equals("Yes")) {
                obj.data.remove(index)
              }
            }
            updateFromXML()
          }) {
            this.tooltip = "Delete data from group"
            this.icon = new ImageIcon("icons\\16x16\\remove.png")

          }
          contents += new Button(Action("") {

            val nameP = new FlowPanel() {
              contents += new Label("Group Name")
              val grpnameField = new TextField() {
                this.text = obj.name
                this.preferredSize = new Dimension(200, 20)
                this.maximumSize = new Dimension(200, 20)
                this.minimumSize = new Dimension(200, 20)
              }
              contents += grpnameField
            }

            val ratingP = new FlowPanel() {
              contents += new Label("Rating      ")
              val ratingField = new TextField() {
                this.text = obj.rating
                this.preferredSize = new Dimension(200, 20)
                this.maximumSize = new Dimension(200, 20)
                this.minimumSize = new Dimension(200, 20)
              }
              contents += ratingField
            }
            val informationP = new FlowPanel() {
              contents += new Label("Informations:")
              val info = new TextArea() {
                this.text = obj.info
                preferredSize = new Dimension(200, 100)
              }
              contents += info
            }

            val panel = new BoxPanel(Orientation.Vertical) {

              contents += nameP
              contents += ratingP
              contents += informationP
            }
            Dialog.showMessage(null, panel.peer, "Group Changes", Dialog.Message.Plain)

            obj.name = nameP.grpnameField.text
            obj.rating = ratingP.ratingField.text
            obj.info = informationP.info.text
            updateFromXML()
          }
          ) {
            this.tooltip = "Edit group settings"
            this.icon = new ImageIcon("icons\\16x16\\edit.png")
          }
        }

        val fp = new FlowPanel() {
          border = Swing.EmptyBorder(20, 20, 20, 20)
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
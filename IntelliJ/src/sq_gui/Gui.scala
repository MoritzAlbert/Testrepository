package sq_gui

import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import TabbedPane._
import jBrowser.JBrowser
import javax.swing.tree.{TreeSelectionModel, DefaultTreeSelectionModel, DefaultTreeModel, DefaultMutableTreeNode}
import javax.swing.{JList, UIManager, ImageIcon, JTree, DropMode}
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import dragndrop._
import java.awt.{Dimension, Color}

//Begin object Object Gui
object Gui extends SimpleSwingApplication with UpdateFunctions with XML with Functions with Search {

  var petrolHEX = new Color(0x116856)
  var child_name = ""
  var child_list = new JList()
  val root = new DefaultMutableTreeNode("Groups")
  val tree_model = new DefaultTreeModel(root)

  //Begin MainFrame
  def top = new MainFrame() {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

    override def closeOperation() {
      exportToXML(database, "test.xml")
      System.exit(0)
    }

    title = "Gui Explorer"
    visible = true

    //treeview
    val jtree = new JTree(tree_model)
    val tsm = new DefaultTreeSelectionModel()
    tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION)
    jtree.setSelectionModel(tsm)

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

    //Add data to datapool
    val addData = Action("Hinzufügen") {

      val fileChooser = new FileChooser() {
        fileFilter = new FileNameExtensionFilter("JPG, PDF & MP4", "jpg", "pdf", "mp4")
      }

      fileChooser.showOpenDialog(frame)
      val file = fileChooser.selectedFile

      val url = file.toURI.toURL

      //database.addToDataPool(url.getQuery)
      database.addToDataPool(url.getPath)

      println(url.getFile)

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
        contents += new MenuItem(Action("Exit") {
          closeOperation()
        })
      }

      contents += new Menu("Help") {
        contents += new MenuItem(Action("Onlinehelp") {
          val browser = new JBrowser("http://www.laptico.de")
          val f = new Frame() {
            contents = new ScrollPane(Component.wrap(browser))
          }
          f.peer.setSize(750, 700)
          f.peer.setLocation(370, 20)
          f.visible = true
        })
      }
    }

    val addGroup = Action("") {
      val panel = new BoxPanel(Orientation.Vertical) {
        val groupname = new TextField("")
        contents += groupname
      }

      Dialog.showMessage(null, panel.peer, "Enter group name", Dialog.Message.Plain)

      println(panel.groupname.text)
      println(database.grouppool.size)

      if (panel.groupname.text == "") {
        Dialog.showMessage(null, "Please enter name", "Missing input", Dialog.Message.Error)
      } else {
        database.addToGrouppool(panel.groupname.text)
        println(database.grouppool.size)
        val child = new DefaultMutableTreeNode(panel.groupname.text)
        tree_model.insertNodeInto(child, root, root.getChildCount)
      }
      updateFromXML()
    }

    val delete_group = Action("") {
      val tp = jtree.getLeadSelectionPath
      val node = tp.getLastPathComponent.asInstanceOf[DefaultMutableTreeNode]

      if (node != root) {
        val x = Dialog.showConfirmation(null, "Do you really want to delete this group?", "Question", Dialog.Options.YesNo, Dialog.Message.Question)
        if (x.toString.equals("Ok")) {
          tree_model.removeNodeFromParent(node)
          database.removeFromGrouppool(node.toString)
        }

      }
      updateFromXML()

    }

    var add_group = new Button {
      action = addGroup
    }

    var add_delete_group = new FlowPanel {

      contents += new Button {
        action = addGroup
        this.icon = new ImageIcon("icons\\24x24\\new_group.png")

      }
      contents += new Button {
        action = delete_group
        this.icon = new ImageIcon("icons\\24x24\\trash.png")
      }
    }

    var tree = new BoxPanel(Orientation.Vertical) {

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
    var add = new Button {
      action = addData
    }

    var search = new Button() {
      action = searchData
      this.icon = new ImageIcon("icons\\16x16\\search.png")
      this.preferredSize = new Dimension(40, 25)
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

    val settingDia = new Dialog() {
      this.preferredSize = new Dimension(600, 400)
      this.centerOnScreen()
      resizable = true
      contents = new BoxPanel(Orientation.Vertical) {
        contents += new FlowPanel() {
          contents += new Label("Set standard data editors")
        }
        contents += new FlowPanel() {
          val fileChooserJPEG = new FileChooser()
          contents += new Label("JPEG:")
          contents += new Button(Action("Choose...") {
            fileChooserJPEG.showOpenDialog(this)

            val file = fileChooserJPEG.selectedFile
            //val url = file.toURI.toURL
            val url = file.getAbsolutePath
            println(url)
            //playerImage = url.getPath()
            playerImage = url

          }) {
            this.tooltip = "Set editor for .jpeg data"
          }
          // contents += new Label(fileChooserJPEG.selectedFile.toString())
        }
        contents += new FlowPanel() {
          val fileChooserMP = new FileChooser()
          contents += new Label("MP4:")
          contents += new Button(Action("Choose...") {
            fileChooserMP.showOpenDialog(this)

            val file = fileChooserMP.selectedFile
            val url = file.getAbsolutePath
            playerVideo = url
            println(playerVideo)

          }) {
            this.tooltip = "Set editor for .mp4 data"
          }
        }
        contents += new FlowPanel() {
          val fileChooserPDF = new FileChooser()
          contents += new Label("PDF:")
          contents += new Button(Action("Choose...") {
            fileChooserPDF.showOpenDialog(this)

            val file = fileChooserPDF.selectedFile
            val url = file.getAbsolutePath
            playerPDF = url

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
    var group_new = new ScrollPane(group)

    def updateFromXML() {
      group_new.contents = paintGridPanel
    }

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

      this.preferredSize = new Dimension(900, 100)
      this.maximumSize = new Dimension(900, 100)
      this.minimumSize = new Dimension(900, 100)

      contents += newGroupTextFieldPanel
      contents += newGroupAddElementsPanel
      contents += newGroupCreateButtonPanel


    }

    //left-aligned Panel (containing two components)
    var box_left = new BoxPanel(Orientation.Horizontal) {
      // this.preferredSize = new Dimension(1200, 600)

      contents += tree
      contents += group_new
      //contents += newGroupPanel

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

    val klappButton = Action("") {
      //TODO: Gruppe gross darstellen
    }
    val loeschButton = Action("") {
      //TODO: ausgewaehlte Gruppe entfernen
      //TODO Dialog
    }

    val hinzuButton = Action("") {}

    val buttonP = new FlowPanel() {
      contents += new Button("okay")
      contents += new Button("abbrechen")
    }

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
          this.preferredSize = new Dimension(730, 73)
        }

        // TEXT
        var bp = new BoxPanel(Orientation.Vertical) {

          val playButton = Action("") {
            obj.playGroup()
          }

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
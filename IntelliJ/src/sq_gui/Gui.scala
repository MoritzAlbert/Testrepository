package sq_gui

import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import TabbedPane._
import java.awt.{Dimension, Color}
import dragndrop.MyTransferHandler
import jBrowser.JBrowser
import javax.swing.tree.{TreeSelectionModel, DefaultTreeSelectionModel, DefaultTreeModel, DefaultMutableTreeNode}
import javax.swing.{JList, UIManager, ImageIcon, JTree, DropMode, JButton, JPanel}
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}

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
      System.exit(0);

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
        val tp = jtree.getLastSelectedPathComponent.toString
        child_name = tp
        println(child_name)
        val grp = database.getGroupByString(tp)
        child_list = getJListFromGroup(grp)
      }
    })

    jtree.setRootVisible(true)

    val it = database.grouppool.iterator
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

    //menubar
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem("Settings")
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
      val child = new DefaultMutableTreeNode(panel.groupname.text)
      tree_model.insertNodeInto(child, root, root.getChildCount)

    }

    val delete_group = Action("delete group") {
      val tp = jtree.getLeadSelectionPath
      val node = tp.getLastPathComponent.asInstanceOf[DefaultMutableTreeNode]
      database.removeFromGrouppool(node.toString)
      if (node != root) {
        tree_model.removeNodeFromParent(node)
      }
    }

    var add_group = new Button {
      action = addGroup
    }

    var add_delete_group = new FlowPanel {

      contents += new Button {
        action = addGroup
      }
      contents += new Button {
        action = delete_group
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
      val it = database.grouppool.iterator

      //GRUPPEN GENERIEREN
      while (it.hasNext) {
        val obj = it.next()
        val list = getJListFromGroup(obj)
        list.setDragEnabled(true)
        list.setDropMode(DropMode.INSERT)
        list.setTransferHandler(new MyTransferHandler)

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
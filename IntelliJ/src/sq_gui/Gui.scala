package sq_gui

import scala.swing._
import javax.swing.ImageIcon
import javax.swing.JList
import javax.swing.DefaultListModel
import java.io.File
import extension._
import com.ebenius._
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.UIManager
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.Image
import org.jdom.input.SAXBuilder
import org.jdom.Element
import org.jdom.output.XMLOutputter
import org.jdom.output.Format
import java.io.FileOutputStream
import javax.swing.DropMode
import TabbedPane._


//Begin object Object Gui

object Gui extends SimpleSwingApplication {

  //Declarations
  val file = new File("test.xml")
  var database = readFromFile(file)

  var col_data = (database.datapool.size) / 3 + 1
  var col_image = (database.imagepool.size) / 3 + 1
  var col_doc = (database.documentpool.size) / 3 + 1
  var col_video = (database.videopool.size) / 3 + 1

  var list = getJListFromDatabase(database)

  list.setDragEnabled(true)
  list.setDropMode(DropMode.INSERT)
  list.setTransferHandler(new ListMoveTransferHandler())
  list.setVisibleRowCount(col_data)
  list.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll = ListRenderer(list)

  var list_image = getJListImageFromDatabase(database)

  list_image.setDragEnabled(true)
  list_image.setDropMode(DropMode.INSERT)
  list_image.setTransferHandler(new ListMoveTransferHandler())
  list_image.setVisibleRowCount(col_image)
  list_image.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll_image = ListRenderer(list_image)

  var list_doc = getJListDocumentFromDatabase(database)
  list_doc.setDragEnabled(true)
  list_doc.setDropMode(DropMode.INSERT)
  list_doc.setTransferHandler(new ListMoveTransferHandler())
  list_doc.setVisibleRowCount(col_doc)
  list_doc.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll_doc = ListRenderer(list_doc)

  var list_video = getJListVideoFromDatabase(database)

  list_video.setDragEnabled(true)
  list_video.setDropMode(DropMode.INSERT)
  list_video.setTransferHandler(new ListMoveTransferHandler())
  list_video.setVisibleRowCount(col_video)
  list_video.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll_video = ListRenderer(list_video)

  //Begin MainFrame

  def top = new MainFrame() {

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    title = "Gui Explorer"
    visible = true

    var frame = new FlowPanel()


    //Add data to datapool
    val addData = Action("add") {

      val fileChooser = new FileChooser() {
        fileFilter = new FileNameExtensionFilter("JPG,PDF & MP4", "jpg", "pdf", "mp4")
      }

      fileChooser.showOpenDialog(frame)
      val file = fileChooser.selectedFile

      val url = file.toURI.toURL

      database.addToDataPool(url.getPath)

      updateListData(list, database)
      updateListImage(list_image, database)
      updateListDocument(list_doc, database)
      updateListVideo(list_video, database)

      exportToXML(database, "test.xml")

    }

    //add-Button
    var add = new Button {
      action = addData
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

    //searchPanel
    var searchPanel = new FlowPanel {

      contents += new TextField("Suche")

    }

    //right-aligned Panel (containing three components)
    var box_right = new BoxPanel(Orientation.Vertical) {

      contents += add
      contents += tab
      contents += searchPanel

    }

    //panel for presentation all available groups
    var group = paintGridPanel

    //group scrollPane
    var group_new = new ScrollPane(group)

    //left-aligned Panel (containing two components)
    var box_left = new BoxPanel(Orientation.Vertical) {
      contents += group_new
      contents += new Button("add group")
    }

    //complete window (containing left_box and right_box)
    val main = new BoxPanel(Orientation.Horizontal) {

      contents += box_left

      contents += box_right

    }

    contents = main

  }

  //Begin methods

  //generating a JList from a database
  def getJListFromDatabase(data: Datapool): JList = {

    val model = new DefaultListModel() {

      val it = data.datapool.iterator

      while (it.hasNext) {
        val data = it.next()

        if (data.url.endsWith(".jpg")) {

          val img = data.asInstanceOf[Image]

          img.image.getImage.getScaledInstance(10, 10, 10)

          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if (data.url.endsWith(".pdf")) {

          val img = data.asInstanceOf[Document]

          img.image.getImage.getScaledInstance(10, 10, 10)

          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if (data.url.endsWith(".mp4")) {

          val img = data.asInstanceOf[Video]

          img.image.getImage.getScaledInstance(10, 10, 10)

          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }

      }


    }

    val list = new JList(model)

    list.addMouseListener(new MouseAdapter() {

      override def mouseClicked(s: MouseEvent) {

        val e = s.getSource.asInstanceOf[JList]

        val f = e.getSelectedValue.asInstanceOf[ImageIcon]

        if (s.getClickCount == 2) {
          playObject(f.toString)
        }

      }

    })

    list

  }

  //generating an image-JList from database
  def getJListImageFromDatabase(data: Datapool): JList = {

    val model = new DefaultListModel() {

      val it = data.imagepool.iterator

      while (it.hasNext) {

        val data = it.next()

        data.image.getImage.getScaledInstance(10, 10, 10)

        data.image.setImage(data.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        this.addElement(data.image)

      }
    }

    val list = new JList(model)

    list.addMouseListener(new MouseAdapter() {

      override def mouseClicked(s: MouseEvent) {

        val e = s.getSource.asInstanceOf[JList]

        val f = e.getSelectedValue.asInstanceOf[ImageIcon]

        if (s.getClickCount == 2) {
          playObject(f.toString)
        }

      }

    })

    list

  }

  //generating a document/pdf-JList from database
  def getJListDocumentFromDatabase(data: Datapool): JList = {

    val model = new DefaultListModel() {

      val it = data.documentpool.iterator

      while (it.hasNext) {

        val data = it.next()

        data.image.getImage.getScaledInstance(10, 10, 10)

        data.image.setImage(data.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        this.addElement(data.image)

      }
    }

    val list = new JList(model)

    list.addMouseListener(new MouseAdapter() {

      override def mouseClicked(s: MouseEvent) {

        val e = s.getSource.asInstanceOf[JList]

        val f = e.getSelectedValue.asInstanceOf[ImageIcon]

        if (s.getClickCount == 2) {
          playObject(f.toString)
        }

      }

    })

    list

  }

  //generating a video-JList from database
  def getJListVideoFromDatabase(data: Datapool): JList = {

    val model = new DefaultListModel {

      val it = data.videopool.iterator

      while (it.hasNext) {

        val data = it.next()

        data.image.getImage.getScaledInstance(10, 10, 10)

        data.image.setImage(data.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        this.addElement(data.image)

      }
    }

    val list = new JList(model)

    list.addMouseListener(new MouseAdapter() {

      override def mouseClicked(s: MouseEvent) {

        val e = s.getSource.asInstanceOf[JList]

        val f = e.getSelectedValue.asInstanceOf[ImageIcon]

        if (s.getClickCount == 2) {
          playObject(f.toString)
        }

      }

    })

    list

  }

  //generating a JList of all groups in database
  def getJListGroupFromDatabase(s: Group): JList = {

    val model = new DefaultListModel() {

      val it = s.data.iterator

      while (it.hasNext) {
        val data = it.next()

        if (data.url.endsWith(".jpg")) {

          val img = data.asInstanceOf[Image]

          img.image.getImage.getScaledInstance(10, 10, 10)

          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if (data.url.endsWith(".pdf")) {

          val img = data.asInstanceOf[Document]

          img.image.getImage.getScaledInstance(10, 10, 10)

          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if (data.url.endsWith(".mp4")) {

          val img = data.asInstanceOf[Video]

          img.image.getImage.getScaledInstance(10, 10, 10)

          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }

      }

    }

    val list = new JList(model)

    list.addMouseListener(new MouseAdapter() {

      override def mouseClicked(s: MouseEvent) {

        val e = s.getSource.asInstanceOf[JList]

        val f = e.getSelectedValue.asInstanceOf[ImageIcon]

        if (s.getClickCount == 2) {
          playObject(f.toString)
        }

      }

    })

    list
  }

  //list-renderer
  def ListRenderer(s: JList): Component = {

    val renderer = new IconTextListCellRenderer()

    s.setCellRenderer(renderer)

    val scroll = new ScrollPane(Component.wrap(s))

    scroll
  }

  //playing an object
  def playObject(s: String) {

    val x = s.split("[.]")

    if (x.last.equals("jpg")) {
      playImage(s)
    }
    if (x.last.equals("pdf")) {
      playDocument(s)
    }
    if (x.last.equals("mp4")) {
      playVideo(s)
    }

  }

  //presenting an image
  def playImage(s: String) {

    val it = database.imagepool.iterator

    println(s)

    while (it.hasNext) {

      val data = it.next()

      if (data.getName.equals(s)) {
        println(data.url)
        data.play
      }

    }

  }

  //presenting a pdf-file
  def playDocument(s: String) {

    val it = database.documentpool.iterator

    while (it.hasNext) {

      val data = it.next()

      if (data.getName.equals(s)) {
        println(data.url)
        data.play
      }

    }

  }

  //presenting a video
  def playVideo(s: String) {

    val it = database.videopool.iterator

    while (it.hasNext) {

      val data = it.next()

      if (data.getName.equals(s)) {
        println(data.url)
        data.play
      }

    }

  }

  //paint gridPanel
  def paintGridPanel: GridPanel = {

    val size = database.grouppool.size

    val grid = new GridPanel(size, 1) {

      val it = database.grouppool.iterator

      while (it.hasNext) {

        val obj = it.next()


        val list = getJListGroupFromDatabase(obj)

        list.setDragEnabled(true)
        list.setDropMode(DropMode.INSERT)
        list.setTransferHandler(new ListMoveTransferHandler())
        list.setVisibleRowCount(1)
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP)

        var s_list = new ScrollPane(Component.wrap(list))

        var bp = new BoxPanel(Orientation.Vertical) {

          contents += new Label(obj.name)
          contents += new Label(obj.data.size + " Elements")
          //border = Swing.EmptyBorder(30, 30, 10, 30)
        }

        val fp = new FlowPanel {

          contents += bp
          contents += s_list

        }

        contents += fp

      }
    }

    grid

  }

  //update methods

  //updating listData
  def updateListData(list: JList, data: Datapool) {

    val listModel = list.getModel.asInstanceOf[DefaultListModel]

    listModel.clear()

    val it = data.datapool.iterator

    while (it.hasNext) {

      val data = it.next()

      if (data.url.endsWith(".jpg")) {

        val img = data.asInstanceOf[Image]

        img.image.getImage.getScaledInstance(10, 10, 10)

        img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        listModel.addElement(img.image)

      }
      if (data.url.endsWith(".pdf")) {

        val img = data.asInstanceOf[Document]

        img.image.getImage.getScaledInstance(10, 10, 10)

        img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        listModel.addElement(img.image)

      }
      if (data.url.endsWith(".mp4")) {

        val img = data.asInstanceOf[Video]

        img.image.getImage.getScaledInstance(10, 10, 10)

        img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        listModel.addElement(img.image)

      }

    }

  }

  //updating listImage
  def updateListImage(list: JList, data: Datapool) {

    val listModel = list.getModel.asInstanceOf[DefaultListModel]

    listModel.clear()

    val it = data.imagepool.iterator

    while (it.hasNext) {

      val data = it.next()

      data.image.getImage.getScaledInstance(10, 10, 10)

      data.image.setImage(data.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

      listModel.addElement(data.image)

    }
  }

  //updating listDocument
  def updateListDocument(list: JList, data: Datapool) {

    val listModel = list.getModel.asInstanceOf[DefaultListModel]

    listModel.clear()

    val it = data.documentpool.iterator

    while (it.hasNext) {

      val data = it.next()

      data.image.getImage.getScaledInstance(10, 10, 10)

      data.image.setImage(data.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

      listModel.addElement(data.image)

    }
  }

  //updating listVideo
  def updateListVideo(list: JList, data: Datapool) {

    val listModel = list.getModel.asInstanceOf[DefaultListModel]

    listModel.clear()

    val it = data.videopool.iterator

    while (it.hasNext) {

      val data = it.next()

      data.image.getImage.getScaledInstance(10, 10, 10)

      data.image.setImage(data.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))

      listModel.addElement(data.image)

    }
  }

  //reading method

  //read from file
  def readFromFile(f: File): Datapool = {

    val sAXBuilder = new SAXBuilder

    val doc = sAXBuilder.build(f)

    val root = doc.getRootElement

    val database = new Datapool

    val children = root.getChildren

    //   var array = new Array[Array[Int]](1000)(1000)
    //
    //   var m = new Array[Int](children.size())

    var i = 0

    while (i < children.size()) {

      var j = 0

      val x = children.get(i)

      val elem = x.asInstanceOf[Element]

      var name = ""

      if (elem.getName == "Group") {

        name = elem.getChild("name").getText
        database.addToGrouppool(name)

        val child = elem.getChildren("Data")

        while (j < child.size) {

          val x = child.get(j)

          val kind = x.asInstanceOf[Element]

          val url = kind.getChild("URL").getText

          database.getGroup(name).addData(url)
          j += 1

        }

      }
      else {

        name = elem.getChild("URL").getText

        database.addToDataPool(name)

      }

      i += 1
    }

    database

  }

  //export method

  //export to XML
  def exportToXML(d: Datapool, s: String) {

    val root = new Element("Datapool")

    val doc = new org.jdom.Document(root)

    val it = d.datapool.iterator

    while (it.hasNext) {

      val data = it.next()

      val url = data.url
      root.addContent(new Element("Data")
        .addContent(new Element("URL")
        .addContent(url)))
    }

    val it2 = d.grouppool.iterator

    var i = d.datapool.size

    while (it2.hasNext) {

      val grp = it2.next()

      val name = grp.name

      root.addContent(new Element("Group")
        .addContent(new Element("name")
        .addContent(name)))

      val children = root.getChildren

      val x = children.get(i)

      val elem = x.asInstanceOf[Element]

      val set = grp.data

      val it3 = set.iterator

      while (it3.hasNext) {

        val obj = it3.next()

        val url = obj.url
        elem.addContent(new Element("Data")
          .addContent(new Element("URL")
          .addContent(url)))

      }

      i += 1

    }

    val xml = new XMLOutputter(Format.getPrettyFormat)

    val output = new FileOutputStream(s)

    xml.output(doc, output)


  }

}
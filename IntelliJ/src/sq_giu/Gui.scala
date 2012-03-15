package sq_gui
import scala.swing.MainFrame
import scala.swing._
import javax.swing.ImageIcon
import javax.swing.ScrollPaneConstants
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.DefaultListModel
import javax.swing.JFrame
import java.net.URL
import java.io.File
import javax.swing.JPanel
import javax.swing.JComponent
import extension._
import com.ebenius._
import javax.swing.JScrollPane
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.UIManager
import javax.swing.SwingUtilities
import scala.swing.event.MouseClicked
import scala.swing.event.ButtonClicked
import scala.swing.event.SelectionChanged
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import scala.swing.event.MousePressed
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.Image
import scala.collection.mutable.HashSet
import org.jdom.input.SAXBuilder
import org.jdom.Element
import org.jdom.output.XMLOutputter
import org.jdom.output.Format
import java.io.FileOutputStream
import javax.swing.event.MouseInputAdapter
import javax.swing.DropMode

object Gui extends SimpleGUIApplication{

  var file = new File("test.xml")

  var datapool = readFromFile(file)

  var col_data = (datapool.datapool.size)/3 +1
  var col_image = (datapool.imagepool.size)/3 +1
  var col_doc = (datapool.documentpool.size)/3 +1
  var col_video = (datapool.videopool.size)/3 +1

  var list = getJlistData(datapool)

  list.setDragEnabled(true)
  list.setDropMode(DropMode.INSERT)
  list.setTransferHandler(new ListMoveTransferHandler())
  list.setVisibleRowCount(col_data)
  list.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll = ListRenderer(list)

  var list_image = getJlistImage(datapool)

  list_image.setDragEnabled(true)
  list_image.setDropMode(DropMode.INSERT)
  list_image.setTransferHandler(new ListMoveTransferHandler())
  list_image.setVisibleRowCount(col_image)
  list_image.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll_image = ListRenderer(list_image)

  var list_doc = getJlistDocument(datapool)
  list_doc.setDragEnabled(true)
  list_doc.setDropMode(DropMode.INSERT)
  list_doc.setTransferHandler(new ListMoveTransferHandler())
  list_doc.setVisibleRowCount(col_doc)
  list_doc.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll_doc = ListRenderer(list_doc)

  var list_video = getJlistVideo(datapool)

  list_video.setDragEnabled(true)
  list_video.setDropMode(DropMode.INSERT)
  list_video.setTransferHandler(new ListMoveTransferHandler())
  list_video.setVisibleRowCount(col_video)
  list_video.setLayoutOrientation(JList.VERTICAL_WRAP)

  var scroll_video = ListRenderer(list_video)

  def top = new MainFrame(){

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    title = "Gui Explorer"

    visible = true

    var frame = new FlowPanel()

    val addData = Action("add"){

      var filechooser = new FileChooser(){
        fileFilter = new FileNameExtensionFilter("JPG,PDF & MP4","jpg","pdf","mp4")
      }

      filechooser.showOpenDialog(frame)
      var file = filechooser.selectedFile

      var url = file.toURL()

      datapool.addToDataPool(url.getPath())

      updateListData(list,datapool)
      updateListImage(list_image,datapool)
      updateListDocument(list_doc,datapool)
      updateListVideo(list_video,datapool)

      exportToXML(datapool,"test.xml")

    }

    var add = new Button{action=addData}

    var tab_test = new TabbedPane{

      import TabbedPane._

      pages += new Page("Alle",scroll)
      pages += new Page("Images",scroll_image)
      pages += new Page("Docs",scroll_doc)
      pages += new Page("Vids",scroll_video)

    }

    var tab = new ScrollPane(tab_test)

    var search = new FlowPanel{

      contents += new TextField("Suche")

    }

    var box_rechts = new BoxPanel(Orientation.Vertical){

      contents += add
      contents += tab
      contents += search

    }

    var group = paintGridPanel

    var group_new = new ScrollPane(group)

    var box_links = new BoxPanel(Orientation.Vertical){
      contents += group_new
      contents += new Button("add group")
    }


    val main = new BoxPanel(Orientation.Horizontal){

      contents += box_links

      contents += box_rechts

    }


    contents = main

  }

  def getJlistData(data:Datapool) : JList = {

    var model = new DefaultListModel(){

      var it = data.datapool.iterator

      while(it.hasNext){
        var data = it.next()

        if(data.url.endsWith(".jpg")){

          var img = data.asInstanceOf[Image]

          img.image.getImage().getScaledInstance(10,10,10)

          img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if(data.url.endsWith(".pdf")){

          var img = data.asInstanceOf[Document]

          img.image.getImage().getScaledInstance(10,10,10)

          img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if(data.url.endsWith(".mp4")){

          var img = data.asInstanceOf[Video]

          img.image.getImage().getScaledInstance(10,10,10)

          img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }

      }


    }

    var list = new JList(model)

    list.addMouseListener(new MouseAdapter(){

      override def mouseClicked(s: MouseEvent){

        var e = s.getSource().asInstanceOf[JList]

        val f = e.getSelectedValue().asInstanceOf[ImageIcon]

        if(s.getClickCount()==2){
          getObject(f.toString())
        }

      }

    })

    list

  }

  def getJlistImage(data:Datapool) : JList = {

    var model = new DefaultListModel(){

      var it = data.imagepool.iterator

      while(it.hasNext){

        var data = it.next()

        data.image.getImage().getScaledInstance(10,10,10)

        data.image.setImage(data.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        this.addElement(data.image)

      }
    }

    var list = new JList(model)

    list.addMouseListener(new MouseAdapter(){

      override def mouseClicked(s: MouseEvent){

        var e = s.getSource().asInstanceOf[JList]

        val f = e.getSelectedValue().asInstanceOf[ImageIcon]

        if(s.getClickCount()==2){
          getObject(f.toString())
        }

      }

    })

    list

  }

  def getJlistDocument(data:Datapool) : JList = {

    var model = new DefaultListModel(){

      var it = data.documentpool.iterator

      while(it.hasNext){

        var data = it.next()

        data.image.getImage().getScaledInstance(10,10,10)

        data.image.setImage(data.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        this.addElement(data.image)

      }
    }

    var list = new JList(model)

    list.addMouseListener(new MouseAdapter(){

      override def mouseClicked(s: MouseEvent){

        var e = s.getSource().asInstanceOf[JList]

        val f = e.getSelectedValue().asInstanceOf[ImageIcon]

        if(s.getClickCount()==2){
          getObject(f.toString())
        }

      }

    })

    list

  }

  def getJlistVideo(data:Datapool) : JList = {

    var model = new DefaultListModel(){

      var it = data.videopool.iterator

      while(it.hasNext){

        var data = it.next()

        data.image.getImage().getScaledInstance(10,10,10)

        data.image.setImage(data.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        this.addElement(data.image)

      }
    }

    var list = new JList(model)

    list.addMouseListener(new MouseAdapter(){

      override def mouseClicked(s: MouseEvent){

        var e = s.getSource().asInstanceOf[JList]

        val f = e.getSelectedValue().asInstanceOf[ImageIcon]

        if(s.getClickCount()==2){
          getObject(f.toString())
        }

      }

    })

    list

  }

  def getJlistGroup(s:Group) : JList = {

    var model = new DefaultListModel(){

      var it = s.data.iterator

      while(it.hasNext){
        var data = it.next()

        if(data.url.endsWith(".jpg")){

          var img = data.asInstanceOf[Image]

          img.image.getImage().getScaledInstance(10,10,10)

          img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if(data.url.endsWith(".pdf")){

          var img = data.asInstanceOf[Document]

          img.image.getImage().getScaledInstance(10,10,10)

          img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }
        if(data.url.endsWith(".mp4")){

          var img = data.asInstanceOf[Video]

          img.image.getImage().getScaledInstance(10,10,10)

          img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

          this.addElement(img.image)

        }

      }

    }

    var list = new JList(model)

    list.addMouseListener(new MouseAdapter(){

      override def mouseClicked(s: MouseEvent){

        var e = s.getSource().asInstanceOf[JList]

        val f = e.getSelectedValue().asInstanceOf[ImageIcon]

        if(s.getClickCount()==2){
          getObject(f.toString())
        }

      }

    })

    list
  }


  def ListRenderer(s:JList) : Component = {

    var renderer = new IconTextListCellRenderer()

    s.setCellRenderer(renderer)

    var scroll = new ScrollPane(Component.wrap(s))

    scroll
  }

  def getObject(s:String){

    var x = s.split("[.]")

    if(x.last.equals("jpg")){
      getImageObj(s)
    }
    if(x.last.equals("pdf")){
      getDocumentObj(s)
    }
    if(x.last.equals("mp4")){
      getVideoObj(s)
    }

  }

  def getImageObj(s:String){

    var it = datapool.imagepool.iterator

    println(s)

    while(it.hasNext){

      var data = it.next()

      if(data.getName.equals(s)){
        println(data.url)
        data.play
      }

    }

  }

  def getDocumentObj(s:String){

    var it = datapool.documentpool.iterator

    while(it.hasNext){

      var data = it.next()

      if(data.getName.equals(s)){
        println(data.url)
        data.play
      }

    }

  }

  def getVideoObj(s:String){

    var it = datapool.videopool.iterator

    while(it.hasNext){

      var data = it.next()

      if(data.getName.equals(s)){
        println(data.url)
        data.play
      }

    }

  }

  def paintGridPanel :GridPanel = {

    var size = datapool.grouppool.size

    var grid = new GridPanel(size,1){

      var it = datapool.grouppool.iterator

      while (it.hasNext){

        var obj = it.next()


        var jlist = getJlistGroup(obj)

        jlist.setDragEnabled(true)
        jlist.setDropMode(DropMode.INSERT)
        jlist.setTransferHandler(new ListMoveTransferHandler())
        jlist.setVisibleRowCount(1)
        jlist.setLayoutOrientation(JList.HORIZONTAL_WRAP)

        var s_jlist = new ScrollPane(Component.wrap(jlist))

        var bp = new BoxPanel(Orientation.Vertical){

          contents += new Label(obj.name)
          contents += new Label(obj.data.size + " Elements")
          //border = Swing.EmptyBorder(30, 30, 10, 30)
        }

        var fp = new FlowPanel{

          contents += bp
          contents += s_jlist

        }

        contents += fp

      }
    }

    grid

  }

  def updateListData(list:JList, data:Datapool){

    var listModel = list.getModel().asInstanceOf[DefaultListModel]

    listModel.clear()

    var it = data.datapool.iterator

    while(it.hasNext){

      var data = it.next()

      if(data.url.endsWith(".jpg")){

        var img = data.asInstanceOf[Image]

        img.image.getImage().getScaledInstance(10,10,10)

        img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        listModel.addElement(img.image)

      }
      if(data.url.endsWith(".pdf")){

        var img = data.asInstanceOf[Document]

        img.image.getImage().getScaledInstance(10,10,10)

        img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        listModel.addElement(img.image)

      }
      if(data.url.endsWith(".mp4")){

        var img = data.asInstanceOf[Video]

        img.image.getImage().getScaledInstance(10,10,10)

        img.image.setImage(img.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

        listModel.addElement(img.image)

      }

    }

  }

  def updateListImage(list:JList, data:Datapool){

    var listModel = list.getModel().asInstanceOf[DefaultListModel]

    listModel.clear()

    var it = data.imagepool.iterator

    while(it.hasNext){

      var data = it.next()

      data.image.getImage().getScaledInstance(10,10,10)

      data.image.setImage(data.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

      listModel.addElement(data.image)

    }
  }

  def updateListDocument(list:JList, data:Datapool){

    var listModel = list.getModel().asInstanceOf[DefaultListModel]

    listModel.clear()

    var it = data.documentpool.iterator

    while(it.hasNext){

      var data = it.next()

      data.image.getImage().getScaledInstance(10,10,10)

      data.image.setImage(data.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

      listModel.addElement(data.image)

    }
  }

  def updateListVideo(list:JList, data:Datapool){

    var listModel = list.getModel().asInstanceOf[DefaultListModel]

    listModel.clear()

    var it = data.videopool.iterator

    while(it.hasNext){

      var data = it.next()

      data.image.getImage().getScaledInstance(10,10,10)

      data.image.setImage(data.image.getImage().getScaledInstance(100, 75, Image.SCALE_DEFAULT))

      listModel.addElement(data.image)

    }
  }


  def readFromFile(f:File) : Datapool={

    var sxbuilder = new SAXBuilder

    var doc = sxbuilder.build(f)

    var root = doc.getRootElement

    var database = new Datapool

    var children = root.getChildren()

    //   var array = new Array[Array[Int]](1000)(1000)
    //
    //   var m = new Array[Int](children.size())

    var i = 0

    var k = 0

    while(i < children.size()){

      var j=0

      var x = children.get(i)

      var elem = x.asInstanceOf[Element]

      var name = ""

      if(elem.getName()=="Group"){

        name = elem.getChild("name").getText()
        database.addToGrouppool(name)

        var child = elem.getChildren("Data")

        while(j < child.size()){

          var x = child.get(j)

          var kind = x.asInstanceOf[Element]

          var url = kind.getChild("URL").getText()

          database.getGroup(name).addData(url)
          j += 1

        }

      }
      else{

        name = elem.getChild("URL").getText()

        database.addToDataPool(name)

      }

      i+=1
    }

    database

  }

  def exportToXML(d: Datapool,s:String){

    var root = new Element("Datapool")

    var doc = new org.jdom.Document(root)

    var it = d.datapool.iterator

    while(it.hasNext){

      var data = it.next()

      var url = data.url
      root.addContent(new Element("Data")
        .addContent(new Element("URL")
        .addContent(url)))
    }

    var it2 = d.grouppool.iterator

    var i = d.datapool.size

    while(it2.hasNext){

      var grp = it2.next()

      var name = grp.name

      root.addContent(new Element("Group")
        .addContent(new Element("name")
        .addContent(name)))

      var children = root.getChildren()

      var x = children.get(i)

      var elem = x.asInstanceOf[Element]

      var set = grp.data

      var it3 = set.iterator

      while(it3.hasNext){

        var obj = it3.next()

        var url = obj.url
        elem.addContent(new Element("Data")
          .addContent(new Element("URL")
          .addContent(url)))

      }

      i += 1

    }

    var outputter = new XMLOutputter(Format.getPrettyFormat())

    var output = new FileOutputStream(s)

    outputter.output(doc,output)


  }

}
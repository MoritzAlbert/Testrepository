package sq_gui

import java.awt.event._
import extension.IconTextListCellRenderer
import swing._
import java.awt.Image
import java.io.File
import javax.swing.{DropMode, ImageIcon, DefaultListModel, JList}
import com.ebenius.ListMoveTransferHandler


trait Functions extends XML with UpdateFunctions {

  //Declarations

  val file = new File("test.xml")
  var database = readFromFile(file)

  var list = getJListFromDatabase(database)

  list.setDragEnabled(true)
  list.setDropMode(DropMode.INSERT)
  list.setTransferHandler(new ListMoveTransferHandler())

  list.addKeyListener(new KeyAdapter {
    override def keyPressed(evt:KeyEvent) {
      val key = evt.getKeyCode
      if (key == KeyEvent.VK_DELETE ){
        val x = Dialog.showConfirmation(null,"Wollen Sie die Datei wirklich löschen?","Question", Dialog.Options.YesNo, Dialog.Message.Question)
        println(x.toString)
        if(x.toString.equals("Ok")){
        val obj = list.getSelectedValue.asInstanceOf[ImageIcon]
        println("ImageIcon: "+obj.getDescription)
        val url = searchURL(obj.getDescription)
        println(url)
        database.removeFromPools(url)
        updateListData(list,database)
        updateListDocument(list_doc,database)
        updateListImage(list_image,database)
        updateListVideo(list_video,database)
        }
      }
    }
  })

  var scroll = ListRenderer(list)
  var list_image = getJListImageFromDatabase(database)

  list_image.setDragEnabled(true)
  list_image.setDropMode(DropMode.INSERT)
  list_image.setTransferHandler(new ListMoveTransferHandler())
  list_image.addKeyListener(new KeyAdapter {
    override def keyPressed(evt:KeyEvent) {
      val key = evt.getKeyCode
      if (key == KeyEvent.VK_DELETE ){
        val x = Dialog.showConfirmation(null,"Wollen Sie die Datei wirklich löschen?","Question", Dialog.Options.YesNo, Dialog.Message.Question)
        if(x.toString.equals("Ok")){
        val obj = list_image.getSelectedValue.asInstanceOf[ImageIcon]
        println("ImageIcon: "+obj.getDescription)
        val url = searchURL(obj.getDescription)
        println(url)
        database.removeFromPools(url)
        updateListData(list,database)
        updateListDocument(list_doc,database)
        updateListImage(list_image,database)
        updateListVideo(list_video,database)
        }
      }
    }
  })

  var scroll_image = ListRenderer(list_image)
  var list_doc = getJListDocumentFromDatabase(database)

  list_doc.setDragEnabled(true)
  list_doc.setDropMode(DropMode.INSERT)
  list_doc.setTransferHandler(new ListMoveTransferHandler())
  list_doc.addKeyListener(new KeyAdapter {
    override def keyPressed(evt:KeyEvent) {
      val x = Dialog.showConfirmation(null,"Wollen Sie die Datei wirklich löschen?","Question", Dialog.Options.YesNo, Dialog.Message.Question)
      if(x.toString.equals("Ok")){
      val key = evt.getKeyCode
      if (key == KeyEvent.VK_DELETE ){
        val obj = list_doc.getSelectedValue.asInstanceOf[ImageIcon]
        println("ImageIcon: "+obj.getDescription)
        val url = searchURL(obj.getDescription)
        println(url)
        database.removeFromPools(url)
        updateListData(list,database)
        updateListDocument(list_doc,database)
        updateListImage(list_image,database)
        updateListVideo(list_video,database)
      }
      }
    }
  })

  var scroll_doc = ListRenderer(list_doc)
  var list_video = getJListVideoFromDatabase(database)

  list_video.setDragEnabled(true)
  list_video.setDropMode(DropMode.INSERT)
  list_video.setTransferHandler(new ListMoveTransferHandler())
  list_video.addKeyListener(new KeyAdapter {
    override def keyPressed(evt:KeyEvent) {
      val x = Dialog.showConfirmation(null,"Wollen Sie die Datei wirklich löschen?","Question", Dialog.Options.YesNo, Dialog.Message.Question)
      if(x.toString.equals("Ok")){
      val key = evt.getKeyCode
      if (key == KeyEvent.VK_DELETE ){
        val obj = list_video.getSelectedValue.asInstanceOf[ImageIcon]
        println("ImageIcon: "+obj.getDescription)
        val url = searchURL(obj.getDescription)
        println(url)
        database.removeFromPools(url)
        updateListData(list,database)
        updateListDocument(list_doc,database)
        updateListImage(list_image,database)
        updateListVideo(list_video,database)
      }
      }
    }
  })

  var scroll_video = ListRenderer(list_video)
  var list_group = getJListFromGrouppool(database)

  //generating a JList from a database
  def getJListFromDatabase(data: Datapool): JList = {

    val model = new DefaultListModel() {
      val it = data.datapool.iterator
      while (it.hasNext) {
        val data = it.next()
        // Pictures
        if (data.url.endsWith(".jpg")) {
          val img = data.asInstanceOf[Image]
          img.image.getImage.getScaledInstance(10, 10, 10)
          img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
          this.addElement(img.image)
        }
        //Documtents
        if (data.url.endsWith(".pdf")) {
          val img = data.asInstanceOf[Document]
          img.image.getImage.getScaledInstance(10, 10, 10)
          img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
          this.addElement(img.image)
        }
        // Videos
        if (data.url.endsWith(".mp4")) {
          val img = data.asInstanceOf[Video]
          img.image.getImage.getScaledInstance(10, 10, 10)
          img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
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
        data.image.setImage(data.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
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
        data.image.setImage(data.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
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
        data.image.setImage(data.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
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

  def getJListFromGrouppool(s: Datapool): JList = {
    val model = new DefaultListModel {
      val list = s.sortGrouppool()
      val it = list.iterator
      while (it.hasNext) {
        val data = it.next()
        this.addElement(data.name)
      }
    }
    val list = new JList(model)
    list

  }

  //generating a JList of all groups in database
  def getJListFromGroup(s: Group): JList = {
    val model = new DefaultListModel() {
      val it = s.data.iterator
      while (it.hasNext) {
        val data = it.next()
        if (data.url.endsWith(".jpg")) {
          val img = data.asInstanceOf[Image]
          img.image.getImage.getScaledInstance(10, 10, 10)
          img.image.setImage(img.image.getImage.getScaledInstance(120, 120, Image.SCALE_DEFAULT))
          this.addElement(img.image)
        }
        if (data.url.endsWith(".pdf")) {
          val img = data.asInstanceOf[Document]
          img.image.getImage.getScaledInstance(10, 10, 10)
          img.image.setImage(img.image.getImage.getScaledInstance(120, 120, Image.SCALE_DEFAULT))
          this.addElement(img.image)
        }
        if (data.url.endsWith(".mp4")) {
          val img = data.asInstanceOf[Video]
          img.image.getImage.getScaledInstance(10, 10, 10)
          img.image.setImage(img.image.getImage.getScaledInstance(120, 120, Image.SCALE_DEFAULT))
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

  def searchURL(s:String) : String={
    var url = ""
    val it = database.datapool.iterator
    while(it.hasNext){
      val obj = it.next()
      val u = obj.url.split("/")
      if (u.last.equals(s)){
         url = obj.url
      }
    }
    url
  }
  
}

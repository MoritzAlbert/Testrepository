package sq_gui

import scala.collection.mutable.HashSet

class Datapool() {

  var datapool = new HashSet[Data]()
  var imagepool = new HashSet[Image]()
  var videopool = new HashSet[Video]()
  var documentpool = new HashSet[Document]()
  var grouppool = new HashSet[Group]()
  val searchPool = new HashSet[Data]()

  def addToDataPool(s: String) {

    val file = new java.io.File(s)

    if (file.getName.endsWith(".jpg")) {
      val img = new Image(s)
      datapool.add(img)
      imagepool.add(img)
      img.id = datapool.size
    }

    if (file.getName.endsWith(".mp4")) {
      val vid = new Video(s)
      datapool.add(vid)
      videopool.add(vid)
      vid.id = datapool.size

    }

    if (file.getName.endsWith(".pdf")) {
      val doc = new Document(s)
      datapool.add(doc)
      documentpool.add(doc)
      doc.id = datapool.size
    }

  }

  def addToDataPool(s: Set[String]) {

    for (args <- s) {

      val src = new java.io.File(args)

      if (src.getName.endsWith(".jpg")) {
        val img = new Image(args)
        datapool.add(img)
        imagepool.add(img)
        img.id = datapool.size
      }

      if (src.getName.endsWith(".mp4")) {
        val vid = new Video(args)
        datapool.add(vid)
        videopool.add(vid)
        vid.id = datapool.size
      }

      if (src.getName.endsWith(".pdf")) {
        val doc = new Document(args)
        datapool.add(doc)
        documentpool.add(doc)
        doc.id = datapool.size
      }

    }
  }

  def addToDataPoolI(s: HashSet[Image]) {
    for (args <- s) this.addToDataPool(args.url)
  }

  def addToDataPoolD(s: HashSet[Document]) {
    for (args <- s) this.addToDataPool(args.url)
  }

  def addToDataPoolV(s: HashSet[Video]) {
    for (args <- s) this.addToDataPool(args.url)
  }

  def getDataByString(s: String): Data = {
    var id = new Data("")
    val it = datapool.iterator
    while (it.hasNext) {
      val data = it.next()

      if (data.url.equals(s)) {
        id = data
      }
    }

    id

  }

  def removeFromPools(s: String) {
    val data = getDataByString(s)
    val src = new java.io.File(s)

    datapool.remove(data)
    if (src.getName.endsWith(".jpg")) {


      imagepool.remove(data.asInstanceOf[Image])

    }

    if (src.getName.endsWith(".mp4")) {
      videopool.remove(data.asInstanceOf[Video])
    }

    if (src.getName.endsWith(".pdf")) {
      documentpool.remove(data.asInstanceOf[Document])
    }

  }

  def addToGrouppool(s: String) {

    val grp = new Group(s)
    grouppool += grp
    grp.id = grouppool.size

  }

  def addToGrouppool(s: Group) {

    grouppool += s
    s.id = grouppool.size

  }

  def getGroupByString(s: String): Group = {

    var i = 0
    var id = new Group("")
    val it = grouppool.iterator

    while (it.hasNext) {
      val grp = it.next()
      if (grp.name.equals(s)) id = grp
      else i += 1
    }
    id
  }

  def getGroupByID(s: Int): Group = {

    var i = 0
    var id = new Group("")
    val it = grouppool.iterator

    while (it.hasNext) {
      val grp = it.next()
      if (grp.id.equals(s)) id = grp
      else i += 1
    }
    id
  }

  def removeFromGrouppool(s: String) {

    val grp = getGroupByString(s)
    println("Gruppenname:" +  grp.name + "Stringname:" + s)
    val b = grouppool.remove(grp)
    println(b)

  }

  def sortGrouppool(): List[Group] = {
    val list = grouppool.toList.sortWith((e1, e2) => (e1.name.toLowerCase< e2.name.toLowerCase))
    list
  }

  def sortDatapool(): List[Data] = {
    val list = datapool.toList.sortWith((e1, e2) => (e1.getName.toLowerCase< e2.getName.toLowerCase))
    list
  }

  def sortImagepool(): List[Image] = {
    val list = imagepool.toList.sortWith((e1, e2) => (e1.getName.toLowerCase< e2.getName.toLowerCase))
    list
  }

  def sortVideopool(): List[Video] = {
    val list = videopool.toList.sortWith((e1, e2) => (e1.getName.toLowerCase< e2.getName.toLowerCase))
    list
  }

  def sortDocumentpool(): List[Document] = {
    val list = documentpool.toList.sortWith((e1, e2) => (e1.getName.toLowerCase< e2.getName.toLowerCase))
    list
  }
}
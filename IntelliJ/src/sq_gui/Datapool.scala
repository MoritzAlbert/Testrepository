package sq_gui

import scala.collection.mutable.HashSet

//Begin class Datapool

class Datapool() {
  var datapool = new HashSet[Data]()
  var imagepool = new HashSet[Image]()
  var videopool = new HashSet[Video]()
  var documentpool = new HashSet[Document]()
  var grouppool = new HashSet[Group]()

  //methods

  //adding a file to datapool
  def addToDataPool(s: String) {
    val file = new java.io.File(s)
    if (file.getName.endsWith(".jpg")) {
      val img = new Image(s)
      datapool.add(img)
      imagepool.add(img)
    }

    if (file.getName.endsWith(".mp4")) {
      val vid = new Video(s)
      datapool.add(vid)
      videopool.add(vid)
    }

    if (file.getName.endsWith(".pdf")) {
      val doc = new Document(s)
      datapool.add(doc)
      documentpool.add(doc)
    }
  }

  //adding a set to datapool
  def addToDataPool(s: Set[String]) {
    for (args <- s) {
      val src = new java.io.File(args)
      if (src.getName.endsWith(".jpg")) {
        val img = new Image(args)
        datapool.add(img)
        imagepool.add(img)
      }

      if (src.getName.endsWith(".mp4")) {
        val vid = new Video(args)
        datapool.add(vid)
        videopool.add(vid)
      }

      if (src.getName.endsWith(".pdf")) {
        val doc = new Document(args)
        datapool.add(doc)
        documentpool.add(doc)
      }
    }
  }

  //adding a hashset of images to datapool
  def addToDataPoolI(s: HashSet[Image]) {
    for (args <- s) this.addToDataPool(args.url)
  }

  //adding a hashset of pdfs to datapool
  def addToDataPoolD(s: HashSet[Document]) {
    for (args <- s) this.addToDataPool(args.url)
  }

  //adding a hashset of videos to datapool
  def addToDataPoolV(s: HashSet[Video]) {
    for (args <- s) this.addToDataPool(args.url)
  }

  //getData
  def getData(s: String): Data = {
    var i = 0
    var id = new Data("")
    val it = datapool.iterator
    while (it.hasNext) {
      val data = it.next()
      if (data.url.equals(s)) {
        id = data
      }
      else i += 1
    }
    id
  }


  //remove a file from datapool
  def removeFromPools(s: String) {
    val data = getData(s)
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

  //adding a group to grouppool
  def addToGrouppool(s: String) {
    val grp = new Group(s)
    grouppool += grp
  }

  //adding a group to grouppool
  def addToGrouppool(s: Group) {
    grouppool += s
  }

  //getGroup
  def getGroup(s: String): Group = {
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

  //remove a group from grouppool
  def removeFromGrouppool(s: String) {
    val grp = getGroup(s)
    grouppool.remove(grp)
  }
}
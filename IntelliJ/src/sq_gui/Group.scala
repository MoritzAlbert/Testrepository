package sq_gui

import java.util.ArrayList

//Begin class Group

class Group(s: String) {

  //declarations

  var id = 0
  var name = s
  //var data = HashSet[Data]()

  var data_id = 0

  var data = new ArrayList[Data]()
  //add a new group to data
  def addData(obj: Data){data.add(obj)}

  def addData(s: String) {
    val file = new java.io.File(s)
    if (file.getName.endsWith(".jpg")) {
      val img = new Image(s)
      data.add(img)
    }

    if (file.getName.endsWith(".mp4")) {
      val vid = new Video(s)
      data.add(vid)
    }

    if (file.getName.endsWith(".pdf")) {
      val doc = new Document(s)
      data.add(doc)
    }
  }

  def addData(i:Int, s: String) {
    val file = new java.io.File(s)
    if (file.getName.endsWith(".jpg")) {
      val img = new Image(s)
      data.add(i,img)
    }

    if (file.getName.endsWith(".mp4")) {
      val vid = new Video(s)
      data.add(i,vid)
    }

    if (file.getName.endsWith(".pdf")) {
      val doc = new Document(s)
      data.add(i,doc)
    }
  }
  //deleting an object from data
  def removeData(obj: Data) {
    data.remove(obj)
    // val bool = data.remove(obj)
    //if(bool == true) Dialog.showMessage(null, "Datei wurde gelscht!!!")
    //else Dialog.showMessage(null, "Datei wurde nicht gelscht!!!")
  }


}
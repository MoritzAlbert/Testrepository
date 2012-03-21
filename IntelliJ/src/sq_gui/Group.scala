package sq_gui

import scala.collection.mutable.Set

//Begin class Group

class Group(s: String) {

  //declarations

  var name = s

  var data = Set[Data]()

  var playlist = Set[Data]()

  var data_id = 0

  //methods

  //add a new group to data
  def addData(obj: Data) = data += obj

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

  //deleting an object from data
  def removeData(obj: Data) {
    data.remove(obj)
    // val bool = data.remove(obj)
    //if(bool == true) Dialog.showMessage(null, "Datei wurde gelscht!!!")
    //else Dialog.showMessage(null, "Datei wurde nicht gelscht!!!")
  }

  //playing all elements of a group
  def playGroup() {

    for (arg <- data) arg.play

  }

  //adding a new set to a playlist
  def addSetToPlaylist(s: Set[Data]) {

    for (arg <- s) playlist += arg

  }

  //adding a new object to a playlist
  def addDataToPlaylist(s: Data) = playlist += s

  //playing a playlist
  def playPlaylist() {
    for (arg <- playlist) arg.play
  }


}
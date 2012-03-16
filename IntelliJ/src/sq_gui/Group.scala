package sq_gui

import scala.collection.mutable.Set

class Group(s: String) {

  var name = s

  var data = Set[Data]()

  var playlist = Set[Data]()

  var data_id = 0

  //fgt der Gruppe eine neue Datei hinzu
  def addData(obj: Data) = data += obj

  def addData(s:String){

    val file = new java.io.File(s)

    if(file.getName.endsWith(".jpg")) {

      val img = new Image(s)
      data.add(img)

    }

    if(file.getName.endsWith(".mp4")) {

      val vid = new Video(s)
      data.add(vid)

    }

    if(file.getName.endsWith(".pdf")) {

      val doc = new Document(s)
      data.add(doc)

    }

  }

  //lscht Objekt aus Set
  def removeData(obj:Data){
    data.remove(obj)
    // val bool = data.remove(obj)
    //if(bool == true) Dialog.showMessage(null, "Datei wurde gelscht!!!")
    //else Dialog.showMessage(null, "Datei wurde nicht gelscht!!!")
  }

  //Spielt alle Dateien ab
  def playGroup(){

    for(arg <- data) arg.play

  }

  //fgt ein neues Set in playlist ein
  def addSetToPlaylist(s:Set[Data]) {

    for(arg <- s) playlist += arg

  }

  //fgt ein Objekt in playlist ein
  def addDataToPlaylist(s:Data) = playlist +=s

  //spielt Playliste ab
  def playPlaylist(){
    for(arg <- playlist) arg.play
  }



}
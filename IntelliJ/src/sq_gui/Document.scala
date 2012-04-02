package sq_gui

import java.io._
import javax.swing.ImageIcon

//Begin class Document
class Document(s: String) extends Data(s) {

  //declarations
  this.url = s
  val file = new File(s)
  var default = "pdf_logo.jpg"
  var image = new ImageIcon(default, getName)
  var player = "C:/Program Files (x86)/Adobe/Acrobat 10.0/Acrobat/Acrobat.exe"

  //methods

  //overwrites the play-function of video
  override def play {
    //Desktop.getDesktop().open(file)
    //oder
    //java.lang.Runtime.getRuntime().exec(player,null,file)
    //oder
    val sb = new StringBuilder(player)
    sb.append(' ')
    sb.append(url)

    Runtime.getRuntime.exec(sb.toString());
  }

  //new player
  def newPlayer(s: String) {
    player = s
  }

  //getName
  def getName: String = {
    val s = url.split("/")
    val name = s.last
    name
  }
}
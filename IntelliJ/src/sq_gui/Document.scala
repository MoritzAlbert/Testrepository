package sq_gui

import java.io._
import javax.swing.ImageIcon

class Document(s: String) extends Data(s) {

  this.url = s

  val file = new File(s)

  var default = "D:/eclipse-jee-indigo-SR1-win32-x86_64/eclipse/workspace/SQ_GUI/pdf_logo.jpg"

  var image = new ImageIcon(default,getName)

  var player = "C:/Program Files (x86)/Adobe/Acrobat 10.0/Acrobat/Acrobat.exe"

  //berschreibt play-Fkt von Data
  override def play{
    //Desktop.getDesktop().open(file)
    //oder
    //java.lang.Runtime.getRuntime().exec(player,null,file)
    //oder
    val sb = new StringBuilder(player)
    sb.append(' ')
    sb.append(url)

    Runtime.getRuntime.exec(sb.toString());
  }

  //neuer Player
  def newPlayer(s:String) {
    player = s
  }

  def getName : String = {

    val s = url.split("/")

    val name = s.last

    name

  }

}
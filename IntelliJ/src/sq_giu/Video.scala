package sq_gui

import java.io._
import java.awt.Desktop
import javax.swing.ImageIcon

class Video(s: String) extends Data(s) {

  this.url = s

  var default = "D:/eclipse-jee-indigo-SR1-win32-x86_64/eclipse/workspace/SQ_GUI/video.jpg"

  val file = new File(s)

  var image = new ImageIcon(default,getName)

  var player = "C:/Program Files (x86)/Windows Media Player/wmplayer.exe"

  //berschreibt play-Fkt von Data
  override def play{
    //Desktop.getDesktop().open(file)
    //oder
    //java.lang.Runtime.getRuntime().exec(player,null,file)
    //oder
    var sb = new StringBuilder(player);
    sb.append(' ');
    sb.append(url);

    Runtime.getRuntime().exec(sb.toString());
  }

  //neuer Player
  def newPlayer(s:String) = player = s

  def getName : String = {

    var s = url.split("/")

    var name = s.last

    name

  }


}
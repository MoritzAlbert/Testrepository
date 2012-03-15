package sq_gui

import java.io._
import java.awt.Desktop
import javax.swing.ImageIcon

class Image(s: String) extends Data(s) {

  this.url = s

  val file = new File(s)

  var image = new ImageIcon(s,getName)

  var player = "C:/Program Files (x86)/GIMP-2.0/bin/gimp-2.6.exe"

  //berschreibt play-Fkt von Data
  override def play{
    //Desktop.getDesktop().open(file)
    //oder
    //java.lang.Runtime.getRuntime().exec(player)
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
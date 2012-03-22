package sq_gui

import java.io._
import javax.swing.ImageIcon

//Begin class Video

class Image(s: String) extends Data(s) {

  //declarations
  this.url = s
  val file = new File(s)
  var image = new ImageIcon(s, getName)
  var player = "C:/Program Files (x86)/GIMP-2.0/bin/gimp-2.6.exe"
  //methods

  //overwrites the play-function of data
  override def play {
    //Desktop.getDesktop().open(file)
    //oder
    //java.lang.Runtime.getRuntime().exec(player)
    //oder

    val sb = new StringBuilder(player);
    sb.append(' ');
    sb.append(url);

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
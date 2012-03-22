package sq_gui

import java.io._
import javax.swing.ImageIcon

//Begin class Video

class Video(s: String) extends Data(s) {

  //declarations
  this.url = s
  var default = "D:/eclipse-jee-indigo-SR1-win32-x86_64/eclipse/workspace/SQ_GUI/video.jpg"
  val file = new File(s)
  var image = new ImageIcon(default, getName)
  var player = "C:/Program Files (x86)/Windows Media Player/wmplayer.exe"
  //methods

  //overwrites the play-function of video
  override def play {
    //Desktop.getDesktop().open(file)
    //oder
    //java.lang.Runtime.getRuntime().exec(player,null,file)
    //oder
    val sb = new StringBuilder(player);
    sb.append(' ');
    sb.append(url);

    Runtime.getRuntime.exec(sb.toString());
  }

  //new Player
  def newPlayer(s: String) {
    player = s
  }

  //get name
  def getName: String = {
    val s = url.split("/")
    val name = s.last
    name
  }
}
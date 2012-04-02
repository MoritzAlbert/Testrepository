package sq_gui

import java.io._
import javax.swing.ImageIcon

//Begin class Video

class Video(s: String) extends Data(s) {

  //declarations
  this.url = s
  var default = "video.jpg"
  val file = new File(s)
  var image = new ImageIcon(default, getName)
 this.name = getName
  this.player = "C:/Program Files (x86)/Windows Media Player/wmplayer.exe"
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

}
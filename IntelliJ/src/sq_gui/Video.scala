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

  override def getName: String ={
    val s = url.split('\\')
    val name = s.last
    name
  }

}
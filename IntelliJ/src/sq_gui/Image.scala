package sq_gui

import java.io._
import javax.swing.ImageIcon

//Begin class Video

class Image(s: String) extends Data(s){

  //declarations
  this.url = s
  val file = new File(s)
  var image = new ImageIcon(s, getName)
  //methods
  override def getName: String ={
    val s = url.split('\\')
    val name = s.last
    name
  }

}
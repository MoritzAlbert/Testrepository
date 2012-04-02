package sq_gui

import java.io._
import javax.swing.ImageIcon

//Begin class Video

class Image(s: String) extends Data(s){

  //declarations
  this.url = s
  val file = new File(s)
  var image = new ImageIcon(s, getName)
  this.name = getName
  //methods


}
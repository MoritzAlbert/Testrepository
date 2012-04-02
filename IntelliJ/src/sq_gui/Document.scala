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

  override def getName: String ={
    val s = url.split('\\')
    val name = s.last
    name
  }

}
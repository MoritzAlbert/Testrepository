package sq_gui

import scala.collection.mutable.HashSet

class Data(s:String) {

  var id = 0
  var url = ""
  var relation = HashSet[Data]()
  var rating = 0
  var description = ""
  var name = getName
  var player = ""

  //adds a relation to an object
  def addRelation(obj:Data) = relation += obj

  //rating
  def addRating(a:Int) {
    rating = a
  }

  //Description of the Image
  def addDescription(b:String){
    description = b
  }

  def play(){}

  def getName: String ={
    val s = url.split("/")
    val name = s.last
    name
  }

}
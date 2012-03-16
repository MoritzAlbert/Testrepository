package sq_gui

import scala.collection.mutable.HashSet

class Data(s:String) {

  var url = ""
  var relation = HashSet[Data]()
  var rating = 0

  //fgt ein relations-objekt in das HashSet
  def addRelation(obj:Data) = relation += obj

  //setzt das rating
  def addRating(a:Int) {
    rating = a
  }

  //keine Ahnung warum das hier ntig ist!!!
  def play(){}


}
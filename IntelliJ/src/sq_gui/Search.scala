package sq_gui

import collection.mutable.HashSet

/**
 * Created by IntelliJ IDEA.
 * User: Julian Rapp
 * Date: 30.03.12
 * Time: 08:55
 * To change this template use File | Settings | File Templates.
 */

trait Search extends Functions {

  //     search for String

  // manipulate view/list of datapool

  // if searchString == empty  > alle bilder werden angezeigt


  /*
public int binaereSuche(int[] feld, int key) {
int u = 0, o = feld.length - 1;
while (u <= o) {
  int m = (u + o) / 2;
  if (feld[m] == key)
    return m;
  else if (feld[m] > key)
    o = m - 1;
  else // feld[m] < key
    u = m + 1;
}
return -1;
}    */


  def startSearch(searchString: String) {

    // DATENBANK durchlaufen       ok
    // JEDES DATENBANKELEMENT      ok
    // SEQUENTIELL NACH TEXT SUCHEN
    // FALLS ELEMENT GEFUNDEN IN NEUE LISTE
    // NEUE LISTE AM ENDE IN VIEW LADEN

    // IF Suchfeld-leer =   alle wieder anzeigen


    val searchPool = new HashSet[Data]()
    val index = database.datapool.iterator
    while (index.hasNext) {
      val dataFromIndex = index.next()
      // Schnelle Suche
      if (dataFromIndex.url.split("/").last.split("[.]").head == searchString) {
        // ADD FILE TO POOL
                searchPool.add(dataFromIndex)
        println(searchPool.size)
      }
        //alternative suche
      else
      {

      }



      // searchPool.add(dataFromIndex)


    }

    //println(searchPool.size)


  }

}

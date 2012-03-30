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
    // SEQUENTIELL NACH TEXT SUCHEN       ok
    // FALLS ELEMENT GEFUNDEN IN NEUE LISTE       ok
    // NEUE LISTE AM ENDE IN VIEW LADEN

    // IF Suchfeld-leer =   alle wieder anzeigen

    if (searchString.isEmpty)     {
      // TODO Datenpool gesamt laden!
    }

    database.searchPool.clear()

    val index = database.datapool.iterator
    while (index.hasNext) {
      val dataFromIndex = index.next()
      // Schnelle Suche
      if (dataFromIndex.url.split("/").last.split("[.]").head.toLowerCase.contains(searchString.toLowerCase)) {
        // ADD FILE TO POOL
       database.searchPool.add(dataFromIndex)
      }
    }
    //TODO Panel View Aktualisieren

    println(searchString + " = " + database.searchPool.size)
  }

}

package sq_gui

trait Search extends Functions {

  def startSearch(searchString: String) {

    database.searchPool.clear()

    val index = database.datapool.iterator
    while (index.hasNext) {
      val dataFromIndex = index.next()

      // quick search
      if (dataFromIndex.url.split("/").last.split("[.]").head.toLowerCase.contains(searchString.toLowerCase)) {

        // collect all located files which come to consideration
        database.searchPool.add(dataFromIndex)
      }
    }

    println(searchString + " = " + database.searchPool.size)
  }

}

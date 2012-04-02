package sq_gui

import javax.swing._
import java.awt._

trait UpdateFunctions {

  //update methods

  //updating listData
  def updateListData(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.datapool.iterator
    while (it.hasNext) {
      val data = it.next()
      if (data.url.endsWith(".jpg")) {
        val img = data.asInstanceOf[sq_gui.Image]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".pdf")) {
        val img = data.asInstanceOf[sq_gui.Document]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".mp4")) {
        val img = data.asInstanceOf[sq_gui.Video]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
        listModel.addElement(img.image) //TODO richtig dass img.image?   nicht video.image?
      }
    }
  }

  //updating searchlist
  def updateSearchListData(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.searchPool.iterator
    while (it.hasNext) {
      val data = it.next()
      if (data.url.endsWith(".jpg")) {
        val img = data.asInstanceOf[sq_gui.Image]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".pdf")) {
        val img = data.asInstanceOf[sq_gui.Document]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".mp4")) {
        val img = data.asInstanceOf[sq_gui.Video]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
        listModel.addElement(img.image) //TODO richtig dass img.image?   nicht video.image?
      }
    }
  }

  //updating listImage
  def updateListImage(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.imagepool.iterator
    while (it.hasNext) {
      val data = it.next()
      data.image.getImage.getScaledInstance(10, 10, 10)
      data.image.setImage(data.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
      listModel.addElement(data.image)
    }
  }

  //updating listDocument
  def updateListDocument(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.documentpool.iterator
    while (it.hasNext) {
      val data = it.next()
      data.image.getImage.getScaledInstance(10, 10, 10)
      data.image.setImage(data.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
      listModel.addElement(data.image)
    }
  }

  //updating listVideo
  def updateListVideo(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.videopool.iterator
    while (it.hasNext) {
      val data = it.next()
      data.image.getImage.getScaledInstance(10, 10, 10)
      data.image.setImage(data.image.getImage.getScaledInstance(40, 40, Image.SCALE_DEFAULT))
      listModel.addElement(data.image)
    }
  }

  //updating listGroup
  def updateListGroup(list: JList, data: Datapool) {

    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.grouppool.iterator

    while (it.hasNext) {
      val obj = it.next()
        /*
          val list2 = getJListFromGroup(obj)
          val listModel2 = list2.getModel.asInstanceOf[DefaultListModel]
          listModel2.clear()
          val it2 = obj.data.iterator

          while(it2.hasNext){
          val obj2 = it2.next()

          if(obj2.url.endsWith(".jpg")){
               val img = obj2.asInstanceOf[Image]
              img.image.getImage.getScaledInstance(10,10,10)
              img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))
             listModel.addElement(img.image)

              }
            if(obj2.url.endsWith(".pdf")){
                          val img = obj2.asInstanceOf[Document]
                            img.image.getImage.getScaledInstance(10,10,10)
                                  img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))
                                listModel.addElement(img.image)
                                                    }

          if(obj2.url.endsWith(".mp4")){
          val img = obj2.asInstanceOf[Video]
          img.image.getImage.getScaledInstance(10,10,10)
          img.image.setImage(img.image.getImage.getScaledInstance(100, 75, Image.SCALE_DEFAULT))
          listModel.addElement(img.image)
            }
           }*/

      listModel.addElement(obj.name)

    }
  }

}

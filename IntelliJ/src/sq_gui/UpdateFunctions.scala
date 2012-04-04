package sq_gui

import javax.swing._
import java.awt._

trait UpdateFunctions {

  //update methods

  //updating listData
  def updateListData(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.sortDatapool().iterator
    while (it.hasNext) {
      val data = it.next()
      if (data.url.endsWith(".jpg")) {
        val img = data.asInstanceOf[sq_gui.Image]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(70, 70, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".pdf")) {
        val img = data.asInstanceOf[sq_gui.Document]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".mp4")) {
        val img = data.asInstanceOf[sq_gui.Video]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
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
        img.image.setImage(img.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".pdf")) {
        val img = data.asInstanceOf[sq_gui.Document]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
        listModel.addElement(img.image)
      }
      if (data.url.endsWith(".mp4")) {
        val img = data.asInstanceOf[sq_gui.Video]
        img.image.getImage.getScaledInstance(10, 10, 10)
        img.image.setImage(img.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
        listModel.addElement(img.image) //TODO richtig dass img.image?   nicht video.image?
      }
    }
  }

  //updating listImage
  def updateListImage(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.sortImagepool().iterator
    while (it.hasNext) {
      val data = it.next()
      data.image.getImage.getScaledInstance(10, 10, 10)
      data.image.setImage(data.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
      listModel.addElement(data.image)
    }
  }

  //updating listDocument
  def updateListDocument(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.sortDocumentpool().iterator
    while (it.hasNext) {
      val data = it.next()
      data.image.getImage.getScaledInstance(10, 10, 10)
      data.image.setImage(data.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
      listModel.addElement(data.image)
    }
  }

  //updating listVideo
  def updateListVideo(list: JList, data: Datapool) {
    val listModel = list.getModel.asInstanceOf[DefaultListModel]
    listModel.clear()
    val it = data.sortVideopool().iterator
    while (it.hasNext) {
      val data = it.next()
      data.image.getImage.getScaledInstance(10, 10, 10)
      data.image.setImage(data.image.getImage.getScaledInstance(65, 65, Image.SCALE_DEFAULT))
      listModel.addElement(data.image)
    }
  }

}

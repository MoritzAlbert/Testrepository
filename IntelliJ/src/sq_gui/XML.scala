package sq_gui

import org.jdom.input.SAXBuilder
import org.jdom.Element
import org.jdom.output.{Format, XMLOutputter}
import java.io.{FileOutputStream, File}

trait XML {

  //reading method

  //read from file
  def readFromFile(f: File): Datapool = {
    val sAXBuilder = new SAXBuilder
    val doc = sAXBuilder.build(f)
    val root = doc.getRootElement
    val database = new Datapool
    val children = root.getChildren

    //   var array = new Array[Array[Int]](1000)(1000)
    //
    //   var m = new Array[Int](children.size())

    var i = 0

    while (i < children.size()) {
      var j = 0
      val x = children.get(i)
      val elem = x.asInstanceOf[Element]
      var name = ""

      if (elem.getName == "Group") {
        //Geändert!!!
        name = elem.getChild("name").getText
        val grp = new Group(name)
        val id = elem.getAttribute("id").getIntValue

        grp.id = id
        database.addToGrouppool(grp)

        val child = elem.getChildren("Data")

        while (j < child.size) {
          val x = child.get(j)
          val kind = x.asInstanceOf[Element]
          val url = kind.getChild("URL").getText
          database.getGroupByString(name).addData(url)
          j += 1
        }
      }
      else {

        name = elem.getChild("URL").getText
        //Geändert
        //val id = elem.getAttribute("id").getName.asInstanceOf[Int]
        database.addToDataPool(name)
        //database.datapool.last.id = id

      }
      i += 1
    }
    database
  }


  //export method

  //export to XML


  def exportToXML(d: Datapool, s: String) {
    val root = new Element("Datapool")
    val doc = new org.jdom.Document(root)
    val it = d.datapool.iterator

    while (it.hasNext) {
      val data = it.next()
      val url = data.url
      root.addContent(new Element("Data")
        .setAttribute("id", data.id.toString)
        .addContent(new Element("URL")
        .addContent(url)))
    }

    val it2 = d.grouppool.iterator
    var i = d.datapool.size

    while (it2.hasNext) {
      val grp = it2.next()
      val name = grp.name

      root.addContent(new Element("Group")
        .setAttribute("id", grp.id.toString)
        .addContent(new Element("name")
        .addContent(name)))

      val children = root.getChildren
      val x = children.get(i)
      val elem = x.asInstanceOf[Element]
      val set = grp.data
      val it3 = set.iterator

      while (it3.hasNext) {
        val obj = it3.next()
        val url = obj.url
        elem.addContent(new Element("Data")
          .setAttribute("id", obj.id.toString)
          .addContent(new Element("URL")
          .addContent(url)))
        /*
          elem.addContent(new Element("Data")
          .setAttribute("id",obj.id.toString)

        */
      }
      i += 1
    }

    val xml = new XMLOutputter(Format.getPrettyFormat)

    val output = new FileOutputStream(s)

    xml.output(doc, output)
  }


}

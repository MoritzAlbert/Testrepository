package sq_gui

import org.jdom.input.SAXBuilder
import org.jdom.Element
import org.jdom.output.{Format, XMLOutputter}
import java.io.{FileOutputStream, File}
import scala.xml._
import java.util.{ArrayList}

trait XML {

  //read from file
  def readFromFile(f: File): Datapool = {
    val sAXBuilder = new SAXBuilder
    val doc = sAXBuilder.build(f)
    val root = doc.getRootElement
    val database = new Datapool
    val children = root.getChildren

    var i = 0

    while (i < children.size()) {
      var j = 0
      val x = children.get(i)
      val elem = x.asInstanceOf[Element]
      var name = ""
      var rating = ""
      var info = ""

      //GRUPPE!
      if (elem.getName == "Group") {
        //Geändert!!!
        name = elem.getChild("name").getText
        rating = elem.getChild("rating").getText
        info = elem.getChild("info").getText
        val grp = new Group(name)

        database.addToGrouppool(grp)

        val obj = database.getGroupByString(name)
        obj.info = info
        obj.rating = rating

        val child = elem.getChildren("Data")

        while (j < child.size) {
          val x = child.get(j)
          val kind = x.asInstanceOf[Element]
          val url = kind.getChild("URL").getText
          database.getGroupByString(name).addData(url)
          j += 1
        }
      } //GRUPPE ENDE
      else {

        name = elem.getChild("URL").getText
        //rate = elem.getChild("RATING").getText
        //desc = elem.getChild("DESCRIPTION").getText


        //Geändert
        //val id = elem.getAttribute("id").getName.asInstanceOf[Int]
        //database.addToDataPool(url, rate, desc)

        database.addToDataPool(name)
        //database.datapool.last.id = id

      }
      i += 1
    }
    database
  }

  //export to XML
  def exportToXML(d: Datapool, s: String) {
    val root = new Element("Datapool")
    val doc = new org.jdom.Document(root)
    val it = d.datapool.iterator

    while (it.hasNext) {
      val data = it.next()
      val url: String = data.url
      val rate = data.rating
      val desc: String = data.description
      root.addContent(new Element("Data")
        .setAttribute("id", data.id.toString)
        .addContent(new Element("URL").addContent(url))
        .addContent(new Element("RATING").addContent(rate))
        .addContent(new Element("DESCRIPTION").addContent(desc))
      )
    }

    val it2 = d.grouppool.iterator
    var i = d.datapool.size

    while (it2.hasNext) {
      val grp = it2.next()
      val name = grp.name
      val rating = grp.rating
      val info = grp.info

      root.addContent(new Element("Group")
        .setAttribute("id", grp.id.toString)
        .addContent(new Element("name").addContent(name))
        .addContent(new Element("rating").addContent(rating))
        .addContent(new Element("info").addContent(info))
      )

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
          .addContent(new Element("URL").addContent(url)))
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

  def exportPlayerPreferencesToXML(filename: String) {

    val root = new Element("PREFERENCES")
    val doc = new org.jdom.Document(root)

    root.addContent(new Element("playerImage").addContent(new Element("File").addContent(Gui.playerImage))
    )

    root.addContent(new Element("playerVideo").addContent(new Element("File").addContent(Gui.playerVideo))
    )

    root.addContent(new Element("playerPDF").addContent(new Element("File").addContent(Gui.playerPDF))
    )

    val xml = new XMLOutputter(Format.getPrettyFormat)
    val output = new FileOutputStream(filename)

    xml.output(doc, output)
  }


  def importPlayerPreferences(filename: File): ArrayList[String] = {

    val sAXBuilder = new SAXBuilder
    val doc = sAXBuilder.build(filename)
    val root = doc.getRootElement
    val children = root.getChildren

    val temp = new ArrayList[String]()
    var i = 0

    while (i < children.size()) {
      val x = children.get(i)
      val elem = x.asInstanceOf[Element]

      if (elem.getName == "playerImage") {
        temp.add(elem.getChild("File").getText)
      }
      if (elem.getName == "playerVideo") {
        temp.add(elem.getChild("File").getText)
      }
      if (elem.getName == "playerPDF") {
        temp.add(elem.getChild("File").getText)
      }
      i += 1
    }

    temp
  }
}

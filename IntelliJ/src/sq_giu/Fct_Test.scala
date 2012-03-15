package sq_gui
import java.io.File

object Fct_Test {

  def main ( args : Array [ String ]) {

    var video = "C:/Users/Dave/Desktop/0004.mp4"
    var img = "C:/Users/Dave/Desktop/lustig.jpg"
    var doc = "doc.pdf"

    var data = new Datapool()

    data.addToDataPool(video)
    data.addToDataPool(img)
    data.addToDataPool(doc)

    println("Datenpoolgröße: "+data.datapool.size)
    println("Imagepoolgröße: "+data.imagepool.size)
    println("Videopoolgröße: "+data.videopool.size)
    println("Documentpoolgröße: "+data.documentpool.size)

    var grp = new Group("Test")


    grp.addData(img)
    grp.addData(video)


    data.addToGrouppool(grp)
    println("Gruppengröße: "+grp.data.size)


    //var play = "C:\\Program Files (x86)\\GIMP-2.0\\bin\\gimp-2.6.exe"

    //image.newPlayer(play)

    //image.play

    Gui.exportToXML(data,"Test.xml")

    var file = new File("Test.xml")

    var data2 = new Datapool
    data2 = Gui.readFromFile(file)

    println("Datenpoolgröße Import: "+data2.datapool.size)
    println("Imagepoolgröße Import: "+data2.imagepool.size)
    println("Videopoolgröße Import: "+data2.videopool.size)
    println("Documentpoolgröße Import: "+data2.documentpool.size)
    println("Gruppengröße Import: "+grp.data.size)


  }
}
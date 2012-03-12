import sbt._
import Keys._

object ProductionBuild extends Build {
  //Projects
  lazy val application    = Project ( id = "application", 	base = file( "./"))
}
	

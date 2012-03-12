name := "Softwarequality"

version := "1.0"

organization := "Organzation"

scalaVersion := "2.9.1-1"

scalaSource in Compile <<= baseDirectory(_ / "src")

unmanagedJars in Compile <<= baseDirectory map { base => ((base ** "lib") ** "*.jar").classpath }

autoCompilerPlugins := true

retrieveManaged := true

scalacOptions += "-deprecation"

artifactPath in Compile in packageBin <<=
    baseDirectory { base => base / "build" / "softwarequality.jar" }


import sbt.Keys._

val buildVersion = "1." + sys.props.get("ROOT_BUILD_NUMBER").map(b => "%06d".format(b.toInt))
  .getOrElse(sys.env.get("ROOT_BUILD_NUMBER").map(b => "%06d".format(b.toInt)).getOrElse("SNAPSHOT"))

// TODO this lib should be available publicly and published to maven
lazy val `utility-api-client` = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    crossScalaVersions := Seq("2.10.4", "2.11.7"),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-osgi" % "2.3.9",
      "io.spray" %% "spray-client" % "1.3.3",
      "io.spray" %% "spray-json" % "1.3.2",
      "joda-time" % "joda-time" % "2.8.1",
      "org.joda" % "joda-convert" % "1.7",
      "org.specs2" %% "specs2-core" % ProjectSettings.specs2Version % "it, test",
      "org.specs2" %% "specs2-junit" % ProjectSettings.specs2Version % "it, test",
      "org.specs2" %% "specs2-mock" % ProjectSettings.specs2Version % "it, test"
    ),
    name := ProjectSettings.projectRootName,
    organization := "com.solarmosaic.client",
    packageOptions in (Compile, packageBin) += Package.ManifestAttributes(
      new java.util.jar.Attributes.Name("BuildDate") -> new java.util.Date().toString
    ),
    publishArtifact in Test := true,
    publishTo := Some("MosaicArtifactory" at "http://art.intranet.solarmosaic.com/artifactory/internal"),
    scalaVersion := "2.11.4",
    sbtVersion := "0.13.8",
    resolvers := Seq("Artifactory" at "http://art.intranet.solarmosaic.com/artifactory/repo"),
    version := buildVersion
  )

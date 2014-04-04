import sbt._
import Keys._
import play.Project._
import com.typesafe.config._

object ApplicationBuild extends Build {

  val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
  val appName = conf.getString("application.name")
  println("DEBUG: found application name: " + appName)
  val appVersion = conf.getString("application.version")
  println("DEBUG: found application version: " + appVersion)

  val appDependencies = Seq(
    "org.scalaz" %% "scalaz-core" % "6.0.4",
    "com.twitter" % "util-core" % "1.12.4",
    "org.pegdown" % "pegdown" % "1.4.1",
    "eu.henkelmann" % "actuarius_2.10.0" % "0.2.6")

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    resolvers ++= Seq(
      "twitter.com" at "http://maven.twttr.com/",
      "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
      "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"))

}
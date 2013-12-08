import sbt._
import Keys._

object ApplicationBuild extends Build {

	lazy val buildVersion = "2.2.0"
	lazy val playVersion = "2.2.0"

    val appName         = "Asset Manager"
    val appVersion      = "1.0"

    val appDependencies = Seq(
        "mysql" % "mysql-connector-java" % "5.1.27",
        "org.scala-tools" %% "scala-stm" % "0.3",
        "org.apache.derby" % "derby" % "10.4.1.3" % "test",
        "commons-io" % "commons-io" % "2.3",
        "com.typesafe" %% "play-plugins-mailer" % "2.0.4"
    )
}
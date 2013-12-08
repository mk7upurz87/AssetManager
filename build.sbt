name := "Asset Manager"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    javaJdbc,
    javaEbean,
    cache,
	"org.apache.commons" % "commons-email" % "1.3.1"
)     

play.Project.playJavaSettings
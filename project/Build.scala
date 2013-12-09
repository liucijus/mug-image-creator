import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "mug-image-creator"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    anorm
  )

  lazy val imageGeneratorProject = Project("image-generator", file("modules/image-generator"))

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  ).dependsOn(imageGeneratorProject).aggregate(imageGeneratorProject)
}
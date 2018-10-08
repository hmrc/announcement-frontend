import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object FrontendBuild extends Build with MicroService {

  val appName = "announcement-frontend"

  override lazy val appDependencies: Seq[ModuleID] = compile ++ test()

  val compile = Seq(
    "uk.gov.hmrc" %% "govuk-template" % "5.14.0",
    "uk.gov.hmrc" %% "play-ui" % "7.14.0",
    "uk.gov.hmrc" %% "auth-client" % "2.6.0",
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-25" % "1.6.0",
    "uk.gov.hmrc" %% "csp-client" % "3.1.0"
  )

  def test() = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.0.0" % "test, it",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test, it",
    "org.pegdown" % "pegdown" % "1.6.0" % "test",
    "org.jsoup" % "jsoup" % "1.8.1" % "test",
    "org.mockito" % "mockito-core" % "2.11.0" % "test, it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "test, it",
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "test",
    "com.github.tomakehurst" % "wiremock" % "2.7.1" % "test"
  )

}

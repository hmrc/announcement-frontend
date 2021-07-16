/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import TestPhases.oneForkedJvmPerTest
import play.sbt.PlayImport.PlayKeys.playDefaultPort
import sbt.Keys._
import uk.gov.hmrc.DefaultBuildSettings.{ addTestReportOption, defaultSettings, scalaSettings }
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._

val appName = "announcement-frontend"

lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(majorVersion := 1)
  .enablePlugins(Seq(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin) ++ plugins: _*)
  .settings(playSettings: _*)
  .settings(scalaSettings: _*)
  .settings(playDefaultPort := 9067)
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    scalaVersion := "2.12.12",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    routesGenerator := InjectedRoutesGenerator
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(
    Keys.fork in IntegrationTest := false,
    unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value,
    addTestReportOption(IntegrationTest, "int-test-reports"),
    testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    parallelExecution in IntegrationTest := false
  )
  .settings(resolvers ++= Seq(
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("hmrc", "releases")
  ))
  .settings(
    inConfig(IntegrationTest)(scalafmtCoreSettings ++
       Seq(
         compileInputs in compile := Def.taskDyn {
           val task = test in (resolvedScoped.value.scope in scalafmt.key)
           val previousInputs = (compileInputs in compile).value
           task.map(_ => previousInputs)
         }.value
       )
    ),
    scalafmtTestOnCompile in ThisBuild := true
  )

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

import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {
  
  val compile = Seq(
    "uk.gov.hmrc" %% "govuk-template" % "5.28.0-play-25",
    "uk.gov.hmrc" %% "play-ui" % "7.30.0-play-25",
    "uk.gov.hmrc" %% "auth-client" % "2.19.0-play-25",
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-25" % "4.8.0",
    "uk.gov.hmrc" %% "csp-client" % "3.4.0"
  )

  val test =Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.4.0-play-25" % "test, it",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test, it",
    "org.pegdown" % "pegdown" % "1.6.0" % "test",
    "org.jsoup" % "jsoup" % "1.8.1" % "test",
    "org.mockito" % "mockito-core" % "2.11.0" % "test, it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "test, it",
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "test",
    "com.github.tomakehurst" % "wiremock" % "2.7.1" % "test"
  )

}
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
    "uk.gov.hmrc" %% "govuk-template" % "5.42.0-play-26",
    "uk.gov.hmrc" %% "play-ui" % "8.3.0-play-26",
    "uk.gov.hmrc" %% "auth-client" % "2.30.0-play-26",
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.1.0",
    "uk.gov.hmrc" %% "csp-client" % "4.1.0-play-26"
  )

  val test =Seq(
     "com.typesafe.play" %% "play-test" % PlayVersion.current % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test",
    "org.jsoup" % "jsoup" % "1.12.1" % "test",
    "org.mockito"            % "mockito-all"               % "1.10.19" % "test",
    "com.github.tomakehurst" % "wiremock-jre8"             % "2.21.0" % "test,it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % "test",
    "net.codingwell"         %% "scala-guice"              % "4.2.6",
    "uk.gov.hmrc"            %% "bootstrap-play-26"        % "1.1.0" % "test" classifier "tests"
  )
}
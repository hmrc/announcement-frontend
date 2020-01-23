/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.announcementfrontend.config

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
@Singleton
class AppConfig @Inject()(servicesConfig: ServicesConfig, runModeConfiguration: Configuration, environment: Environment, runMode: RunMode) {

  private def loadConfig(key: String) = runModeConfiguration.getOptional[String](key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private val contactHost = runModeConfiguration.getOptional[String](s"contact-frontend.host").getOrElse("")
  private val twoWayMessageHost = runModeConfiguration.getOptional[String](s"two-way-message-frontend.host").getOrElse("")
  private val contactFormServiceIdentifier = "MyService"

  lazy val buttonToggle = runModeConfiguration.getOptional[Boolean](s"${runMode.env}.featureToggle.button.switch").getOrElse(false)
  lazy val twoWayMessageEnabled = runModeConfiguration.getOptional[Boolean](s"${runMode.env}.twoWayMessage.enable").getOrElse(false)
  lazy val assetsPrefix = loadConfig(s"assets.url") + loadConfig(s"assets.version")
  lazy val analyticsToken = loadConfig(s"google-analytics.token")
  lazy val analyticsHost = loadConfig(s"google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val twoWayMessageEnquiryFrontend = s"$twoWayMessageHost/two-way-message-frontend/message/p800/make_enquiry?backCode=d2luZG93LmxvY2F0aW9uLmhyZWY9Jy9hbm5vdW5jZW1lbnQvc2EtZmlsaW5nLW5vdGljZS0yMDE4Jw%3D%3D"
}
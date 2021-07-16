/*
 * Copyright 2021 HM Revenue & Customs
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

import play.api.{Configuration, Environment}

import javax.inject.{Inject, Singleton}

@Singleton
class AppConfig @Inject()(
  configuration: Configuration,
  environment: Environment
) {
  private def loadConfig(key: String) =
    configuration.getOptional[String](key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  environment.mode

  private val contactHost = configuration.getOptional[String](s"contact-frontend.host").getOrElse("")
  private val twoWayMessageHost =
    configuration.getOptional[String](s"two-way-message-frontend.host").getOrElse("")
  private val contactFormServiceIdentifier = "MyService"

  lazy val buttonToggle: Boolean = configuration.get[Boolean](s"featureToggle.button.switch")
  lazy val twoWayMessageEnabled: Boolean = configuration.get[Boolean](s"twoWayMessage.enable")
  lazy val assetsPrefix: String = loadConfig(s"assets.url") + loadConfig(s"assets.version")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val twoWayMessageEnquiryFrontend =
    s"$twoWayMessageHost/two-way-message-frontend/message/p800/make_enquiry?backCode=d2luZG93LmxvY2F0aW9uLmhyZWY9Jy9hbm5vdW5jZW1lbnQvc2EtZmlsaW5nLW5vdGljZS0yMDE4Jw%3D%3D"
}

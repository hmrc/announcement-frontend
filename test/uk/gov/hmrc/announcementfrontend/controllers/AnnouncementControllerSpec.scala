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

package uk.gov.hmrc.announcementfrontend.controllers

import org.jsoup.Jsoup
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.{Lang, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import uk.gov.hmrc.announcementfrontend.config.AppConfig
import uk.gov.hmrc.announcementfrontend.views.html.sa_filing_notice_2018


class AnnouncementControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite {

  private val mockAppConfig = app.injector.instanceOf[AppConfig]
  override implicit lazy val app: Application = fakeApplication()

  "Announcements sa-filing-notice-2018 html" should {
    "contain a webchat link with webchat status " in {
        val messagesApi = app.injector.instanceOf[MessagesApi]
        val messages = MessagesImpl(Lang.defaultLang, messagesApi)
        val result = Jsoup.parse(sa_filing_notice_2018()(FakeRequest("GET", "/"), messages , mockAppConfig).toString())

        result.body().toString must include("Need help with completing your tax return?")
        result.body().toString must include("Tell HMRC about an employee's")
        result.body().toString must include("Webinar")
        result.body().toString must include("There are several live sessions this month so you can")
        result.body().toString must include("Talk to an adviser online")
    }
  }
}

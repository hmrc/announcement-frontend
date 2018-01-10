/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.announcementfrontend.connectors

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.libs.json.Json
import uk.gov.hmrc.announcementfrontend.config.AppConfig
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.test._
import util.WithWireMock

class CspPartialConnectorSpec extends UnitSpec with MockitoSugar with ScalaFutures with GuiceOneAppPerSuite with WithWireMock {
  implicit val hc : HeaderCarrier = HeaderCarrier()

  private val error = Json.parse("""{"status": "failure", "response": "Error trying to check availability check the channel is correct"}""")

  "Csp Partial Connector Availability response handler" should {
    "determine that webchat is available" in {
      val connector: CspPartialConnector = new MockCspPartialConnector(app)
      val testResponse : HttpResponse = HttpResponse(200)
      val result = connector.responseHandler(testResponse)
      result shouldBe Available
    }

    "determine that webchat is busy" is pendingUntilFixed {
      val connector: CspPartialConnector = new MockCspPartialConnector(app)
      val testResponse : HttpResponse = HttpResponse(200)
      val result = connector.responseHandler(testResponse)
      result shouldBe Busy
    }
  }
}

class MockCspPartialConnector(app: Application) extends CspPartialConnector {
  override def appConfig: AppConfig = app.injector.instanceOf[AppConfig]
  override def http: HttpClient = app.injector.instanceOf(classOf[HttpClient])
}
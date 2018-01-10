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

import uk.gov.hmrc.announcementfrontend.config.AppConfig
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext._
import scala.concurrent.Future

trait CspPartialConnector {
  def http: HttpClient
  def appConfig : AppConfig
  lazy val serviceUrl : String = appConfig.baseUrl("csp-partials")

  def getAvailability(channelId: String)(implicit hc: HeaderCarrier): Future[WebChatAvailability] = {
    http.GET[HttpResponse](s"$serviceUrl/csp-partials/availability/$channelId")
      .map(r => responseHandler(r))
  }

  def responseHandler(response: HttpResponse): WebChatAvailability = {
    response.status match {
      case 200 => {
        //Json.parse(response.body)
        Available
      }
      case _  => TechnicalDifficulties
    }
  }
}

sealed trait WebChatAvailability
case object Available extends WebChatAvailability
case object Unavailable extends WebChatAvailability
case object Busy extends WebChatAvailability
case object TechnicalDifficulties extends WebChatAvailability

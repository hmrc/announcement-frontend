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

package uk.gov.hmrc.announcementfrontend.controllers.actions

import play.api.Logger
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals._
import uk.gov.hmrc.http.{ HeaderCarrier, UpstreamErrorResponse }
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.config.AuthRedirects

import scala.concurrent.{ ExecutionContext, Future }

trait AuthActions extends AuthorisedFunctions with AuthRedirects {

  def authorisedForAnnouncement(cc: ControllerComponents)(
    implicit ec: ExecutionContext): ActionBuilder[AnnouncementRequest, AnyContent] =
    new ActionBuilder[AnnouncementRequest, AnyContent] with ActionRefiner[Request, AnnouncementRequest] with Results {
      override def refine[A](request: Request[A]): Future[Either[Result, AnnouncementRequest[A]]] = {
        implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)
        authorised(Enrolment("IR-SA"))
          .retrieve(authorisedEnrolments) { enrol =>
            Future successful Right(AnnouncementRequest(enrol, request))
          }
          .recover {
            case _: InsufficientEnrolments                       => throw new IllegalArgumentException
            case _: AuthorisationException                       => Left(toGGLogin(request.uri))
            case e: UpstreamErrorResponse if e.statusCode == 401 => Left(toGGLogin(request.uri))
            case e =>
              Logger.error(s"Auth failed to respond: ${e.getMessage}", e)
              Left(InternalServerError)
          }
      }

      override def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser
      override protected def executionContext: ExecutionContext = ec
    }
}

case class AnnouncementRequest[A](enrolments: Enrolments, request: Request[A]) extends WrappedRequest[A](request)

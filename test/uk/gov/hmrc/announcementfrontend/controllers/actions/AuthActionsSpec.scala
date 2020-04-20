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

package uk.gov.hmrc.announcementfrontend.controllers.actions

import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.{ Configuration, Environment }
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.auth.core.{ AuthorisationException, Enrolment, InsufficientEnrolments, _ }
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

class AuthActionsSpec extends PlaySpec with AuthActions with MockitoSugar with GuiceOneAppPerSuite {

  import scala.concurrent.ExecutionContext.Implicits.global

  val controllerComponents = mock[ControllerComponents]

  val enrolmentWithoutSAUTR =
    Enrolment("HMRC-MTD-IT", Seq(EnrolmentIdentifier("MTDITID", "mtdItId")), state = "", delegatedAuthRule = None)

  val enrolmentWithSAUTR =
    Enrolment("IR-SA", Seq(EnrolmentIdentifier("UTR", "someUtr")), state = "", delegatedAuthRule = None)

  "AuthorisedForAnnouncement" should {
    "return an 200 if the user is authorised by Government Gateway and contains SA UTR" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future successful Enrolments(Set(enrolmentWithSAUTR)))

      val result: Result = response(AuthorisedForAnnouncement(controllerComponents))
      result.header.status must be(200)
    }

    "return exception if the user is authorised by Government Gateway and does not contain a SA UTR" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new InsufficientEnrolments)

      intercept[IllegalArgumentException](response(AuthorisedForAnnouncement(controllerComponents)))
    }

    "return a Internal Server Error if auth fails to respond" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new Throwable)

      val result = response(AuthorisedForAnnouncement(controllerComponents))
      result.header.status must be(500)
    }

    "redirect to Government Gateway and return 303 if auth responds with a AuthorisedForAnnouncement" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new AuthorisationException("") {})

      val result = response(AuthorisedForAnnouncement(controllerComponents))
      result.header.status must be(303)
      result.header.headers("Location") must be(toGGLogin("/").header.headers("Location"))
    }

    "redirect to Government Gateway and return 303 if auth responds with a NoActiveSession" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new NoActiveSession("") {})

      val result = response(AuthorisedForAnnouncement(controllerComponents))
      result.header.status must be(303)
      println(result.header.headers("Location"))
      result.header.headers("Location") must be(toGGLogin("/").header.headers("Location"))
    }
  }

  override def authConnector: AuthConnector = mockAuthConnector
  private lazy val mockAuthConnector = mock[AuthConnector]

  private def response(actionBuilder: ActionBuilder[Request, AnyContent]): Result = {
    val action = actionBuilder {
      Results.Ok
    }
    Await.result(action(FakeRequest()), 5 seconds)
  }

  override def config: Configuration = app.injector.instanceOf[Configuration]
  override def env: Environment = app.injector.instanceOf[Environment]
}

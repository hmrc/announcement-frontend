/*
 * Copyright 2017 HM Revenue & Customs
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

import org.mockito.ArgumentMatchers.{eq => eqs, _}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import play.api.mvc.{ActionBuilder, Request, Result, Results}
import play.api.test.FakeRequest
import play.api.{Configuration, Environment, Play}
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.auth.core.{AuthorisationException, Enrolment, InsufficientEnrolments, _}
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future

class AuthActionsSpec extends UnitSpec with MockitoSugar with AuthActions with OneAppPerSuite {

  val enrolmentWithoutSAUTR = Enrolment("HMRC-MTD-IT", Seq(EnrolmentIdentifier("MTDITID", "mtdItId")), state = "", delegatedAuthRule = None)

  val enrolmentWithSAUTR = Enrolment("IR-SA", Seq(EnrolmentIdentifier("UTR", "someUtr")), state = "", delegatedAuthRule = None)

  "AuthorisedForAnnouncement" should {

    "return an 200 if the user is authorised by Government Gateway and contains SA UTR" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future successful Enrolments(Set(enrolmentWithSAUTR)))

      val result = response(AuthorisedForAnnouncement())
      status(result) shouldBe 200
    }

    "return exception if the user is authorised by Government Gateway and does not contain a SA UTR" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new InsufficientEnrolments)

      intercept[IllegalArgumentException](response(AuthorisedForAnnouncement()))
    }


    "return a Internal Server Error if auth fails to respond" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new Throwable)

      val result = response(AuthorisedForAnnouncement())
      status(result) shouldBe 500
    }

    "redirect to Government Gateway and return 303 if auth responds with a AuthorisedForAnnouncement" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new AuthorisationException("") {})

      val result = response(AuthorisedForAnnouncement())
      status(result) shouldBe 303
      result.header.headers("Location") shouldBe toGGLogin("/").header.headers("Location")
    }

    "redirect to Government Gateway and return 303 if auth responds with a NoActiveSession" in {
      when(mockAuthConnector.authorise(any(), any[Retrieval[Enrolments]])(any(), any()))
        .thenReturn(Future failed new NoActiveSession("") {})

      val result = response(AuthorisedForAnnouncement())
      status(result) shouldBe 303
      println(result.header.headers("Location"))
      result.header.headers("Location") shouldBe toGGLogin("/").header.headers("Location")
    }
  }

  override def authConnector: AuthConnector = mockAuthConnector

  lazy val mockAuthConnector = mock[AuthConnector]

  private def response(actionBuilder: ActionBuilder[Request]): Result = {
    val action = actionBuilder {
      Results.Ok
    }
    await(action(FakeRequest()))
  }

  override def config: Configuration = Play.current.configuration

  override def env: Environment = app.injector.instanceOf[Environment]
}
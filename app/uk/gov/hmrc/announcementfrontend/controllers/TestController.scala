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

import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.{Configuration, Environment}
import play.api.mvc.{Action, AnyContent, ControllerComponents, InjectedController, MessagesControllerComponents}
import uk.gov.hmrc.announcementfrontend.config.AppConfig
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.play.bootstrap.controller.{BackendController, FrontendController}
import uk.gov.hmrc.play.bootstrap.config.{AuthRedirects, ServicesConfig}

import scala.concurrent.Future

class TestController @Inject() (
                                 implicit val appConfig: AppConfig,
configuration: Configuration,
servicesConfig: ServicesConfig,
controllerComponents: MessagesControllerComponents,
environment: Environment,
override val authConnector: AuthConnector
)
extends FrontendController(controllerComponents)  with I18nSupport with AuthorisedFunctions with AuthRedirects {

  def eval:Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok("result"))
  }

  override def config: Configuration = configuration

  override def env: Environment = environment
}

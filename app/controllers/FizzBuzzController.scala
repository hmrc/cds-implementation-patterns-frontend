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

package controllers

import config.AppConfig
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent}
import services.CustomsSessionCacheService

import scala.concurrent.Future

@Singleton
class FizzBuzzController @Inject()(cache: CustomsSessionCacheService, val messagesApi: MessagesApi, implicit val appConfig: AppConfig)
  extends CustomsController {

  val factorForm: Form[FizzBuzzFactor] = Form(mapping(
    "value" -> number(1)
  )(FizzBuzzFactor.apply)(FizzBuzzFactor.unapply))

  val displayFirstFactorForm: Action[AnyContent] = Action { implicit req =>
    Ok(views.html.firstFactorForm(factorForm))
  }

  val displaySecondFactorForm: Action[AnyContent] = Action { implicit req =>
    Ok(views.html.secondFactorForm(factorForm))
  }

  val displayFizzBuzzResultPage: Action[AnyContent] = Action.async { implicit req =>
//    for (
//      f1 <- cache.get("firstFactor");
//      f2 <- cache.get("secondFactor");
//    ) yield {
//      f1
//    }
    Future.successful(Ok)
  }

  val handleFirstFactorForm: Action[AnyContent] = Action.async { implicit req =>
    factorForm.bindFromRequest().fold(
      errors => Future.successful(BadRequest),
      success => cache.put("firstFactor", success).map { _ =>
        Ok // actually redirect
      }
    )
  }

  val handleSecondFactorForm: Action[AnyContent] = Action.async { implicit req =>
    Future.successful(Ok)
  }

}

case class FizzBuzzFactor(value: Int)

object FizzBuzzFactor {
  implicit val fizzBuzzFactorFormat: OFormat[FizzBuzzFactor] = Json.format[FizzBuzzFactor]
}

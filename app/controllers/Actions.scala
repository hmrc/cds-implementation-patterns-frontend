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

import com.google.inject.ImplementedBy
import config.ErrorHandler
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.{Configuration, Environment}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrievals._
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Name, ~}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.config.AuthRedirects

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[ActionsImpl])
trait Actions {

  def auth: ActionBuilder[AuthenticatedRequest] with ActionRefiner[Request, AuthenticatedRequest]

}

@Singleton
class ActionsImpl @Inject()(authConnector: AuthConnector, errorHandler: ErrorHandler, cfg: Configuration, environment: Environment)(implicit val ec: ExecutionContext) extends Actions {

  def auth: ActionBuilder[AuthenticatedRequest] with ActionRefiner[Request, AuthenticatedRequest] = new AuthAction(authConnector, cfg, environment)

}

class AuthAction(auth: AuthConnector, cfg: Configuration, environment: Environment)(implicit ec: ExecutionContext) extends ActionBuilder[AuthenticatedRequest] with ActionRefiner[Request, AuthenticatedRequest] with AuthorisedFunctions with AuthRedirects {

  override def authConnector: AuthConnector = auth

  override def config: Configuration = cfg

  override def env: Environment = environment

  override protected def refine[A](request: Request[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))
    authorised(SignedInUser.authorisationPredicate)
      .retrieve(credentials and name and email and affinityGroup and internalId and allEnrolments) {
        case credentials ~ name ~ email ~ affinityGroup ~ internalId ~ allEnrolments => Future.successful(Right(AuthenticatedRequest(
          request, SignedInUser(credentials, name, email, affinityGroup, internalId, allEnrolments)
        )))
      }
  }

}

case class SignedInUser(credentials: Credentials,
                        name: Name,
                        email: Option[String],
                        affinityGroup: Option[AffinityGroup],
                        internalId: Option[String],
                        enrolments: Enrolments) {

  lazy val eori: Option[String] = enrolments.getEnrolment(SignedInUser.cdsEnrolmentName).flatMap(_.getIdentifier(SignedInUser.eoriIdentifierKey)).map(_.value)

  // TODO throw ApplicationException which redirects to "enrol" page
  lazy val requiredEori: String = eori.getOrElse(throw new IllegalStateException("EORI missing"))

}

object SignedInUser {

  val cdsEnrolmentName: String = "HMRC-CUS-ORG"

  val eoriIdentifierKey: String = "EORINumber"

  val authorisationPredicate: Predicate = Enrolment(cdsEnrolmentName)

}

case class AuthenticatedRequest[A](request: Request[A], user: SignedInUser) extends WrappedRequest[A](request)

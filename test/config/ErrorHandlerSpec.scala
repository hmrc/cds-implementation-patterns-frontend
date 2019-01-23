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

package config

import controllers.{SignedInUser, routes}
import play.api.http.{HeaderNames, Status}
import play.api.test.FakeRequest
import play.api.{Configuration, Environment}
import uk.gov.customs.test.{CustomsSpec, HtmlAssertions}
import uk.gov.hmrc.auth.core.{InsufficientEnrolments, NoActiveSession}

class ErrorHandlerSpec extends CustomsSpec with HtmlAssertions {

  val handler = new ErrorHandler(messages, component[Configuration], component[Environment], appConfig)
  val req = FakeRequest("GET", "/foo")

  "resolve error" should {

    "handle no active session authorisation exception" in {
      val res = handler.resolveError(req, new NoActiveSession("A user is not logged in") {})
      res.header.status must be(Status.SEE_OTHER)
      res.header.headers.get(HeaderNames.LOCATION) must be(Some("/gg/sign-in?continue=%2Ffoo&origin=cds-implementation-patterns-frontend"))
    }

    "handle insufficient enrolments authorisation exception" in {
      val res = handler.resolveError(req, new InsufficientEnrolments(SignedInUser.cdsEnrolmentName))
      res.header.status must be(Status.SEE_OTHER)
      res.header.headers.get(HeaderNames.LOCATION) must be(Some(routes.UnauthorisedController.enrol().url))
    }

  }

}

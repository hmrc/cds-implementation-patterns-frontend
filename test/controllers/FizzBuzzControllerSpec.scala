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

import play.api.http.HttpVerbs
import uk.gov.customs.test._
import uk.gov.hmrc.http.HeaderCarrier

class FizzBuzzControllerSpec extends CustomsSpec
  with RequestHandlerBehaviours
  with HttpAssertions
  with HtmlAssertions
  with SessionCachingBehaviours
  with AuthenticationBehaviours {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val k = "value"
  private val v: Int = randomInt(Int.MaxValue)

  val form: Map[String, String] = Map(k -> s"$v")

  "GET /fizz" should {

    "display form for user to enter factor one" in withSignedInUser() { (_, _, _) =>
      withRequest(GET, uriWithContextPath("/fizz")) { res =>
        includeForm(res, routes.FizzBuzzController.handleFirstFactorForm(), HttpVerbs.POST)
        includesHtmlInput(res, k)
      }
    }

  }

  "POST /fizz" should {
    "store factor in session cache and redirect to buzz" in withSignedInUser() { (_, _, _) =>
      withCache() { cache =>
        withRequestAndFormBody(POST, uriWithContextPath("/fizz"), body = form) { res =>
          wasRedirected(routes.FizzBuzzController.displaySecondFactorForm().url, res)
          cache("firstFactor").value must be(v)
        }
      }
    }
  }

  "GET /buzz" should {

    "display form for user to enter factor two" in withSignedInUser() { (_, _, _) =>
      withRequest(GET, uriWithContextPath("/buzz")) { res =>
        includeForm(res, routes.FizzBuzzController.handleSecondFactorForm(), HttpVerbs.POST)
        includesHtmlInput(res, k)
      }
    }

  }

  "POST /buzz" should {
    "store factor in session cache and redirect to results" in withSignedInUser() { (_, _, _) =>
      withCache() { cache =>
        withRequestAndFormBody(POST, uriWithContextPath("/buzz"), body = form) { res =>
          wasRedirected(routes.FizzBuzzController.displayFizzBuzzResultPage().url, res)
          cache("secondFactor").value must be(v)
        }
      }
    }
  }

  "GET /result" should {

    // bonus points for spotting why this test will blink
    "display result of fizzbuzz calculation" in withSignedInUser() { (_, _, _) =>
      withCache(Map("firstFactor" -> FizzBuzzFactor(3), "secondFactor" -> FizzBuzzFactor(5))) { cache =>
        withRequest(GET, uriWithContextPath("/result")) { res =>
          wasOk(res)
          includeHtmlTag(res, "h1", messages("page.result.heading"))
          includeHtmlTag(res, "p", "fizzbuzz")
        }
      }
    }

  }

}

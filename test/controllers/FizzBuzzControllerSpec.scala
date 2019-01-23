package controllers

import play.api.http.HttpVerbs
import uk.gov.customs.test._
import uk.gov.hmrc.http.HeaderCarrier

class FizzBuzzControllerSpec extends CustomsSpec
  with RequestHandlerBehaviours
  with HttpAssertions
  with HtmlAssertions
  with SessionCachingBehaviours {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val k = "value"
  private val v: Int = randomInt(Int.MaxValue)

  val form: Map[String, String] = Map(k -> s"$v")

  "GET /fizz" should {

    "display form for user to enter factor one" in withRequest(GET, uriWithContextPath("/fizz")) { res =>
      includeForm(res, routes.FizzBuzzController.handleFirstFactorForm(), HttpVerbs.POST)
      includesHtmlInput(res, k)
    }

  }

  "POST /fizz" should {
    "store factor in session cache and redirect to buzz" in withRequestAndFormBody(POST, uriWithContextPath("/fizz"), body = form) { res =>
      withCache() { cache =>
        wasRedirected(routes.FizzBuzzController.displaySecondFactorForm().url, res)
        cache("firstFactor").value must be(FizzBuzzFactor(v))
      }
    }
  }

  "GET /buzz" should {

    "display form for user to enter factor two" in withRequest(GET, uriWithContextPath("/buzz")) { res =>
      includeForm(res, routes.FizzBuzzController.handleSecondFactorForm(), HttpVerbs.POST)
      includesHtmlInput(res, k)
    }

  }

  "POST /buzz" should {
    "store factor in session cache and redirect to results" in withRequestAndFormBody(POST, uriWithContextPath("/fizz"), body = form) { res =>
      withCache() { cache =>
        wasRedirected(routes.FizzBuzzController.displayFizzBuzzResultPage().url, res)
        cache("secondFactor").value must be(FizzBuzzFactor(v))
      }
    }
  }

  "GET /result" should {

    // bonus points for spotting why this test will blink
    "display result of fizzbuzz calculation" in withRequest(GET, uriWithContextPath("/result")) { res =>
      withCache(Map("firstFactor" -> FizzBuzzFactor(3), "secondFactor" -> FizzBuzzFactor(5))) { cache =>
        wasOk(res)
        includeHtmlTag(res, "h1", messages("page.result.heading"))
        includeHtmlTag(res, "p", "fizzbuzz")
      }
    }

  }

}

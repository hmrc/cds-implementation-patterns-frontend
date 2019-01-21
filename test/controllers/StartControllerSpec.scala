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

import uk.gov.customs.test.{CustomsSpec, HtmlAssertions, HttpAssertions, RequestHandlerBehaviours}

class StartControllerSpec extends CustomsSpec
  with RequestHandlerBehaviours
  with HttpAssertions
  with HtmlAssertions {

  val start = uriWithContextPath("/start")

  "GET /start" should {

    "return 200" in withRequest(GET, start) {
      wasOk
    }

    "have expected heading" in withRequest(GET, start) { resp =>
      includeHtmlTag(resp, "h1", messages("page.start.heading"))
    }

    "have a start button" in withRequest(GET, start) { resp =>
      contentAsHtml(resp) should include element withName("a").
        withClass("button-start").
        withValue(messages("page.start.button"))
      includeHtmlTag(resp, "a", messages("page.start.button"))
    }

  }

}

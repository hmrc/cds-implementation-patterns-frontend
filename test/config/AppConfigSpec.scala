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

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class AppConfigSpec extends PlaySpec with MustMatchers with GuiceOneAppPerSuite {

  val cfg = app.injector.instanceOf[AppConfig]

  "app config" should {

    "have assets prefix" in {
      cfg.assetsPrefix must be("http://localhost:9032/assets/2.149.0")
    }

    "have analytics token" in {
      cfg.analyticsToken must be("N/A")
    }

    "have analytics host" in {
      cfg.analyticsHost must be("auto")
    }

    "have report a problem partial URL" in {
      cfg.reportAProblemPartialUrl must be("http://localhost:9250/contact/problem_reports_ajax?service=MyService")
    }

    "have report a problem non-JS URL" in {
      cfg.reportAProblemNonJSUrl must be("http://localhost:9250/contact/problem_reports_nonjs?service=MyService")
    }

    "have app name" in {
      cfg.appName must be("cds-implementation-patterns-frontend")
    }

  }

}

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

import uk.gov.customs.test.CustomsSpec

class AppConfigSpec extends CustomsSpec {

  "app config" should {

    "have assets prefix" in {
      appConfig.assetsPrefix must be(s"${appConfig.assets.url}${appConfig.assets.version}")
    }

    "have analytics token" in {
      appConfig.analyticsToken must be("N/A")
    }

    "have analytics host" in {
      appConfig.analyticsHost must be("auto")
    }

    "have report a problem partial URL" in {
      appConfig.reportAProblemPartialUrl must be("http://localhost:9250/contact/problem_reports_ajax?service=MyService")
    }

    "have report a problem non-JS URL" in {
      appConfig.reportAProblemNonJSUrl must be("http://localhost:9250/contact/problem_reports_nonjs?service=MyService")
    }

    "have app name" in {
      appConfig.appName must be("cds-implementation-patterns-frontend")
    }

    "have assets frontend version" in {
      appConfig.assets.version must be("3.3.2")
    }

  }

}

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

import pureconfig._

case class AppConfig(appName: String,
                     contactFrontend: ContactFrontend = ContactFrontend(),
                     assets: Assets = Assets(),
                     googleAnalytics: GoogleAnalytics = GoogleAnalytics(),
                     microservice: Microservice = Microservice()) {

  lazy val assetsPrefix = assets.prefix
  lazy val analyticsToken = googleAnalytics.token
  lazy val analyticsHost = googleAnalytics.host
  lazy val reportAProblemPartialUrl = contactFrontend.reportAProblemPartialUrl
  lazy val reportAProblemNonJSUrl = contactFrontend.reportAProblemNonJSUrl

}

object AppConfig {
  implicit val appNameHint: ProductHint[AppConfig] = ProductHint(new ConfigFieldMapping {
    def apply(fieldName: String): String = fieldName match {
      case "appName" => fieldName
      case _ => KebabCase.fromTokens(CamelCase.toTokens(fieldName))
    }
  })
}

case class ContactFrontend(host: String = "http://localhost:9250", serviceIdentifier: String = "MyService") {
  lazy val reportAProblemPartialUrl = s"$host/contact/problem_reports_ajax?service=$serviceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$host/contact/problem_reports_nonjs?service=$serviceIdentifier"
}

case class Assets(version: String = "2.149.0", url: String = "http://localhost:9032/assets/") {
  lazy val prefix: String = s"$url$version"
}

case class GoogleAnalytics(token: String = "N/A", host: String = "auto")

case class Microservice(services: Services = Services())

case class Services()

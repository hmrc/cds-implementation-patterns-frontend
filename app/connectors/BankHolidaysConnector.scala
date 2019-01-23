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

package connectors

import javax.inject.Singleton
import org.joda.time.DateTime
import play.api.libs.json._

import scala.concurrent.Future

// TODO fetch data occasionally from https://www.gov.uk/bank-holidays.json using scheduled task then feed into back end mongo data store
// after the above is introduced, this "connector" will be redundant: instead we will have a connector to our backend
// where all "business logic" relating to fizzbuzz calculation will be performed
@Singleton
class BankHolidaysConnector {

  def findBankHolidays: Future[Set[BankHoliday]] = Future.successful(
    Json.parse(getClass.getResourceAsStream("/bank-holidays.json")).as[Set[BankHoliday]]
  )

}

case class BankHoliday(title: String, date: DateTime, notes: String, bunting: Boolean)

object BankHoliday {
  implicit val bankHolidayFormat: OFormat[BankHoliday] = Json.format[BankHoliday]
}

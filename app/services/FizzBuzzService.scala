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

package services

import connectors.BankHolidaysConnector
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

// TODO ultimately, all this "business logic" should live in a backend; we would then just call that to retrieve frontend display requirements
@Singleton
class FizzBuzzService @Inject()(dateTimeService: DateTimeService, bankHolidaysConnector: BankHolidaysConnector)(implicit val ec: ExecutionContext) {

  def calculateFizzBuzzObligation(firstFactor: Int, secondFactor: Int): Future[String] = {
    val prod = firstFactor * secondFactor
    isBankHoliday.map { hols =>
      if (prod.toString.contains("3") && hols) "lucky"
      else if (prod % 15 == 0) "fizzbuzz"
      else if (prod % 5 == 0) "buzz"
      else if (prod % 3 == 0) "fizz"
      else "none"
    }
  }

  private def isBankHoliday: Future[Boolean] =
    bankHolidaysConnector.findBankHolidays.map(_.exists(_.date.dayOfYear() == dateTimeService.now().dayOfYear()))

}

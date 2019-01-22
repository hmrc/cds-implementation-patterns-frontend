package services

import connectors.BankHolidaysConnector
import org.joda.time.DateTime
import uk.gov.customs.test.{CustomsSpec, MockDateTimeService}

class FizzBuzzServiceSpec extends CustomsSpec {

  val definitelyNotBankHoliday: DateTime = new DateTime(1999, 3, 10, 0, 0, 0, 0)
  val definitelyBankHoliday: DateTime = new DateTime(2012, 1, 2, 0, 0, 0, 0)

  class Scenario(dateTime: DateTime = DateTime.now()) {
    val service = new FizzBuzzService(new MockDateTimeService(dateTime), component[BankHolidaysConnector])
  }

  "calculate fizzbuzz obligation" should {

    "return fizz when product is a multiple of 3" in new Scenario(definitelyNotBankHoliday) {
      service.calculateFizzBuzzObligation(3, 2).futureValue must be("fizz")
    }

    "return buzz when product is a multiple of 5" in new Scenario(definitelyNotBankHoliday) {
      service.calculateFizzBuzzObligation(5, 2).futureValue must be("buzz")
    }

    "return fizzbuzz when product is a multiple of 3 AND 5" in new Scenario(definitelyNotBankHoliday) {
      service.calculateFizzBuzzObligation(5, 3).futureValue must be("fizzbuzz")
    }

    "return none when product is a not a multiple of 3, 5, or 3 and 5" in new Scenario(definitelyNotBankHoliday) {
      service.calculateFizzBuzzObligation(7, 2).futureValue must be("none")
    }

    "return lucky when product contains digit 3 and date time is bank holiday" in new Scenario(definitelyBankHoliday) {
      service.calculateFizzBuzzObligation(3, 1).futureValue must be("lucky")
    }

  }

}

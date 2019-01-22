package connectors

import uk.gov.customs.test.CustomsSpec

class BankHolidaysConnectorSpec extends CustomsSpec {

  val connector = new BankHolidaysConnector

  "find bank holidays" should {

    "find some bank holidays" in {
      connector.findBankHolidays.futureValue.nonEmpty must be(true)
    }

  }

}

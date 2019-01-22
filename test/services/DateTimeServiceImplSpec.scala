package services

import uk.gov.customs.test.CustomsSpec

class DateTimeServiceImplSpec extends CustomsSpec {

  val service = new DateTimeServiceImpl

  "now" should {

    "return current date time" in {
      val before = System.currentTimeMillis()
      val now = service.now().getMillis
      val after = System.currentTimeMillis()
      now must be >= before
      now must be <= after
    }

  }

}

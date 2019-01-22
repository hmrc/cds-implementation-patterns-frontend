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

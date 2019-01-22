package services

import com.google.inject.ImplementedBy
import javax.inject.Singleton
import org.joda.time.DateTime

@ImplementedBy(classOf[DateTimeServiceImpl])
trait DateTimeService {
  def now(): DateTime = DateTime.now()
}

@Singleton
class DateTimeServiceImpl extends DateTimeService

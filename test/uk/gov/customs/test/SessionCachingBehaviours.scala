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

package uk.gov.customs.test

import controllers.FizzBuzzFactor
import org.scalatest.BeforeAndAfterEach
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{Reads, Writes}
import services.CustomsSessionCacheService
import uk.gov.hmrc.http.HeaderCarrier

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

trait SessionCachingBehaviours extends CustomsSpec with BeforeAndAfterEach {

  private lazy val caching = new MockCustomsCacheService()

  def withCache(data: Map[String, FizzBuzzFactor] = Map.empty)
                  (test: mutable.Map[String, FizzBuzzFactor] => Unit)
                  (implicit r: Reads[FizzBuzzFactor], w: Writes[FizzBuzzFactor], hc: HeaderCarrier, ec: ExecutionContext): Unit =
    test(caching.cacheAll(data)(r, w, hc, ec))

  override protected def customise(builder: GuiceApplicationBuilder): GuiceApplicationBuilder =
    super.customise(builder).overrides(bind[CustomsSessionCacheService].to(caching))

  override protected def afterEach(): Unit = caching.clearCache()

}

class MockCustomsCacheService extends CustomsSessionCacheService {

  // source -> cacheId -> formId -> value
  val cache: mutable.Map[String, FizzBuzzFactor] = mutable.Map.empty

  override def get[A](key: String)
                     (implicit r: Reads[A], hc: HeaderCarrier, ec: ExecutionContext): Future[Option[A]] = Future.successful(
    cache.get(key).map(_.asInstanceOf[A])
  )

  override def put[A](key: String, value: A)
                     (implicit r: Reads[A], w: Writes[A], hc: HeaderCarrier, ec: ExecutionContext): Future[Boolean] = {
    cache.getOrElseUpdate(key, value.asInstanceOf[FizzBuzzFactor])
    Future.successful(true)
  }

  def clearCache(): Unit = cache.clear()

  def cacheAll(data: Map[String, FizzBuzzFactor])
                 (implicit r: Reads[FizzBuzzFactor], w: Writes[FizzBuzzFactor], hc: HeaderCarrier, executionContext: ExecutionContext): mutable.Map[String, FizzBuzzFactor] = {
    data.foreach(entry => put(entry._1, entry._2))
    cache
  }

}

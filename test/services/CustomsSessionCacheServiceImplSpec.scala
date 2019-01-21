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

import play.api.libs.json.{Json, Reads, Writes}
import uk.gov.customs.test.CustomsSpec
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

// off-the-wire, unit level test which merely asserts that we delegate appropriately to relevant SessionCache functions
class CustomsSessionCacheServiceImplSpec extends CustomsSpec {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  val k = randomString(16)
  val v = randomString(48)

  class CacheScenario(cachedData: Map[String, Any] = Map.empty) {

    val service = new CustomsSessionCacheServiceImpl(appConfig, component[HttpClient]) {

      var cache: Map[String, Any] = cachedData

      override def fetchAndGetEntry[A](key: String)(implicit hc: HeaderCarrier, r: Reads[A], ec: ExecutionContext): Future[Option[A]] =
        Future.successful(cachedData.get(key).asInstanceOf[Option[A]])

      override def cache[A](key: String, body: A)(implicit w: Writes[A], hc: HeaderCarrier, ec: ExecutionContext): Future[CacheMap] =
        Future.successful(CacheMap(randomString(8), Map(key -> Json.toJson(body))))

    }
  }

  "get" should {

    "return existing data from cache" in new CacheScenario(cachedData = Map(k -> v)) {
      service.get[String](k).futureValue must be(Some(v))
    }

    "return none for non-existent key" in new CacheScenario {
      service.get[String](randomString(32)).futureValue must be(None)
    }

  }

  "put" should {

    "place given data in session cache" in new CacheScenario {
      service.put[String](k, v).futureValue must be(true)
    }

  }

}

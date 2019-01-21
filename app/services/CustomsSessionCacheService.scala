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

import com.google.inject.{ImplementedBy, Inject, Singleton}
import config.AppConfig
import play.api.libs.json.{Reads, Writes}
import uk.gov.hmrc.http.cache.client._
import uk.gov.hmrc.http.{HeaderCarrier, HttpDelete, HttpGet, HttpPut}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[CustomsSessionCacheServiceImpl])
trait CustomsSessionCacheService {

  def get[A](key: String)(implicit r: Reads[A], hc: HeaderCarrier, ec: ExecutionContext): Future[Option[A]]

  def put[A](key: String, body: A)(implicit r: Reads[A], w: Writes[A], hc: HeaderCarrier, ec: ExecutionContext): Future[Boolean]

}

@Singleton
class CustomsSessionCacheServiceImpl @Inject()(cfg: AppConfig, httpClient: HttpClient) extends CustomsSessionCacheService with SessionCache {

  override def defaultSource: String = cfg.microservice.services.keystore.defaultSource

  override def baseUri: String = cfg.microservice.services.keystore.baseUri

  override def domain: String = cfg.microservice.services.keystore.domain

  override def http: HttpGet with HttpPut with HttpDelete = httpClient

  override def get[A](key: String)(implicit r: Reads[A], hc: HeaderCarrier, ec: ExecutionContext): Future[Option[A]] =
    fetchAndGetEntry[A](key)

  override def put[A](key: String, body: A)(implicit r: Reads[A], w: Writes[A], hc: HeaderCarrier, ec: ExecutionContext): Future[Boolean] =
    cache[A](key, body).map(res => res.getEntry[A](key).isDefined)

}
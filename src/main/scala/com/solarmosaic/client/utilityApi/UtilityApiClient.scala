/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Solar Mosaic, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.solarmosaic.client.utilityApi

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.solarmosaic.client.utilityApi.`type`.FormatTypes
import com.solarmosaic.client.utilityApi.`type`.FormatTypes.FormatType
import com.solarmosaic.client.utilityApi.json.IsoDateTimeConversions
import com.solarmosaic.client.utilityApi.model.response._
import org.joda.time.DateTime
import spray.can.Http
import spray.client.pipelining._
import spray.http.HttpHeaders.{Accept, Authorization}
import spray.http.{StatusCodes, GenericHttpCredentials, HttpRequest}
import spray.httpx.{UnsuccessfulResponseException, SprayJsonSupport}
import spray.httpx.unmarshalling.FromResponseUnmarshaller

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * UtilityAPI client.
 *
 * @param token API token
 * @param timeoutDuration Request timeout duration, defaults to 10 seconds.
 *
 * @see https://utilityapi.com/api/tokens/new.html
 */
case class UtilityApiClient(
  token: String,
  timeoutDuration: FiniteDuration = 10.seconds
) extends SprayJsonSupport with IsoDateTimeConversions {
  implicit val system = ActorSystem("utility-api-client")
  implicit val timeout = Timeout(timeoutDuration)
  import system.dispatcher

  /**
   * Set up a host-specific connection with SSL encryption enabled.
   */
  protected[utilityApi] lazy val connection: Future[SendReceive] = for (
    Http.HostConnectorInfo(connector, _) <- IO(Http) ? Http.HostConnectorSetup(
      host = UtilityApiClient.host,
      port = 443,
      sslEncryption = true
    )
  ) yield sendReceive(connector)

  /**
   * Gets a future response from the host connection, unmarshalling it to the given type.
   *
   * @param request The HTTP request.
   * @param format Which format to deliver the response in.
   * @tparam T The type to unmarshal the response to.
   * @return A Future containing the unmarshalled response object.
   */
  protected[utilityApi] def response[T: FromResponseUnmarshaller](request: HttpRequest)
      (implicit format: FormatType = FormatTypes.json): Future[T] = connection
    .map(sendAndReceive => addHeader(Authorization(GenericHttpCredentials("Token", token)))
      ~> addHeader(Accept(format.mediaType))
      ~> sendAndReceive
      ~> unmarshal[T]
    )
    .flatMap(_(request))

  /**
   * Get an account by account uid.
   * @see https://utilityapi.com/api/docs/api.html#accounts-uid
   *
   * @param accountUid The unique identifier for the account.
   * @param format The response format.
   * @return A Future containing Some account, or None if not found.
   */
  def getAccount(accountUid: String)
      (implicit format: FormatType = FormatTypes.json): Future[Option[AccountResponse]] =
    response[Option[AccountResponse]](Get(s"/api/accounts/$accountUid")) recover {
      case e: UnsuccessfulResponseException if e.response.status == StatusCodes.NotFound => None
    }

  /**
   * Get all accounts.
   * @see https://utilityapi.com/api/docs/api.html#accounts
   *
   * @param format The response format.
   * @return A Future containing a list of accounts.
   */
  def getAccounts()
      (implicit format: FormatType = FormatTypes.json): Future[List[AccountResponse]] =
    response[List[AccountResponse]](Get("/api/accounts"))

  /**
   * Get bills by service uid.
   * @see https://utilityapi.com/api/docs/api.html#bills
   *
   * @param serviceUid The unique identifier for the service.
   * @param format The response format.
   * @return A Future containing a list of bills.
   */
  def getBillsByServiceUid(serviceUid: String)
      (implicit format: FormatType = FormatTypes.json): Future[List[BillResponse]] =
    response[List[BillResponse]](Get(s"/api/services/$serviceUid/bills"))

  /**
   * Get meter intervals by service uid.
   * @see https://utilityapi.com/api/docs/api.html#intervals
   *
   * @param serviceUid The unique identifier for the service.
   * @param format The response format.
   * @return A Future containing a list of meter intervals.
   */
  def getIntervalsByServiceUid(serviceUid: String, startOption: Option[DateTime], endOption: Option[DateTime])
      (implicit format: FormatType = FormatTypes.json): Future[List[IntervalResponse]] =
    response[List[IntervalResponse]] {
      val start = startOption.map(dateTimeToIsoFormat).getOrElse("")
      val end = endOption.map(dateTimeToIsoFormat).getOrElse("")
      Get(s"/api/services/$serviceUid/intervals?start=$start&end=$end")
    }

  /**
   * Get a service by service uid.
   * @see https://utilityapi.com/api/docs/api.html#services-uid
   *
   * @param serviceUid The unique identifier for the service.
   * @param format The response format.
   * @return A Future containing Some service, or None if not found.
   */
  def getService(serviceUid: String)
      (implicit format: FormatType = FormatTypes.json): Future[Option[ServiceResponse]] =
    response[Option[ServiceResponse]](Get(s"/api/services/$serviceUid")) recover {
      case e: UnsuccessfulResponseException if e.response.status == StatusCodes.NotFound => None
    }

  /**
   * Get all services.
   * @see https://utilityapi.com/api/docs/api.html#services
   *
   * @param format The response format.
   * @return A Future containing a list of services.
   */
  def getServices()
      (implicit format: FormatType = FormatTypes.json): Future[List[ServiceResponse]] =
    response[List[ServiceResponse]](Get("/api/services"))

  /**
   * Get services by account uid.
   * @see https://utilityapi.com/api/docs/api.html#services
   *
   * @param accountUid The unique identifier for the account.
   * @param format The response format.
   * @return A Future containing a list of account services.
   */
  def getServicesByAccountUid(accountUid: String)
      (implicit format: FormatType = FormatTypes.json): Future[List[ServiceResponse]] =
    response[List[ServiceResponse]](Get(s"/api/services?accounts=$accountUid"))

}

object UtilityApiClient {
  val host = "utilityapi.com"
}

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
import com.solarmosaic.client.utilityApi.model.request.{ServiceModifyRequest, AccountModifyRequest}
import com.solarmosaic.client.utilityApi.model.response._
import com.solarmosaic.client.utilityApi.util.PathWrappers
import org.joda.time.DateTime
import spray.can.Http
import spray.client.pipelining._
import spray.http.HttpHeaders.{Accept, Authorization}
import spray.http.Uri.Path
import spray.http.Uri.Path.Slash
import spray.http.{Uri, GenericHttpCredentials, HttpRequest}
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
) extends SprayJsonSupport with IsoDateTimeConversions with PathWrappers {
  implicit val system = ActorSystem("utility-api-client")
  implicit val timeout = Timeout(timeoutDuration)
  import system.dispatcher

  val baseUri = UtilityApiClient.uri
  val basePath = baseUri.path

  /** The client URI. */
  def getUri: Uri = baseUri

  /** The client URI with the given String segments appended to the base Path. */
  def getUri(segments: Any*): Uri = getUri.withPath(basePath / segments.mkString("/"))

  /** The client URI with the given Path appended to the base Path. */
  def getUri(path: Path): Uri = getUri.withPath(basePath ++ Slash(path))

  /**
   * Gets a future response for the given request, unmarshalling it to the given type.
   *
   * @param request The HTTP request.
   * @param format Which format to deliver the response in.
   * @tparam T The type to unmarshal the response to.
   * @return A Future containing the unmarshalled response object.
   */
  protected[utilityApi] def pipeline[T: FromResponseUnmarshaller](request: HttpRequest)
      (implicit format: FormatType = FormatTypes.json): Future[T] = (
      addHeader(Authorization(GenericHttpCredentials("Token", token)))
      ~> addHeader(Accept(format.mediaType))
      ~> sendReceive
      ~> unmarshal[T]
    ).apply(request)

  /**
   * Get an account by account uid.
   * @see https://utilityapi.com/api/docs/api.html#accounts-uid
   *
   * @param accountUid The unique identifier for the account.
   * @param format The response format.
   * @return A Future containing Some account, or None if the account was not found.
   */
  def getAccount(accountUid: String)
      (implicit format: FormatType = FormatTypes.json): Future[Option[AccountResponse]] =
    pipeline[Option[AccountResponse]](Get(getUri("accounts", accountUid))) recover {
      case e: UnsuccessfulResponseException => None
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
    pipeline[List[AccountResponse]](Get(getUri("accounts"))) recover {
      case e: UnsuccessfulResponseException => Nil
    }

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
    pipeline[List[BillResponse]](Get(getUri("services", serviceUid, "bills"))) recover {
      case e: UnsuccessfulResponseException => Nil
    }

  /**
   * Get meter intervals by service uid.
   * @see https://utilityapi.com/api/docs/api.html#intervals
   *
   * @param serviceUid The unique identifier for the service.
   * @param format The response format.
   * @return A Future containing a list of meter intervals.
   */
  def getIntervalsByServiceUid(serviceUid: String, start: Option[DateTime] = None, end: Option[DateTime] = None)
      (implicit format: FormatType = FormatTypes.json): Future[List[IntervalResponse]] =
    pipeline[List[IntervalResponse]] {
      val uri = getUri("services", serviceUid, "intervals")
      val query = Seq(
        start.map(s => "start" -> dateTimeToIsoFormat(s)),
        end.map(e => "end" -> dateTimeToIsoFormat(e))
      ).flatten
      Get(uri.withQuery(query: _*))
    } recover {
      case e: UnsuccessfulResponseException => Nil
    }

  /**
   * Get a service by service uid.
   * @see https://utilityapi.com/api/docs/api.html#services-uid
   *
   * @param serviceUid The unique identifier for the service.
   * @param format The response format.
   * @return A Future containing Some service, or None if the service was not found.
   */
  def getService(serviceUid: String)
      (implicit format: FormatType = FormatTypes.json): Future[Option[ServiceResponse]] =
    pipeline[Option[ServiceResponse]](Get(getUri("services", serviceUid))) recover {
      case e: UnsuccessfulResponseException => None
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
    pipeline[List[ServiceResponse]](Get(getUri("services"))) recover {
      case e: UnsuccessfulResponseException => Nil
    }

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
    pipeline[List[ServiceResponse]](Get(getUri("services").withQuery("accounts" -> accountUid))) recover {
      case e: UnsuccessfulResponseException => Nil
    }

  /**
   * Modify an account by account uid.
   * @see https://utilityapi.com/docs#accounts-modify
   *
   * @param accountUid The unique identifier for the account.
   * @param request The request model.
   * @param format The request/response format.
   * @return A Future containing Some account, or None if the account was not found.
   */
  def modifyAccount(accountUid: String, request: AccountModifyRequest)
      (implicit format: FormatType = FormatTypes.json): Future[Option[AccountResponse]] =
    pipeline[Option[AccountResponse]](Post(getUri("accounts", accountUid, "modify"), request)) recover {
      case e: UnsuccessfulResponseException => None
    }

  /**
   * Modify a service by service uid.
   * @see https://utilityapi.com/docs#services-modify
   *
   * @param serviceUid The unique identifier for the service.
   * @param request The request model.
   * @param format The request/response format.
   * @return A Future containing Some service, or None if the service was not found.
   */
  def modifyService(serviceUid: String, request: ServiceModifyRequest)
      (implicit format: FormatType = FormatTypes.json): Future[Option[ServiceResponse]] =
    pipeline[Option[ServiceResponse]](Post(getUri("services", serviceUid, "modify"), request)) recover {
      case e: UnsuccessfulResponseException => None
    }
}

object UtilityApiClient {
  val uri = Uri("https://utilityapi.com/api")
}

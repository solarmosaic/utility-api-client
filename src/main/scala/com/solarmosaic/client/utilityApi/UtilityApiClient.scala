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
import com.solarmosaic.client.utilityApi.model.response._
import spray.can.Http
import spray.client.pipelining._
import spray.http.HttpHeaders.{Accept, Authorization}
import spray.http._
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
) extends SprayJsonSupport {
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
   * @param format Which format to deliver the response in.
   * @param request The HTTP request.
   * @tparam T The type to unmarshal the response to.
   * @return A Future containing the unmarshalled response object.
   */
  protected[utilityApi] def response[T: FromResponseUnmarshaller](
    request: HttpRequest
  )(implicit format: FormatType = FormatTypes.json): Future[T] = connection
    .map(sendAndReceive => addHeader(Authorization(GenericHttpCredentials("Token", token)))
      ~> addHeader(Accept(format.mediaType))
      ~> sendAndReceive
      ~> unmarshal[T]
    )
    .flatMap(_(request))

  /**
   * Get an account by uid.
   *
   * @see https://utilityapi.com/api/docs/api.html#accounts-uid
   * @param uid The unique identifier for the account.
   * @param format The requested response format.
   * @return A Future containing the account.
   */
  def getAccount(uid: String)(implicit format: FormatType = FormatTypes.json): Future[Option[AccountResponse]] =
    response[Option[AccountResponse]](Get(s"/api/accounts/$uid")) recover {
      case e: UnsuccessfulResponseException if e.response.status == StatusCodes.NotFound => None
    }

  /**
   * Get all accounts.
   *
   * @see https://utilityapi.com/api/docs/api.html#accounts
   * @param format The requested response format.
   * @return A Future containing a list of accounts.
   */
  def getAccounts()(implicit format: FormatType = FormatTypes.json): Future[List[AccountResponse]] =
    response[List[AccountResponse]](Get("/api/accounts"))
}

object UtilityApiClient {
  val host = "utilityapi.com"
}

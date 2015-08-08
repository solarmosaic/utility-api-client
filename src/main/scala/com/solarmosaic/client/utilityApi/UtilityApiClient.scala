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
import spray.httpx.SprayJsonSupport
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
  lazy val connection: Future[SendReceive] = for (
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
  def response[T: FromResponseUnmarshaller](request: HttpRequest, format: FormatType): Future[T] = connection
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
   * @return Future wrapped AccountResponse.
   */
  def getAccount(uid: String, format: FormatType = FormatTypes.json): Future[AccountResponse] =
    response[AccountResponse](Get(s"/api/accounts/$uid"), format)
}

object UtilityApiClient {
  val host = "utilityapi.com"
}

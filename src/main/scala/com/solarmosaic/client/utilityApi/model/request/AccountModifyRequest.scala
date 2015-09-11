package com.solarmosaic.client.utilityApi.model.request

import com.solarmosaic.client.utilityApi.json.JsonSupport

/**
 * UtilityAPI account modification request.
 * @see https://utilityapi.com/docs#accounts-modify
 *
 * @param authType The type of authorization used.
 * @param realName The full name of the customer giving direct authorization.
 * @param thirdPartyFile A base64 encoded string of a completed utility authorization form filled out and signed by the
 *  owner of the utility account.
 * @param utilityUsername The login username of the utility account that the customer is authorizing access to.
 * @param utilityPassword The login password of the utility account that the customer is authorizing access to.
 * @param updateServices Whether or not to automatically re-collect the services for this account.
 * @param updateData Whether or not to automatically re-collect service data for services in this account.
 */
case class AccountModifyRequest(
  authType: Option[String] = None,
  realName: Option[String] = None,
  thirdPartyFile: Option[String] = None,
  utilityUsername: Option[String] = None,
  utilityPassword: Option[String] = None,
  updateServices: Boolean = true,
  updateData: Boolean = true
)

object AccountModifyRequest extends JsonSupport {
  /** Implicitly provides JSON conversions for `AccountModifyRequest`. */
  implicit val format = jsonFormat(
    AccountModifyRequest.apply,
    "auth_type",
    "real_name",
    "3rdparty_file",
    "utility_username",
    "utility_password",
    "update_services",
    "update_data"
  )
}

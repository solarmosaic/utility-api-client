package com.solarmosaic.client.utilityApi.model.response

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime

/**
 * UtilityAPI account response object.
 * @see https://utilityapi.com/api/docs/api.html#account-object
 *
 * @param uid The unique identifier of the object.
 * @param userUid The unique identifier of the User that created the Account.
 * @param utility The utility abbreviation.
 * @param created A timestamp from when the object was created.
 * @param authType The type of authorization submitted. Can be either "owner" or "3rdparty".
 * @param auth Details about the authorizer.
 * @param authExpires A timestamp from when the customer authorization expires.
 * @param login The type of access to that is used to collect the data.
 * @param latest The latest log message for service access of the account.
 * @param modified The latest log message for modifying the account.
 */
case class AccountResponse(
  uid: String,
  userUid: String,
  utility: String,
  created: DateTime,
  authType: String,
  auth: String,
  authExpires: DateTime,
  login: String,
  latest: LogResponse,
  modified: LogResponse
)

object AccountResponse extends JsonSupport {
  /** Implicitly provides JSON conversions for `AccountResponse`. */
  implicit val format = jsonFormat10(AccountResponse.apply)
}

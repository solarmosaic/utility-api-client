package com.solarmosaic.client.utilityApi.model.response

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime

/**
 * UtilityAPI account response object.
 * @see https://utilityapi.com/api/docs/api.html#account-object
 *
 * @param uid Unique identifier for this account.
 * @param userUid Unique identifier for the User that created this account.
 * @param utility Utility string abbreviation.
 * @param created ISO 8601 formatted String representing the creation date of this account.
 * @param authType The type of authorization submitted (either "owner" or "3rdparty").
 * @param auth Details about the authorizer. See the link above for more details.
 * @param authExpires ISO 8601 formatted String representing when customer authorization expires.
 * @param login The type of access that was used to collect this data (either "credentials" or "account_number").
 * @param latest The latest log message for service access of the account (either "pending", "updated", or "error").
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

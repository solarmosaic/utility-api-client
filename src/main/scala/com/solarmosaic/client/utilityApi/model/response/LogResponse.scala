package com.solarmosaic.client.utilityApi.model.response

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime

/**
 * UtilityAPI log response object.
 * @see https://utilityapi.com/api/docs/api.html#log-object
 *
 * @param `type` The log entry type. One of "updated", "error", or "pending".
 * @param timestamp The timestamp of the log entry.
 * @param message The message of the log entry.
 */
case class LogResponse(
  // This shouldn't be optional according to the UtilityAPI docs, but it's missing in AccountResponse.modified
  `type`: Option[String],
  timestamp: DateTime,
  message: String
)

object LogResponse extends JsonSupport {
  /** Implicitly provides JSON conversions for `LogResponse`. */
  implicit val format = jsonFormat3(LogResponse.apply)
}

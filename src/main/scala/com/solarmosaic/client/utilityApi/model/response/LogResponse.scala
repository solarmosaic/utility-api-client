package com.solarmosaic.client.utilityApi.model.response

import java.sql.Timestamp

import com.scalapenos.spray.SnakifiedSprayJsonSupport
import com.solarmosaic.client.utilityApi.json.TimestampJsonFormat

/**
 * UtilityAPI log response object.
 *
 * @see https://utilityapi.com/api/docs/api.html#log-object
 * @param `type` The type of log entry (one of "updated", "error" or "pending").
 * @param timestamp ISO 8601 formatted String representing the Timestamp of the log entry.
 * @param message The message for the log entry.
 */
case class LogResponse(
  // This shouldn't be optional according to the UtilityAPI docs, but it's missing in AccountResponse.modified
  `type`: Option[String],
  timestamp: Timestamp,
  message: String
)

object LogResponse extends SnakifiedSprayJsonSupport with TimestampJsonFormat {
  /** Implicitly provides JSON conversions to and from `LogResponse`. */
  implicit val format = jsonFormat3(LogResponse.apply)
}

package com.solarmosaic.client.utilityApi.json

import java.sql.Timestamp

import org.joda.time.format.ISODateTimeFormat
import spray.json._

trait TimestampJsonSupport {

  /**
   * Convert a Timestamp into an ISO 8601 formatted String.
   * @param timestamp The Timestamp to format.
   * @return ISO 8601 formatted String.
   */
  def toIso8601(timestamp: Timestamp): String = ISODateTimeFormat.dateTime.print(timestamp.getTime)

  /**
   * Convert an ISO 8601 formatted String into a Timestamp.
   * @param time The formatted String.
   * @return A Timestamp.
   */
  def fromIso8601(time: String): Timestamp = new Timestamp(ISODateTimeFormat.dateTimeParser().parseMillis(time))

  /** Implicitly convert between ISO 8601 compliant `JsString` and `java.sql.Timestamp`. */
  implicit object TimestampJsonFormat extends JsonFormat[Timestamp] {

    /**
     * Convert Timestamp to an ISO 8601 formatted JsString.
     * @param timestamp The timestamp
     * @return ISO 8601 formatted JsString
     */
    def write(timestamp: Timestamp) = JsString(toIso8601(timestamp))

    /**
     * Convert ISO 8601 formatted JsString to Timestamp.
     * @param json The JsValue
     * @return Timestamp
     */
    def read(json: JsValue) = json match {
      case JsString(time) => fromIso8601(time)
      case _ => throw new DeserializationException("ISO 8601 compliant string expected.")
    }
  }
}

object TimestampJsonSupport extends TimestampJsonSupport

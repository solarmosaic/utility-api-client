package com.solarmosaic.client.utilityApi.json

import java.sql.Timestamp

import org.joda.time.format.ISODateTimeFormat
import spray.json._

trait TimestampJsonFormat {

  /** Implicitly convert between ISO 8601 compliant `JsString` and `java.sql.Timestamp`. */
  implicit object TimestampFormat extends JsonFormat[Timestamp] {

    /**
     * Convert Timestamp to an ISO 8601 formatted JsString.
     * @param timestamp The timestamp
     * @return ISO 8601 formatted JsString
     */
    def write(timestamp: Timestamp) = JsString(ISODateTimeFormat.dateTime.print(timestamp.getTime))

    /**
     * Convert ISO 8601 formatted JsString to Timestamp.
     * @param json The JsValue
     * @return Timestamp
     */
    def read(json: JsValue) = json match {
      case JsString(time) => new Timestamp(ISODateTimeFormat.dateTimeParser().parseMillis(time))
      case _ => throw new DeserializationException("ISO 8601 compliant string expected.")
    }
  }
}

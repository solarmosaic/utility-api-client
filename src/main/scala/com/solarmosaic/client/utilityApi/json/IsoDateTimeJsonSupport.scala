package com.solarmosaic.client.utilityApi.json

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json._

trait IsoDateTimeJsonSupport extends IsoDateTimeConversions {

  /** Implicitly convert between ISO 8601 formatted `JsString` and `DateTime`. */
  implicit object IsoDateTimeJsonFormat extends JsonFormat[DateTime] {

    /**
     * Convert DateTime to an ISO 8601 formatted JsString.
     * @param dateTime The DateTime
     * @return ISO 8601 formatted JsString
     */
    def write(dateTime: DateTime) = JsString(dateTimeToIsoFormat(dateTime))

    /**
     * Convert ISO 8601 formatted JsString to DateTime.
     * @param value The JsValue
     * @return DateTime
     */
    def read(value: JsValue) = value match {
      case JsString(formatted) => isoFormatToDateTime(formatted)
      case _ => throw new DeserializationException("ISO 8601 formatted String expected.")
    }
  }
}

trait IsoDateTimeConversions {
  /**
   * Convert a DateTime into an ISO 8601 formatted String.
   * @param dateTime The DateTime to format.
   * @return Formatted String.
   */
  def dateTimeToIsoFormat(dateTime: DateTime): String = ISODateTimeFormat.dateTime.print(dateTime)

  /**
   * Convert an ISO 8601 formatted String into a DateTime.
   * @param formatted The formatted String.
   * @return A DateTime.
   */
  def isoFormatToDateTime(formatted: String): DateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(formatted)
}

object IsoDateTimeJsonSupport extends IsoDateTimeJsonSupport

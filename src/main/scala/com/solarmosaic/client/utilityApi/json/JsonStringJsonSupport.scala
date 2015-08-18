package com.solarmosaic.client.utilityApi.json

import spray.json._

/**
 * Stringified JSON.
 *
 * @param json The underlying JSON String.
 */
case class JsonString(json: String) {
  override def toString = json
}

trait JsonStringJsonSupport {

  /** Implicitly convert `JsValue` to `JsonString`. */
  implicit object JsonStringJsonFormat extends JsonFormat[JsonString] {

    /**
     * Convert `JsonString` to `JsValue`.
     * @param value The JsonString
     * @return JsValue
     */
    def write(value: JsonString) = value.toJson

    /**
     * Convert `JsValue` to `JsonString`.
     * @param value The JsValue
     * @return JsonString
     */
    def read(value: JsValue) = JsonString(value.toString())
  }
}

object JsonStringJsonSupport extends JsonStringJsonSupport

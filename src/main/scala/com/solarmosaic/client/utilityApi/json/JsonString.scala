package com.solarmosaic.client.utilityApi.json

/**
 * Stringified JSON.
 *
 * @param json The underlying JSON String.
 */
case class JsonString(json: String) {
  override def toString = json
}

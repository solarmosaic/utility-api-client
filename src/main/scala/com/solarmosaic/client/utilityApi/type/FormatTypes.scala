package com.solarmosaic.client.utilityApi.`type`

import spray.http.MediaType
import spray.http.MediaTypes._

/**
 * Contains supported MediaType formats.
 */
object FormatTypes extends Enumeration {
  type FormatType = MediaTypeValue

  /**
   * MediaType Value class.
   * @param mediaType The MediaType.
   */
  class MediaTypeValue(val mediaType: MediaType) extends Val(nextId, mediaType.toString())

  /**
   * Value creation method for MediaTypeValue.
   * @param mediaType The MediaType.
   * @return MediaTypeValue
   */
  protected final def Value(mediaType: MediaType): MediaTypeValue = new MediaTypeValue(mediaType)

  val csv = Value(`text/csv`)
  val json = Value(`application/json`)
}

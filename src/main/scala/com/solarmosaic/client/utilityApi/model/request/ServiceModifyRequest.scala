package com.solarmosaic.client.utilityApi.model.request

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime

/**
 * UtilityAPI service modification request.
 * @see https://utilityapi.com/docs#services-modify
 *
 * @param activeUntil The date on which to end periodic data collection. Either an ISO 8601 formatted String, or "now".
 * @param updateData Whether to automatically re-collect service data if the `activeUntil` time is in the future.
 */
case class ServiceModifyRequest(
  activeUntil: String,
  updateData: Boolean = true
)

object ServiceModifyRequest extends JsonSupport {
  /** Implicitly provides JSON conversions for `ServiceModifyRequest`. */
  implicit val format = jsonFormat2(ServiceModifyRequest.apply(_: String, _: Boolean))

  /**
   * Create a `ServiceModifyRequest` using a `DateTime`.
   *
   * @param activeUntil The date on which to end periodic service data collection.
   * @param updateData Whether to automatically re-collect service data if the `activeUntil` time is in the future.
   * @return
   */
  def apply(
    activeUntil: DateTime,
    updateData: Boolean
  ): ServiceModifyRequest = ServiceModifyRequest(
    activeUntil = dateTimeToIsoFormat(activeUntil),
    updateData = updateData
  )
}

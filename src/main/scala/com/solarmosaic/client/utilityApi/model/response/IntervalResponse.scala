package com.solarmosaic.client.utilityApi.model.response

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime

/**
 * UtilityAPI interval response object.
 * @see https://utilityapi.com/api/docs/api.html#interval-object
 *
 * @param serviceUid The unique identifier of the Service object to which this interval object belongs.
 * @param utility The utility abbreviation.
 * @param utilityServiceId The utility's service identifier.
 * @param utilityTariffName The interval's utility tariff (i.e. rate schedule) for the service.
 * @param utilityServiceAddress The interval's service address for the meter.
 * @param utilityMeterNumber The interval's meter number for the service.
 * @param intervalStart The start timestamp of the interval period.
 * @param intervalEnd The end timestamp of the interval period.
 * @param intervalKwh The total energy usage (in kilowatt hours) during the interval period.
 * @param intervalKw The total demand (in kilowatts) during the interval period.
 * @param source The location where the interval data was collected.
 * @param updated When the interval data was last updated.
 */
case class IntervalResponse(
  serviceUid: String,
  utility: String,
  utilityServiceId: String,
  utilityTariffName: String,
  utilityServiceAddress: String,
  utilityMeterNumber: String,
  intervalStart: DateTime,
  intervalEnd: DateTime,
  intervalKwh: BigDecimal,
  intervalKw: BigDecimal,
  source: Option[String],
  updated: DateTime
)

object IntervalResponse extends JsonSupport {
  /** Implicitly provides JSON conversions for `IntervalResponse`. */
  implicit val format = jsonFormat(
    IntervalResponse.apply,
    "service_uid",
    "utility",
    "utility_service_id",
    "utility_tariff_name",
    "utility_service_address",
    "utility_meter_number",
    "interval_start",
    "interval_end",
    // Since the interval fields contain uppercase characters, we can't properly extract them with SnakeCaseJsonSupport
    "interval_kWh",
    "interval_kW",
    "source",
    "updated"
  )
}

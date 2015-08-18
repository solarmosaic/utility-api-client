package com.solarmosaic.client.utilityApi.model.response

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime
import spray.json.JsValue

/**
 * UtilityAPI bill response object.
 * @see https://utilityapi.com/api/docs/api.html#bill-object
 *
 * @param serviceUid The unique identifier of the Service object to which this bill object belongs.
 * @param utility The utility abbreviation.
 * @param utilityServiceId The utility's service identifier.
 * @param utilityTariffName The bill's utility tariff (i.e. rate schedule) for the service.
 * @param utilityServiceAddress The bill's service address for the meter.
 * @param utilityMeterNumber The bill's meter number for the service.
 * @param billStartDate The start date of the billing period for the service.
 * @param billEndDate The end date of the billing period for the service.
 * @param billBillDays The total number of days between the start and end dates of the billing period for the service.
 * @param billStatementDate The date that the bill stated was issued.
 * @param billTotalKwh The total energy usage (in kilowatt hours) during the billing period for the service.
 * @param billTotal The bill charges (in dollars) for electricity usage during the billing period.
 * @param billBreakdown A breakdown of the individual charges contributing to the bill_total amount.
 * @param source The location where the bill data was collected.
 * @param updated When the bill data was last updated.
 */
case class BillResponse(
  serviceUid: String,
  utility: String,
  utilityServiceId: String,
  utilityTariffName: String,
  utilityServiceAddress: String,
  utilityMeterNumber: String,
  billStartDate: DateTime,
  billEndDate: DateTime,
  billBillDays: Int,
  billStatementDate: DateTime,
  billTotalKwh: BigDecimal,
  billTotal: BigDecimal,
  billBreakdown: Map[String, JsValue],
  source: Option[String],
  updated: DateTime
)

object BillResponse extends JsonSupport {
  /** Implicitly provides JSON conversions for `BillResponse`. */
  implicit val format = jsonFormat15(BillResponse.apply)
}

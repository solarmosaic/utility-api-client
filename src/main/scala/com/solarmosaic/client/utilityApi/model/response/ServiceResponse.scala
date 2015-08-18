package com.solarmosaic.client.utilityApi.model.response

import com.solarmosaic.client.utilityApi.json.JsonSupport
import org.joda.time.DateTime

/**
 * UtilityAPI service response object.
 * @see https://utilityapi.com/api/docs/api.html#service-object
 *
 * @param uid The unique identifier of the object.
 * @param userUid The unique identifier of the User that created the Account to which this Service belongs.
 * @param accountUid The unique identifier of the Account object to which this Service belongs.
 * @param accountAuthType The type of authorization submitted. Can be either "owner" or "3rdparty".
 * @param accountAuth Details about the authorizer.
 * @param utility The utility abbreviation.
 * @param created A timestamp from when the object was created.
 * @param activeUntil A timestamp for when to stop monitoring this service for updates.
 * @param latest The latest log message for data collection of the service.
 * @param modified The latest log message for modifying the service.
 * @param utilityServiceId The utility's service identifier. The format of this is different for each utility.
 * @param utilityTariffName The current utility tariff (i.e. rate schedule) for the service.
 * @param utilityServiceAddress The service address for the service.
 * @param utilityBillingAccount The billing account id for the service.
 * @param utilityBillingContact The billing contact name for the service.
 * @param utilityBillingAddress The billing address for the service (i.e. where the bills are sent).
 * @param utilityMeterNumber The current meter number for the physical meter on the service.
 * @param billCoverage A list of date ranges that are covered by collected bills.
 * @param billCount The number of bills that have been collected and parsed.
 * @param intervalCoverage A list of date ranges that are covered by collected intervals.
 * @param intervalCount The number of intervals that have been collected and parsed.
 */
case class ServiceResponse(
  uid: String,
  userUid: String,
  accountUid: String,
  accountAuthType: String,
  accountAuth: String,
  utility: String,
  created: DateTime,
  activeUntil: DateTime,
  latest: LogResponse,
  modified: LogResponse,
  utilityServiceId: String,
  utilityTariffName: Option[String],
  utilityServiceAddress: Option[String],
  utilityBillingAccount: Option[String],
  utilityBillingContact: Option[String],
  utilityBillingAddress: Option[String],
  utilityMeterNumber: Option[String],
  billCoverage: List[(DateTime, DateTime)],
  billCount: Int,
  intervalCoverage: List[(DateTime, DateTime)],
  intervalCount: Int
)

object ServiceResponse extends JsonSupport {
  /** Implicitly provides JSON conversions for `ServiceResponse`. */
  implicit val format = jsonFormat21(ServiceResponse.apply)
}

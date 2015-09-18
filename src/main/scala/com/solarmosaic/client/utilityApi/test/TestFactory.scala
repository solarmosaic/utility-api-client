package com.solarmosaic.client.utilityApi.test

import com.solarmosaic.client.utilityApi.json._
import com.solarmosaic.client.utilityApi.model.request.{ServiceModifyRequest, AccountModifyRequest}
import com.solarmosaic.client.utilityApi.model.response._
import org.joda.time.DateTime
import spray.json._

/**
 * Factory objects to assist with testing.
 * TODO move to separate project (utility-api-client-test).
 */
trait TestFactory extends JsonSupport {
  lazy val now = DateTime.now()

  object JsonArray {
    def apply(elements: Option[JsValue]*): JsArray = JsArray(elements.flatMap(_.toList).toVector)
  }

  object JsonKeyValue {
    def apply(key: String, value: Any): Option[JsField] = JsonValue(value).map(key -> _)
  }

  object JsonObject {
    def apply(fields: Option[JsField]*): JsObject = JsObject(fields.flatMap(_.toList): _*)
  }

  object JsonValue {
    def apply(value: Any): Option[JsValue] = {
      def toJsValue(any: Any) = any match {
        case a: JsValue => a
        case a: String => JsString(a)
        case a if a == null => JsNull
        case a => a.toString.parseJson
      }
      value match {
        case v: Option[Any] => v.map(a => toJsValue(a))
        case v => Some(toJsValue(v))
      }
    }
  }

  /** Request models and JSON. */
  object request {
    def accountModify = AccountModifyRequest()

    def accountModifyJson(request: AccountModifyRequest) = JsonObject(
      JsonKeyValue("auth_type", request.authType),
      JsonKeyValue("real_name", request.realName),
      JsonKeyValue("3rdparty_file", request.thirdPartyFile),
      JsonKeyValue("utility_username", request.utilityUsername),
      JsonKeyValue("utility_password", request.utilityPassword),
      JsonKeyValue("update_services", request.updateServices),
      JsonKeyValue("update_data", request.updateData)
    )

    def serviceModify = ServiceModifyRequest(activeUntil = "now")

    def serviceModifyJson(request: ServiceModifyRequest) = JsonObject(
      JsonKeyValue("active_until", request.activeUntil),
      JsonKeyValue("update_data", request.updateData)
    )
  }

  /** Response models and JSON. */
  object response {
    def account = AccountResponse(
      uid = "uid",
      userUid = "userUid",
      utility = "utility",
      created = now.minusDays(30),
      authType = "authType",
      auth = "auth",
      authExpires = now.plusDays(30),
      login = "login",
      latest = log,
      modified = log
    )

    def accountJson(response: AccountResponse) = JsonObject(
      JsonKeyValue("auth", response.auth),
      JsonKeyValue("auth_expires", dateTimeToIsoFormat(response.authExpires)),
      JsonKeyValue("auth_type", response.authType),
      JsonKeyValue("created", dateTimeToIsoFormat(response.created)),
      JsonKeyValue("latest", logJson(response.latest)),
      JsonKeyValue("login", response.login),
      JsonKeyValue("modified", logJson(response.modified)),
      JsonKeyValue("uid", response.uid),
      JsonKeyValue("user_uid", response.userUid),
      JsonKeyValue("utility", response.utility)
    )

    def bill = BillResponse(
      serviceUid = "serviceUid",
      utility = "utility",
      utilityServiceId = "utilityServiceId",
      utilityTariffName = "utilityTariffName",
      utilityServiceAddress = "utilityServiceAddress",
      utilityMeterNumber = "utilityMeterNumber",
      billStartDate = now.minusMonths(1),
      billEndDate = now,
      billBillDays = 30,
      billStatementDate = now,
      billTotalKwh = BigDecimal(734.50),
      billTotal = BigDecimal(150.75),
      billBreakdown = JsonString("{}"),
      source = Some("http://test.foo/bar.pdf"),
      updated = now
    )

    def billJson(response: BillResponse) = JsonObject(
      JsonKeyValue("service_uid", response.serviceUid),
      JsonKeyValue("utility", response.utility),
      JsonKeyValue("utility_service_id", response.utilityServiceId),
      JsonKeyValue("utility_tariff_name", response.utilityTariffName),
      JsonKeyValue("utility_service_address", response.utilityServiceAddress),
      JsonKeyValue("utility_meter_number", response.utilityMeterNumber),
      JsonKeyValue("bill_start_date", dateTimeToIsoFormat(response.billStartDate)),
      JsonKeyValue("bill_end_date", dateTimeToIsoFormat(response.billEndDate)),
      JsonKeyValue("bill_bill_days", response.billBillDays),
      JsonKeyValue("bill_statement_date", dateTimeToIsoFormat(response.billStatementDate)),
      JsonKeyValue("bill_total_kWh", response.billTotalKwh),
      JsonKeyValue("bill_total", response.billTotal),
      JsonKeyValue("bill_breakdown", response.billBreakdown),
      JsonKeyValue("source", response.source.getOrElse("")),
      JsonKeyValue("updated", dateTimeToIsoFormat(response.updated))
    )

    def interval = IntervalResponse(
      serviceUid = "serviceUid",
      utility = "utility",
      utilityServiceId = "utilityServiceId",
      utilityTariffName = "utilityTariffName",
      utilityServiceAddress = "utilityServiceAddress",
      utilityMeterNumber = "utilityMeterNumber",
      intervalStart = now.minusDays(1),
      intervalEnd = now,
      intervalKwh = BigDecimal(1.5),
      intervalKw = BigDecimal(6.0),
      source = Some("http://test.foo/bar.pdf"),
      updated = now
    )

    def intervalJson(response: IntervalResponse) = JsonObject(
      JsonKeyValue("service_uid", response.serviceUid),
      JsonKeyValue("utility", response.utility),
      JsonKeyValue("utility_service_id", response.utilityServiceId),
      JsonKeyValue("utility_tariff_name", response.utilityTariffName),
      JsonKeyValue("utility_service_address", response.utilityServiceAddress),
      JsonKeyValue("utility_meter_number", response.utilityMeterNumber),
      JsonKeyValue("interval_start", dateTimeToIsoFormat(response.intervalStart)),
      JsonKeyValue("interval_end", dateTimeToIsoFormat(response.intervalEnd)),
      JsonKeyValue("interval_kWh", response.intervalKwh),
      JsonKeyValue("interval_kW", response.intervalKw),
      JsonKeyValue("source", response.source.getOrElse("")),
      JsonKeyValue("updated", dateTimeToIsoFormat(response.updated))
    )

    def log = LogResponse(
      `type` = Some("pending"),
      timestamp = now.minusMinutes(5),
      message = "message"
    )

    def logJson(response: LogResponse) = JsonObject(
      JsonKeyValue("message", response.message),
      JsonKeyValue("timestamp", dateTimeToIsoFormat(response.timestamp)),
      JsonKeyValue("type", response.`type`)
    )

    def service = ServiceResponse(
      uid = "uid",
      userUid = "userUid",
      accountUid = "accountUid",
      accountAuthType = "accountAuthType",
      accountAuth = "accountAuth",
      utility = "utility",
      created = now,
      activeUntil = now.plusYears(10),
      latest = log,
      modified = None,
      utilityServiceId = "utilityServiceId",
      utilityTariffName = None,
      utilityServiceAddress = None,
      utilityBillingAccount = None,
      utilityBillingContact = None,
      utilityBillingAddress = None,
      utilityMeterNumber = None,
      billCoverage = Nil,
      billCount = 0,
      intervalCoverage = Nil,
      intervalCount = 0
    )

    def serviceJson(response: ServiceResponse) = JsonObject(
      JsonKeyValue("uid", response.uid),
      JsonKeyValue("user_uid", response.userUid),
      JsonKeyValue("account_uid", response.accountUid),
      JsonKeyValue("account_auth_type", response.accountAuthType),
      JsonKeyValue("account_auth", response.accountAuth),
      JsonKeyValue("utility", response.utility),
      JsonKeyValue("created", dateTimeToIsoFormat(response.created)),
      JsonKeyValue("active_until", dateTimeToIsoFormat(response.activeUntil)),
      JsonKeyValue("latest", logJson(response.latest)),
      JsonKeyValue("modified", response.modified.map(logJson).orNull),
      JsonKeyValue("utility_service_id", response.utilityServiceId),
      JsonKeyValue("utility_tariff_name", response.utilityTariffName),
      JsonKeyValue("utility_service_address", response.utilityServiceAddress),
      JsonKeyValue("utility_billing_account", response.utilityBillingAccount),
      JsonKeyValue("utility_billing_address", response.utilityBillingAddress),
      JsonKeyValue("utility_meter_number", response.utilityMeterNumber),
      JsonKeyValue("bill_coverage", response.billCoverage.toJson),
      JsonKeyValue("bill_count", response.billCount),
      JsonKeyValue("interval_coverage", response.intervalCoverage.toJson),
      JsonKeyValue("interval_count", response.intervalCount)
    )
  }
}

object TestFactory extends TestFactory

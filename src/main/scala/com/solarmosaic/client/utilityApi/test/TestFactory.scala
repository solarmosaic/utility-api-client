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

  /**
   * Optionally add a property to a JSON string. Values will be quoted unless they are of type `JsValue`.
   *
   * @param name The name of the property.
   * @param optionValue The value of the property.
   * @param hasTrailingComma Whether or not to append a trailing comma to the property if it exists.
   *
   * @return Json name/value pair String if there is a value, an empty String otherwise.
   */
  def optionalProperty(
    name: String,
    optionValue: Option[Any],
    hasLeadingComma: Boolean = false,
    hasTrailingComma: Boolean = true
  ) = optionValue.map { value =>
    (if(hasLeadingComma) "," else "") +
    (s""""$name":""" + (value match {
      case v: JsValue => v.toString()
      case v => s""""$v""""
    })) +
    (if(hasTrailingComma) "," else "")
  }.getOrElse("")

  /** Request models and JSON. */
  object request {
    def accountModify = AccountModifyRequest()

    def accountModifyJson(request: AccountModifyRequest) =
      s"""
        |{
        |  ${optionalProperty("auth_type", request.authType)}
        |  ${optionalProperty("real_name", request.realName)}
        |  ${optionalProperty("3rdparty_file", request.thirdPartyFile)}
        |  ${optionalProperty("utility_username", request.utilityUsername)}
        |  ${optionalProperty("utility_password", request.utilityPassword)}
        |  "update_services": ${request.updateServices},
        |  "update_data": ${request.updateData}
        |}
      """.stripMargin.parseJson

    def serviceModify = ServiceModifyRequest(activeUntil = "now")

    def serviceModifyJson(request: ServiceModifyRequest) =
      s"""
        |{
        |  "active_until": "${request.activeUntil}",
        |  "update_data": ${request.updateData}
        |}
      """.stripMargin.parseJson
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

    def accountJson(response: AccountResponse) =
      s"""
        |{
        | "auth": "${response.auth}",
        | "auth_expires": "${dateTimeToIsoFormat(response.authExpires)}",
        | "auth_type": "${response.authType}",
        | "created": "${dateTimeToIsoFormat(response.created)}",
        | "latest": ${logJson(response.latest)},
        | "login": "${response.login}",
        | "modified": ${logJson(response.modified)},
        | "uid": "${response.uid}",
        | "user_uid": "${response.userUid}",
        | "utility": "${response.utility}"
        |}
      """.stripMargin.parseJson

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

    def billJson(response: BillResponse) =
      s"""
        |{
        |  "service_uid": "${response.serviceUid}",
        |  "utility": "${response.utility}",
        |  "utility_service_id": "${response.utilityServiceId}",
        |  "utility_tariff_name": "${response.utilityTariffName}",
        |  "utility_service_address": "${response.utilityServiceAddress}",
        |  "utility_meter_number": "${response.utilityMeterNumber}",
        |  "bill_start_date": "${dateTimeToIsoFormat(response.billStartDate)}",
        |  "bill_end_date": "${dateTimeToIsoFormat(response.billEndDate)}",
        |  "bill_bill_days": ${response.billBillDays},
        |  "bill_statement_date": "${dateTimeToIsoFormat(response.billStatementDate)}",
        |  "bill_total_kWh": ${response.billTotalKwh},
        |  "bill_total": ${response.billTotal},
        |  "bill_breakdown": ${response.billBreakdown.toString},
        |  "source": "${response.source.getOrElse("")}",
        |  "updated": "${dateTimeToIsoFormat(response.updated)}"
        |}
      """.stripMargin.parseJson

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

    def intervalJson(response: IntervalResponse) =
      s"""
        |{
        |  "service_uid": "${response.serviceUid}",
        |  "utility": "${response.utility}",
        |  "utility_service_id": "${response.utilityServiceId}",
        |  "utility_tariff_name": "${response.utilityTariffName}",
        |  "utility_service_address": "${response.utilityServiceAddress}",
        |  "utility_meter_number": "${response.utilityMeterNumber}",
        |  "interval_start": "${dateTimeToIsoFormat(response.intervalStart)}",
        |  "interval_end": "${dateTimeToIsoFormat(response.intervalEnd)}",
        |  "interval_kWh": ${response.intervalKwh},
        |  "interval_kW": ${response.intervalKw},
        |  "source": "${response.source.getOrElse("")}",
        |  "updated": "${dateTimeToIsoFormat(response.updated)}"
        |}
      """.stripMargin.parseJson

    def log = LogResponse(
      `type` = Some("pending"),
      timestamp = now.minusMinutes(5),
      message = "message"
    )

    def logJson(response: LogResponse) =
      s"""
        |{
        |  "message": "${response.message}",
        |  "timestamp": "${dateTimeToIsoFormat(response.timestamp)}"
        |  ${optionalProperty("type", response.`type`, hasLeadingComma = true, hasTrailingComma = false)}
        |}
      """.stripMargin.parseJson

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

    def serviceJson(response: ServiceResponse) =
      s"""
        |{
        |  "uid": "${response.uid}",
        |  "user_uid": "${response.userUid}",
        |  "account_uid": "${response.accountUid}",
        |  "account_auth_type": "${response.accountAuthType}",
        |  "account_auth": "${response.accountAuth}",
        |  "utility": "${response.utility}",
        |  "created": "${dateTimeToIsoFormat(response.created)}",
        |  "active_until": "${dateTimeToIsoFormat(response.activeUntil)}",
        |  "latest": ${logJson(response.latest)},
        |  "modified": ${response.modified.map(logJson).orNull},
        |  "utility_service_id": "${response.utilityServiceId}",
        |  ${optionalProperty("utility_tariff_name", response.utilityTariffName)}
        |  ${optionalProperty("utility_service_address", response.utilityServiceAddress)}
        |  ${optionalProperty("utility_billing_account", response.utilityBillingAccount)}
        |  ${optionalProperty("utility_billing_address", response.utilityBillingAddress)}
        |  ${optionalProperty("utility_meter_number", response.utilityMeterNumber)}
        |  "bill_coverage": ${response.billCoverage.toJson},
        |  "bill_count": ${response.billCount},
        |  "interval_coverage": ${response.intervalCoverage.toJson},
        |  "interval_count": ${response.intervalCount}
        |}
      """.stripMargin.parseJson
  }
}

object TestFactory extends TestFactory

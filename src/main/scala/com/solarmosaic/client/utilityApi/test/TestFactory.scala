package com.solarmosaic.client.utilityApi.test

import com.solarmosaic.client.utilityApi.json.IsoDateTimeConversions
import com.solarmosaic.client.utilityApi.model.response.{LogResponse, AccountResponse}
import org.joda.time.DateTime

/**
 * Factory objects to assist with testing.
 * TODO move to separate project (utility-api-client-test).
 */
trait TestFactory extends IsoDateTimeConversions {
  lazy val now = DateTime.now()

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

    def log = LogResponse(
      `type` = Some("type"),
      timestamp = now.minusMinutes(5),
      message = "message"
    )

    object json {
      def account(response: AccountResponse) =
        s"""
          |{
          | "auth": "${response.auth}",
          | "auth_expires": "${dateTimeToIsoFormat(response.authExpires)}",
          | "auth_type": "${response.authType}",
          | "created": "${dateTimeToIsoFormat(response.created)}",
          | "latest": ${log(response.latest)},
          | "login": "${response.login}",
          | "modified": ${log(response.modified)},
          | "uid": "${response.uid}",
          | "user_uid": "${response.userUid}",
          | "utility": "${response.utility}"
          |}
        """.stripMargin

      def log(response: LogResponse) = {
        val `type` = response.`type`.map(`type` => s""", "type": "${`type`}"""").getOrElse("")

        s"""
          |{
          |  "message": "${response.message}",
          |  "timestamp": "${dateTimeToIsoFormat(response.timestamp)}"
          |  ${`type`}
          |}
        """.stripMargin
      }
    }
  }
}

object TestFactory extends TestFactory

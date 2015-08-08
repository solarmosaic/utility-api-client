package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.json.IsoDateTimeConversions
import com.solarmosaic.client.utilityApi.model.response._
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions
import org.specs2.mutable.Specification

trait Test extends Specification
  with Mockito
  with MockitoFunctions
  with IsoDateTimeConversions {
  lazy val now = DateTime.now()

  object sample {
    lazy val accountResponse = AccountResponse(
      uid = "uid",
      userUid = "userUid",
      utility = "utility",
      created = now.minusDays(30),
      authType = "authType",
      auth = "auth",
      authExpires = now.plusDays(30),
      login = "login",
      latest = logResponse,
      modified = logResponse
    )

    lazy val logResponse = LogResponse(
      `type` = Some("type"),
      timestamp = now.minusMinutes(5),
      message = "message"
    )

    def accountResponseJson(account: AccountResponse) =
      s"""
        |{
        | "auth": "${account.auth}",
        | "auth_expires": "${dateTimeToIsoFormat(account.authExpires)}",
        | "auth_type": "${account.authType}",
        | "created": "${dateTimeToIsoFormat(account.created)}",
        | "latest": ${logResponseJson(account.latest)},
        | "login": "${account.login}",
        | "modified": ${logResponseJson(account.modified)},
        | "uid": "${account.uid}",
        | "user_uid": "${account.userUid}",
        | "utility": "${account.utility}"
        |}
      """.stripMargin

    def logResponseJson(log: LogResponse) = {
      val `type` = log.`type`.map(`type` => s""", "type": "${`type`}"""").getOrElse("")

      s"""
        |{
        |  "message": "${log.message}",
        |  "timestamp": "${dateTimeToIsoFormat(log.timestamp)}"
        |  ${`type`}
        |}
      """.stripMargin
    }
  }
}

object Test extends Test

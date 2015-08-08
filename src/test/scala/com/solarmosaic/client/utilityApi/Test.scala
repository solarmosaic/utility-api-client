package com.solarmosaic.client.utilityApi

import java.sql.Timestamp

import com.solarmosaic.client.utilityApi.json.TimestampJsonSupport
import com.solarmosaic.client.utilityApi.model.response._
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions
import org.specs2.mutable.Specification

trait Test extends Specification
  with Mockito
  with MockitoFunctions {
  lazy val now = DateTime.now()

  object sample {
    lazy val accountResponse = AccountResponse(
      uid = "uid",
      userUid = "userUid",
      utility = "utility",
      created = new Timestamp(now.minusDays(30).getMillis),
      authType = "authType",
      auth = "auth",
      authExpires = new Timestamp(now.plusDays(30).getMillis),
      login = "login",
      latest = logResponse,
      modified = logResponse
    )

    lazy val logResponse = LogResponse(
      `type` = Some("type"),
      timestamp = new Timestamp(now.getMillis),
      message = "message"
    )

    def accountResponseJson(account: AccountResponse) =
      s"""
        |{
        | "auth": "${account.auth}",
        | "auth_expires": "${TimestampJsonSupport.toIso8601(account.authExpires)}",
        | "auth_type": "${account.authType}",
        | "created": "${TimestampJsonSupport.toIso8601(account.created)}",
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
        |  "timestamp": "${TimestampJsonSupport.toIso8601(log.timestamp)}"
        |  ${`type`}
        |}
      """.stripMargin
    }
  }
}

object Test extends Test

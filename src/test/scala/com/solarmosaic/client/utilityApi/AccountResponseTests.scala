package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.AccountResponse
import spray.json._

class AccountResponseTests extends Test {
  "AccountResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      response.json.account(response.account).parseJson.convertTo[AccountResponse] shouldEqual response.account
    }
    "correctly round-trip back to itself" in {
      response.account.toJson.convertTo[AccountResponse] shouldEqual response.account
    }
  }
}

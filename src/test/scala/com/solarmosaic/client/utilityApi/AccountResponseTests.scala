package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.AccountResponse
import spray.json._

class AccountResponseTests extends Test {
  "AccountResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      response.accountJson(response.account).convertTo[AccountResponse] must be equalTo response.account
    }
    "correctly round-trip back to itself" in {
      response.account.toJson.convertTo[AccountResponse] must be equalTo response.account
    }
  }
}

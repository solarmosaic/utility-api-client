package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.AccountResponse
import spray.json._

class AccountResponseTests extends Test {
  "AccountResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      sample.accountResponseJson(sample.accountResponse).parseJson.convertTo[AccountResponse] shouldEqual sample.accountResponse
    }
    "correctly round-trip back to itself" in {
      sample.accountResponse.toJson.convertTo[AccountResponse] shouldEqual sample.accountResponse
    }
  }
}

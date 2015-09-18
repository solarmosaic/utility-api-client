package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.request.AccountModifyRequest
import spray.json._

class AccountModifyRequestTests extends Test {
  "AccountModifyRequest JSON format" should {
    "correctly deserialize from valid JSON" in {
      request.accountModifyJson(request.accountModify)
        .convertTo[AccountModifyRequest] must be equalTo request.accountModify
    }
    "correctly round-trip back to itself" in {
      request.accountModify.toJson.convertTo[AccountModifyRequest] must be equalTo request.accountModify
    }
  }
}

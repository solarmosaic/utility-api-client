package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.request.ServiceModifyRequest
import spray.json._

class ServiceModifyRequestTests extends Test {
  "ServiceModifyRequest JSON format" should {
    "correctly deserialize from valid JSON" in {
      request.serviceModifyJson(request.serviceModify)
        .convertTo[ServiceModifyRequest] must be equalTo request.serviceModify
    }
    "correctly round-trip back to itself" in {
      request.serviceModify.toJson.convertTo[ServiceModifyRequest] must be equalTo request.serviceModify
    }
  }
}

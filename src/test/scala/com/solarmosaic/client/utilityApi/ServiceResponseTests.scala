package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.ServiceResponse
import spray.json._

class ServiceResponseTests extends Test {
  "ServiceResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      response.serviceJson(response.service).convertTo[ServiceResponse] shouldEqual response.service
    }
    "correctly round-trip back to itself" in {
      response.service.toJson.convertTo[ServiceResponse] shouldEqual response.service
    }
  }
}

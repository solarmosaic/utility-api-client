package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.LogResponse
import spray.json._

class LogResponseTests extends Test {
  "LogResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      sample.logResponseJson(sample.logResponse).parseJson.convertTo[LogResponse] shouldEqual sample.logResponse
    }
    "correctly round-trip back to itself" in {
      sample.logResponse.toJson.convertTo[LogResponse] shouldEqual sample.logResponse
    }
  }
}

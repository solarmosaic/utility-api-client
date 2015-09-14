package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.LogResponse
import spray.json._

class LogResponseTests extends Test {
  "LogResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      response.json.log(response.log).parseJson.convertTo[LogResponse] shouldEqual response.log
    }
    "correctly round-trip back to itself" in {
      response.log.toJson.convertTo[LogResponse] shouldEqual response.log
    }
  }
}

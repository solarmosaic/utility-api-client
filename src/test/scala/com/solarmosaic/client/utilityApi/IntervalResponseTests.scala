package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.IntervalResponse
import spray.json._

class IntervalResponseTests extends Test {
  "IntervalResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      response.intervalJson(response.interval).convertTo[IntervalResponse] shouldEqual response.interval
    }
    "correctly round-trip back to itself" in {
      response.interval.toJson.convertTo[IntervalResponse] shouldEqual response.interval
    }
  }
}

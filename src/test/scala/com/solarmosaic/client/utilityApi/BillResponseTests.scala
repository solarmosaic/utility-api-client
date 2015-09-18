package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.BillResponse
import spray.json._

class BillResponseTests extends Test {
  "BillResponse JSON format" should {
    "correctly deserialize from valid JSON" in {
      response.billJson(response.bill).convertTo[BillResponse] shouldEqual response.bill
    }
    "correctly round-trip back to itself" in {
      response.bill.toJson.convertTo[BillResponse] shouldEqual response.bill
    }
  }
}

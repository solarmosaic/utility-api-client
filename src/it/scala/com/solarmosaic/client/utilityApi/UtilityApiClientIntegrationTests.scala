package com.solarmosaic.client.utilityApi

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions

import scala.concurrent.Await

class UtilityApiClientIntegrationTests extends Specification
  with Mockito
  with MockitoFunctions {

  val client = new UtilityApiClient(token = "84891b379be24fd8ac71874f33635ce2")

  "UtilityApiClient" should {
    "Get the account with the given uid" in {
      val uid = "1210"
      val result = Await.result(client.getAccount(uid), client.timeoutDuration)
      result.uid === uid
    }
  }
}

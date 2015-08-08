package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response.AccountResponse
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions

import scala.concurrent.Await

class UtilityApiClientIntegrationTests extends Specification
  with Mockito
  with MockitoFunctions {

  // To run these tests, you must specify a token via command-line arguments, like:
  // sbt it:test -Dutility-api.token=<API_TOKEN>
  val apiToken = sys.props.get("utility-api.token")
    .getOrElse(throw new RuntimeException("Missing -Dutility-api.token=<API_TOKEN>"))
  val client = new UtilityApiClient(apiToken)

  "UtilityApiClient" should {
    "Get None for invalid account uid" in {
      val uid = "invalid"
      Await.result(client.getAccount(uid), client.timeoutDuration) should beNone
    }
    "Get all accounts" in {
      val accounts = Await.result(client.getAccounts, client.timeoutDuration)
      accounts should not be empty

      "Get AccountResponse for valid account uid" in {
        Await.result(client.getAccount(accounts.head.uid), client.timeoutDuration) should beSome[AccountResponse]
      }
    }
  }
}

package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.model.response._
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions

import scala.concurrent.Await

/**
 * UtilityAPI client integration tests.
 * These tests make calls to the utilityapi.com server. In order to get your tests to pass, you must provide
 * some VM configuration parameters:
 *
 * -Dutility-api.token=<API_TOKEN> (A valid API token)
 * -Dutility-api.accountUidHasServices=<ACCOUNT_UID> (A valid account uid for an account that has services)
 * -Dutility-api.serviceUidHasIntervals<SERVICE_UID> (A valid service uid for a service that has intervals)
 *
 * These parameters should be passed to `sbt it:test`
 */
class UtilityApiClientIntegrationTests extends Specification
  with Mockito
  with MockitoFunctions {

  // Extract VM parameters
  val config: Map[String, String] = List(
    "utility-api.token",
    "utility-api.accountUidHasServices",
    "utility-api.serviceUidHasIntervals"
  ).map(key => key.split("\\.").last -> sys.props.get(key)
    .getOrElse(throw new RuntimeException("Missing configuration parameter: " + key))).toMap

  val client = new UtilityApiClient(config("token"))
  val invalidUid = "invalid"

  val accounts = Await.result(client.getAccounts, client.timeoutDuration)
  val validAccountUid = accounts.head.uid

  val services = Await.result(client.getServices, client.timeoutDuration)
  val validServiceUid = services.head.uid

  "getAccounts" should {
    "get all accounts" in {
      accounts should beAnInstanceOf[List[AccountResponse]]
      accounts should not be empty
    }
  }

  "getAccount" should {
    "not get an account for invalid account uid" in {
      Await.result(client.getAccount(invalidUid), client.timeoutDuration) should beNone
    }

    "get an account for valid account uid" in {
      val account = Await.result(client.getAccount(validAccountUid), client.timeoutDuration)
      account should beSome[AccountResponse]
      account.map(_.uid) should beSome(validAccountUid)
    }
  }

  "getBillsByServiceUid" should {
    val bills = Await.result(client.getBillsByServiceUid(config("serviceUidHasIntervals")), client.timeoutDuration)

    "not get bills for invalid service uid" in {
      Await.result(client.getBillsByServiceUid(invalidUid), client.timeoutDuration) === Nil
    }

    "get bills for valid service uid" in {
      bills should beAnInstanceOf[List[BillResponse]]
      bills should not be empty
    }
  }

  "getIntervalsByServiceUid" should {
    val service = Await.result(client.getService(config("serviceUidHasIntervals")), client.timeoutDuration).get

    // Note: providing a service with too many intervals will result in an error here.
    // Try to pick a service with less than 100 intervals.
    val intervals = Await.result(client.getIntervalsByServiceUid(service.uid), client.timeoutDuration)

    "get no intervals for invalid service uid" in {
      Await.result(client.getIntervalsByServiceUid(invalidUid), client.timeoutDuration) === Nil
    }

    "get intervals for valid service uid" in {
      intervals should beAnInstanceOf[List[IntervalResponse]]
      intervals should not be empty
    }

    "get intervals after the given start date" in {
      val start = intervals(intervals.size / 2).intervalStart
      val subset = Await.result(
        client.getIntervalsByServiceUid(service.uid, Some(start)),
        client.timeoutDuration
      )

      subset should beAnInstanceOf[List[IntervalResponse]]
      subset should not be empty
    }

    "get intervals before the given end date" in {
      val end = intervals(intervals.size / 2).intervalEnd
      val subset = Await.result(
        client.getIntervalsByServiceUid(service.uid, Some(end)),
        client.timeoutDuration
      )

      subset should beAnInstanceOf[List[IntervalResponse]]
      subset should not be empty
    }

    "get intervals within the given start and end dates" in {
      val start = intervals.last.intervalStart
      val end = intervals(intervals.size / 2).intervalEnd
      val subset = Await.result(
        client.getIntervalsByServiceUid(service.uid, Some(start), Some(end)),
        client.timeoutDuration
      )

      subset should beAnInstanceOf[List[IntervalResponse]]
      subset should not be empty
    }
  }

  "getServices" should {
    "get all services" in {
      services should beAnInstanceOf[List[ServiceResponse]]
      services should not be empty
    }
  }

  "getService" should {
    "not get a service for invalid service uid" in {
      Await.result(client.getService(invalidUid), client.timeoutDuration) should beNone
    }
    "get a service for valid service uid" in {
      val service = Await.result(client.getService(validServiceUid), client.timeoutDuration)
      service should beSome[ServiceResponse]
      service.map(_.uid) should beSome(validServiceUid)
    }
  }

  "getServicesByAccountUid" should {
    "not get services for invalid account uid" in {
      Await.result(client.getServicesByAccountUid(invalidUid), client.timeoutDuration) === Nil
    }

    "get services for valid account uid" in {
      val accountServices = Await.result(
        client.getServicesByAccountUid(config("accountUidHasServices")),
        client.timeoutDuration
      )

      accountServices should beAnInstanceOf[List[ServiceResponse]]
      accountServices should not be empty
      accountServices.head.accountUid === config("accountUidHasServices")
    }
  }
}

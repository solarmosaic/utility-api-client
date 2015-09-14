package com.solarmosaic.client.utilityApi

import com.solarmosaic.client.utilityApi.test.TestFactory
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions
import org.specs2.mutable.Specification

trait Test extends Specification
  with Mockito
  with MockitoFunctions
  with TestFactory

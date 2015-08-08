package com.solarmosaic.client.utilityApi.json

import spray.json.DefaultJsonProtocol

trait JsonSupport extends DefaultJsonProtocol
  with IsoDateTimeJsonSupport
  with SnakeCaseJsonSupport

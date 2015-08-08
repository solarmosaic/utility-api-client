package com.solarmosaic.client.utilityApi.json

import com.scalapenos.spray.SnakifiedSprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends DefaultJsonProtocol
  with SnakifiedSprayJsonSupport
  with TimestampJsonSupport

package com.solarmosaic.client.utilityApi.util

import spray.http.Uri
import spray.http.Uri.Path

trait PathWrappers {
  import scala.language.implicitConversions

  /** Implicitly convert `Path` to `Uri`. */
  implicit def pathToUri(path: Path): Uri = Uri(path = path)
}

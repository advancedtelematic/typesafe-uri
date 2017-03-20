/*
 * Copyright 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 * Copyright 2017 ATS Advanced Telematic Systems GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.advancedtelematic.uri

import scala.annotation.tailrec

private[uri] class EnhancedByteArray(val underlying: Array[Byte]) extends AnyVal {

  /**
    * Tests two byte arrays for value equality in a way that defends against timing attacks.
    * Simple equality testing will stop at the end of a matching prefix thereby leaking information
    * about the length of the matching prefix which can be exploited for per-byte progressive brute-forcing.
    *
    * @note This function leaks information about the length of each byte array as well as
    *       whether the two byte arrays have the same length.
    * @see [[http://codahale.com/a-lesson-in-timing-attacks/]]
    * @see [[http://rdist.root.org/2009/05/28/timing-attack-in-google-keyczar-library/]]
    * @see [[http://emerose.com/timing-attacks-explained]]
    */
  def secure_==(other: Array[Byte]): Boolean = {
    @tailrec def xor(ix: Int = 0, result: Int = 0): Int =
      if (ix < underlying.length) xor(ix + 1, result | (underlying(ix) ^ other(ix))) else result

    other.length == underlying.length && xor() == 0
  }
}

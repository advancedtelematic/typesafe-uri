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

package com.advancedtelematic

import java.nio.charset.Charset

package object uri {
  private[uri] val UTF8     = Charset.forName("UTF8")
  private[uri] val ASCII    = Charset.forName("ASCII")
  private[uri] val ISO88591 = Charset.forName("ISO-8859-1")

  private[uri] implicit def enhanceByteArray(array: Array[Byte]): EnhancedByteArray =
    new EnhancedByteArray(array)
  private[uri] implicit def enhanceString_(s: String): EnhancedString = new EnhancedString(s)
}

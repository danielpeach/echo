/*
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.echo.api.notifications

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import strikt.api.expect
import strikt.assertions.isA

class NotificationTest : JUnit5Minutests {

  fun tests() = rootContext<Fixture> {
    context("Notification") {
      fixture {
        Fixture()
      }

      test("converts to correct action type") {
        val notification = mapper.convertValue<Notification>(notification)

        expect {
          that(notification.interactiveActions.actions.first()).isA<Notification.ButtonAction>()
        }
      }
    }
  }

  private class Fixture {
    var mapper = jacksonObjectMapper()

    val notification = mapOf(
      "notificationType" to "my-notification",
      "interactiveActions" to mapOf(
        "actions" to listOf(
          mapOf(
            "type" to "button",
            "name" to "my-action",
            "value" to "my-value",
            "label" to "Click me!"
          )
        )
      )
    )
  }
}

/*
 * Copyright 2015 Netflix, Inc.
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

package com.netflix.spinnaker.echo.api.notifications;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
public class Notification {
  String notificationType;
  Collection<String> to;
  Collection<String> cc;
  String templateGroup;
  Severity severity;
  Source source;
  Map<String, Object> additionalContext = new HashMap<>();
  InteractiveActions interactiveActions;

  public boolean isInteractive() {
    return interactiveActions != null && !interactiveActions.actions.isEmpty();
  }

  @Data
  public static class Source {
    String executionType;
    String executionId;
    String application;
    String user;
  }

  enum Severity {
    NORMAL,
    HIGH
  }

  /**
   * Allows Spinnaker services sending Notifications through Echo to specify one or more interactive
   * actions that, when acted upon by a user, cause a callback to Echo which gets routed to that
   * originating service.
   */
  @Data
  public static class InteractiveActions {
    String callbackServiceId;
    String callbackMessageId;
    String color = "#cccccc";
    List<InteractiveAction> actions = new ArrayList<>();
  }

  @JsonTypeInfo(
      include = JsonTypeInfo.As.EXISTING_PROPERTY,
      use = JsonTypeInfo.Id.NAME,
      property = "type")
  @JsonSubTypes(@JsonSubTypes.Type(value = ButtonAction.class, name = "button"))
  @Data
  public abstract static class InteractiveAction {
    String type;
    String name;
    String value;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ButtonAction extends InteractiveAction {
    String type = "button";
    String label;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class InteractiveActionCallback {
    InteractiveAction actionPerformed;
    String serviceId;
    String messageId;
    String user;
  }
}

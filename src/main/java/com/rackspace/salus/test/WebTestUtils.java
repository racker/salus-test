/*
 * Copyright 2019 Rackspace US, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rackspace.salus.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Provides testing support for Spring MVC tests.
 */
public class WebTestUtils {

  /**
   * The default Spring Boot error handler is not registered when using {@link MockMvc},
   * so this custom {@link ResultMatcher} encapsulates validation of the resolved exception
   * of the MVC result.
   * Note that this only works for field validations, not class validations
   * <p>
   *   The following is an example of how this result matcher can be used:
   * </p>
   * <pre>
 ZoneCreatePrivate create = newZoneCreatePrivate();
 create.setName("Cant use non-alphanumeric!!!");

 mvc.perform(post("/api/tenant/{tenantId}/zones", "t-1")
   .content(objectMapper.writeValueAsString(create))
   .contentType(MediaType.APPLICATION_JSON)
   .characterEncoding(StandardCharsets.UTF_8.name()))
   .andExpect(status().isBadRequest())
   .andExpect(
     validationError(
       "name",
       "Only alphanumeric and underscore characters can be used"));
   * </pre>
   *
   * @param expectedField the field expected to have a validation issue
   * @param expectedMessage the expected default message of the failed validation
   * @return the {@link ResultMatcher}
   */
  public static ResultMatcher validationError(String expectedField, String expectedMessage) {
    return mvcResult -> {
      assertThat(mvcResult.getResolvedException())
          .isInstanceOf(MethodArgumentNotValidException.class);

      final FieldError fieldError = ((MethodArgumentNotValidException) mvcResult
          .getResolvedException()).getBindingResult()
          .getFieldError();
      assertThat(fieldError.getDefaultMessage()).isEqualTo(expectedMessage);
      assertThat(fieldError.getField()).isEqualTo(expectedField);
    };
  }

  /**
   * Checks the error message returned by a class validation failure.
   * Note that this only works for class validations, not field validations
   * <p>
   *   The following is an example of how this result matcher can be used:
   * </p>
   * <pre>
   DetailedMonitorInput create = setupCreateMonitorTest();
   create.setDetails(new LocalMonitorDetails().setPlugin(new Mem()))
   .setLabelSelector(null)
   .setResourceId("");

   mockMvc.perform(post(url)
   .content(objectMapper.writeValueAsString(create))
   .contentType(MediaType.APPLICATION_JSON)
   .characterEncoding(StandardCharsets.UTF_8.name()))
   .andExpect(status().isBadRequest())
   .andExpect(classValidationError(ValidCreateMonitor.DEFAULT_MESSAGE));

   * </pre>
   *
   * @param expectedMessage the expected default message of the failed validation
   * @return the {@link ResultMatcher}
   */
  public static ResultMatcher classValidationError(String expectedMessage) {
    return mvcResult -> {
      assertThat(mvcResult.getResolvedException())
          .isInstanceOf(MethodArgumentNotValidException.class);

      final String actualMessage = ((MethodArgumentNotValidException) mvcResult
          .getResolvedException()).getMessage();
      assertThat(actualMessage).contains(expectedMessage);
    };
  }

  /**
   * Checks the error condition that typically occurs when a JSON body cannot be parsed due to
   * JSON syntax issue or unsupported deserialization. Specifically, it is checking for
   * {@link HttpMessageNotReadableException}
   * @param expectedMessagePart since exceptions of this type usually have a deep cause-chain, this
   * parameter is used to partially match the exception's message
   * @return the {@link ResultMatcher}
   */
  public static ResultMatcher httpMessageNotReadable(String expectedMessagePart) {
    return mvcResult -> {
      assertThat(mvcResult.getResolvedException())
          .isInstanceOf(HttpMessageNotReadableException.class);

      final HttpMessageNotReadableException resolvedException = (HttpMessageNotReadableException) mvcResult
          .getResolvedException();

      assertThat(resolvedException.getMessage()).contains(expectedMessagePart);
    };
  }
}

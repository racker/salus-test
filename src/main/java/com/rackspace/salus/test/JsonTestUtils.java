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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

/**
 * Provides testing support for JSON unit tests.
 */
public class JsonTestUtils {

  /**
   * This utility loads a text file located in the classpath into the returned string.
   * Since this is intended to be used for unit tests, the text file is typically placed
   * in <code>src/test/resources</code>.
   * <p>
   *   The following shows an example of using this method, where the file's actual path
   *   in the project would be <code>src/test/resources/ConversionTests/net.json</code>:
   * </p>
   * <pre>
   final String content = readContent("/ConversionTests/net.json");
   JSONAssert.assertEquals(content, result.getContent(), true);
   * </pre>
   * @param resource the path to the resource relative to the classpath
   * @return the content of the text file
   * @throws IOException if there was an issue locating or loading the file
   * @see ClassPathResource
   */
  public static String readContent(String resource) throws IOException {
    try (InputStream in = new ClassPathResource(resource).getInputStream()) {
      return FileCopyUtils.copyToString(new InputStreamReader(in));
    }
  }
}

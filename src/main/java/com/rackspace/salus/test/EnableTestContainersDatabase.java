/*
 * Copyright 2020 Rackspace US, Inc.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * This annotation should be placed on any unit test class that is using un-mocked interactions with
 * JPA/JDBC. It will activate the use of testcontainers to create a MySQL container and configure
 * the datasource to reference that.
 * <p>
 *   <b>NOTE</b> if also using the {@link DataJpaTest} annotation on a unit test class, then it
 *   seems to be necessary to place {@link EnableTestContainersDatabase} before {@link DataJpaTest}.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// disable any default behavior to replace datasource with an embedded one, such as H2
@AutoConfigureTestDatabase(replace = Replace.NONE)
// trigger the use of application-testcontainers.yml for configuration
@ActiveProfiles("testcontainers")
public @interface EnableTestContainersDatabase {

}

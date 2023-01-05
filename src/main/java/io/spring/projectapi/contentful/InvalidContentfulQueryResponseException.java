/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.projectapi.contentful;

import org.springframework.graphql.client.ClientGraphQlResponse;

/**
 * {@link ContentfulException} thrown when an invalid response is received.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 */
public class InvalidContentfulQueryResponseException extends ContentfulException {

	InvalidContentfulQueryResponseException(Throwable cause) {
		super(cause);
	}

	InvalidContentfulQueryResponseException(String message) {
		super(message);
	}

	static void throwIfInvalid(ClientGraphQlResponse response) {
		if (response == null || !response.isValid()) {
			throw new InvalidContentfulQueryResponseException("Empty or invalid contentful response");
		}
	}

}
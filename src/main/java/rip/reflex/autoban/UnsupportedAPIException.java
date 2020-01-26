/*
 * Copyright 2020 DarksideCode
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

package rip.reflex.autoban;

/**
 * Thrown in case if ReflexAutoban's API version
 * does not match installed Reflex's API version.
 */
public class UnsupportedAPIException extends RuntimeException {

    /**
     * The advice which may help user to fix resolve this error.
     * Displayed as a part of the exception message.
     */
    private static final String ADVICE =
            "Make sure you are using the latest version of Reflex from Spigot. " +
            "If Reflex API version is greater than ReflexAutoban API version, "  +
            "please contact the developers to update.";

    public UnsupportedAPIException(final int reflexVersion) {
        super("Reflex: " + reflexVersion + ", ReflexAutoban: " + ReflexAutoban.API_VERSION + ". " + ADVICE);
    }

}

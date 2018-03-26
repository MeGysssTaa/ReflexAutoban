/*
 * Copyright 2018 DarksideCode
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

package rip.reflex.autoban.util.action.func;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.Misc;

/**
 * The object to extend when creating Function implementations.
 */
@RequiredArgsConstructor
public abstract class AbstractFunction {

    /**
     * Returned as the function execution result in case
     * an unexpected exception occurred during execution.
     */
    public static final String ERROR_RESULT = "<ERROR>";

    /**
     * The name of this function
     */
    @Getter
    private final String name;

    /**
     * Attempt to execute this Function and obtain its execution result as string.
     *
     * @param args The arguments to execute this Function with
     * @return The object created at the end of the Function execution (toString'ed)
     */
    public final String exec(final String[] args) {
        try {
            return exec0(args);
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn("Error executing function " + name);
            Misc.printStackTrace(ex);

            return ERROR_RESULT;
        }
    }

    protected abstract String exec0(final String[] args) throws Exception;

}

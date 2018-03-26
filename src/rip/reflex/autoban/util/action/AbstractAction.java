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

package rip.reflex.autoban.util.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.Misc;

/**
 * The object to extend when creating Action implementations.
 */
@RequiredArgsConstructor
public abstract class AbstractAction {

    /**
     * The operator (the name) of this action
     */
    @Getter
    private final String op;

    /**
     * The minimal required amount of arguments
     */
    private final int minArgsLen;

    /**
     * Attempt to run this Action and obtain its execution result as boolean.
     *
     * @param args The arguments to run this Action with. May be null.
     * @return true if this Action was executed successfully, false otherwise
     */
    public final boolean run(final String[] args) {
        try {
            return run0(verifyArgs(args));
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn("Error running action " + op);
            Misc.printStackTrace(ex);

            return false;
        }
    }

    protected abstract boolean run0(final String[] args) throws Exception;

    /**
     * Verify the validity of the given array of arguments.
     * @throws IllegalArgumentException If the given array is null while minArgsLen is greater than zero or
     *                                  if the length of the given argument is less than minArgsLen.
     */
    private String[] verifyArgs(final String[] args) {
        if (args == null) {
            if (minArgsLen > 0)
                throw new IllegalArgumentException(String.format("Unexpected args length for %s: none given, %s expected", op, minArgsLen));
            return null;
        }

        if (args.length < minArgsLen)
            throw new IllegalArgumentException(String.format("Unexpected args length for %s: %s given, %s expected", op, args.length, minArgsLen));
        return args;
    }

}

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

import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.str.Strings;
import rip.reflex.autoban.util.action.func.impl.FunctionRnd;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Functions-related utilities.
 */
public class Functions {

    /**
     * The character presence of which at the beginning of String
     * identifies that the String is a function string.
     */
    private static final char FN_IDENTIFIER = '$';

    /**
     * List of registered functions
     */
    private static final Set<AbstractFunction> functions = new HashSet<>();

    /**
     * Initialize the functions
     */
    public static void init() {
        register(new FunctionRnd());
    }

    /**
     * Add the given function to the defined functions set.
     * @param fn The function to add.
     */
    public static void register(final AbstractFunction fn) {
        functions.add(fn);
    }

    /**
     * Create and execute a Function from the given string
     * and obtain its execution result (function value).
     *
     * @param s The string to create and execute a Function from.
     * @return The result of the created Function execution if the given string is
     *         is a function string, or the given string as is otherwise.
     */
    public static String execute(final String s) {
        if (s.charAt(0) != FN_IDENTIFIER)
            return s;

        try {
            // $func(a,b) => func(a,b
            final String fn = Strings.wrap(s).replace("$", "").replace(")", "").get();
            final String[] n = fn.split(Pattern.quote("("));

            final String name = n[0];
            final String[] args = n[1].split(Pattern.quote(","));

            final AbstractFunction func;

            if ((func = functions.stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst().orElse(null)) != null)
                return func.exec(args);

            ReflexAutoban.getInstance().getLog().warn(String.format("Undefined function '%s' (from string '%s')", name, s));
            return AbstractFunction.ERROR_RESULT;
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn("Error executing function '" + s + "'. Make sure to check your syntax!");
            Misc.printStackTrace(ex);

            return AbstractFunction.ERROR_RESULT;
        }
    }

}

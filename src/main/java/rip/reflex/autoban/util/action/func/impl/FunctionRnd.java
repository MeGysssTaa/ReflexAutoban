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

package rip.reflex.autoban.util.action.func.impl;

import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.action.func.AbstractFunction;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates an integer in the given range.
 * For example, rnd(5,10) will generate in integer
 * which is greater than or equal to 5 and less
 * than or equal to 10 (5, 6, 7, 8, 9 or 10).
 */
public class FunctionRnd extends AbstractFunction {

    public FunctionRnd() {
        super("rnd");
    }

    @Override
    protected String exec0(final String[] args) throws Exception {
        if ((args == null) || (args.length != 2))
            throw new IllegalArgumentException("Function RND expected 2 arguments, but got " + ((args == null) ? "<NULL>" : args.length));

        final String strMin = args[0], strMax = args[1];
        final int min, max;

        try {
            min = Integer.parseInt(strMin);
            max = Integer.parseInt(strMax);
        } catch (final NumberFormatException ex) {
            ReflexAutoban.getInstance().getLog().warn(String.format("Function RND expected INTEGER at 0 and 1, but got '%s' and '%s'", strMin, strMax));
            Misc.printStackTrace(ex);

            return ERROR_RESULT;
        }

        return Integer.toString(ThreadLocalRandom.current().nextInt(min, max + 1));
    }

}

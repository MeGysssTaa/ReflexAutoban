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

package rip.reflex.autoban.api;

import rip.reflex.autoban.util.action.AbstractAction;
import rip.reflex.autoban.util.action.Actions;
import rip.reflex.autoban.util.action.func.AbstractFunction;
import rip.reflex.autoban.util.action.func.Functions;

public class ReflexAutobanAPI {

    /**
     * Public version of ReflexAutoban's own API (not the Reflex API version!)
     */
    public static final int PUBLIC_API_VERSION = 1;

    private static boolean init = false;

    ReflexAutobanAPI() {
        if (init)
            throw new IllegalStateException("ReflexAutobanAPI cannot be instantiated twice! Please use ReflexAutobanAPIProvider#getAPI to obtain the working API instance");
        init = true;
    }

    /**
     * Add the given function to the defined functions list.
     * @param fn The function to add.
     */
    public ReflexAutobanAPI registerFunction(final AbstractFunction fn) {
        Functions.register(fn);
        return this;
    }

    /**
     * Add the given action to the defined actions list.
     * @param a The action to add.
     */
    public ReflexAutobanAPI registerAction(final AbstractAction a) {
        Actions.register(a);
        return this;
    }

}

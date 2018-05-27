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

import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.action.impl.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Actions-related utilities.
 */
public class Actions {

    /**
     * List of registered actions
     */
    private static final Set<AbstractAction> actions = new HashSet<>();

    /**
     * Initialize the actions
     */
    public static void init() {
        register(new ActionRunCommand());
        register(new ActionWait());
        register(new ActionPlayBanAnimation());
        register(new ActionIgnoreNext());
        register(new ActionSaveLogs());
    }

    /**
     * Add the given action to the defined actions set.
     * @param a The action to add.
     */
    public static void register(final AbstractAction a) {
        actions.add(a);
    }

    /**
     * Find function with the given op and run it with
     * the given array of arguments returning it's exit state.
     *
     * @param op The operation to search by
     * @param args The array of arguments to run the action with
     *
     * @see Op
     * @return ActionStatus at the end of action run
     */
    public static int run(final String op, final String[] args) {
        try {
            final AbstractAction action;

            if (((action = actions.stream().filter(a -> a.getOp().equalsIgnoreCase(op)).findFirst().orElse(null)) != null)
                    && (action.run(args)))
                return ActionState.DONE;

            ReflexAutoban.getInstance().getLog().warn("Undefined action '" + op + "'");
            return ActionState.ERROR;
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn(String.format("Error running action '%s' with %s arguments. Make sure to check your syntax!", op, (args == null) ? "<NULL>" : args.length));
            Misc.printStackTrace(ex);

            return ActionState.ERROR;
        }
    }

}

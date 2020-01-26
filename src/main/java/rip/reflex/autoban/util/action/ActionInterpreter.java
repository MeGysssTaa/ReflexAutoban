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

package rip.reflex.autoban.util.action;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.multithreading.TaskManager;

import java.util.List;

import static rip.reflex.autoban.util.action.Op.ALL_ASYNC;
import static rip.reflex.autoban.util.action.Op.ALL_SYNC;

/**
 * An Action interpreter. Given a lists of Action objects,
 * executes ones in the specified order accomodated to changes
 * made by flow controllers.
 */
@RequiredArgsConstructor
public class ActionInterpreter {

    /**
     * The list of actions to execute
     */
    private final List<Action> actions;

    /**
     * Begins interpretation of the given Actions list by running
     * the core execution method (run0) synchronously or asynchronously
     * depending on presence of the ALL_ASYNC instruction at the
     * beginning of the given Actions list.
     */
    public void run() {
        if ((actions == null) || (actions.isEmpty()))
            return;

        final String fOp = actions.get(0).getOp();

        if (fOp.equals(ALL_SYNC))
            Bukkit.getScheduler().runTask(ReflexAutoban.getInstance(), this::run0);
        else if (fOp.equals(ALL_ASYNC))
            TaskManager.runAsync(this::run0);
        else run0();
    }

    /**
     * Core execution method.
     */
    private void run0() {
        try {
            for (final Action a : actions) {
                try {
                    if (a.hasFlow(Flow.SYNC))
                        // Force synchronous execution (for ALL_SYNC)
                        Bukkit.getScheduler().runTask(ReflexAutoban.getInstance(), () -> runAction(a));
                    else if (a.hasFlow(Flow.ASYNC))
                        // Force asynchronous execution
                        TaskManager.runAsync(() -> runAction(a));
                    else runAction(a);
                } catch (final Exception ex) {
                    ReflexAutoban.getInstance().getLog().warn(String.format("Failed to interpret action '%s' (current state: %s)", a.getStatement(), a.getCurrentState()));
                    Misc.printStackTrace(ex);
                }
            }
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn("Action Interpretator crashed D: Some (or all) actions may not have ran!");
            Misc.printStackTrace(ex);
        }
    }

    /**
     * Execute the given single Action updating its state.
     * Special case: if the action operator is ALL_SYNC or ALL_ASYNC, nothing
     *               will be done and the DONE result will be returned.
     *
     * @param a The Action to execute.
     * @return Action quit state (DONE in case of success or ERROR in case of an unexpected error)
     */
    private int runAction(final Action a) {
        if ((a.getOp().equals(ALL_SYNC)) || (a.getOp().equals(ALL_ASYNC))) {
            a.setCurrentState(ActionState.DONE);
            return a.getCurrentState();
        }

        try {
            a.setCurrentState(ActionState.RUNNING);
            a.setCurrentState(Actions.run(a.getOp(), a.getArgs()));

            if (a.getCurrentState() == ActionState.ERROR)
                ReflexAutoban.getInstance().getLog().warn("Action quit with ERROR state: '" + a.getStatement() + "'");
        } catch (final Exception ex) {
            ReflexAutoban.getInstance().getLog().warn("Failed to run action '" + a.getStatement() + "':");
            Misc.printStackTrace(ex);

            a.setCurrentState(ActionState.ERROR);
        }

        return a.getCurrentState();
    }

}

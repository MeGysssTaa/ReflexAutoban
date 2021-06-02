/*
 * Copyright 2021 German Vekhorev (DarksideCode)
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
import lombok.Setter;
import rip.reflex.autoban.util.action.func.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Executable action object used in 'actions' list in config.
 */
public class Action {

    /**
     * Character that identifies that a part of String
     * is a flow controller and not a regular argument
     */
    private static final String FLOW_IDENTIFIER = "@";

    /**
     * The statement to execute
     */
    @Getter
    private final String statement;

    /**
     * The current state of this action
     */
    @Getter @Setter
    private int currentState;

    /**
     * The operation type this action performs
     */
    @Getter
    private final String op;

    /**
     * The array of flow controllers given
     */
    @Getter
    private final String[] flow;

    /**
     * The array of arguments given
     */
    @Getter
    private final String[] args;

    /**
     * Construct an Action object with all the needed ready-to-use stuff
     * from the given statement string.
     *
     * @param statement The statement string to construct an Action from
     */
    public Action(final String statement) {
        if ((this.statement = statement).length() < 2)
            throw new IllegalArgumentException("Not a statement: '" + statement + "'");

        String[] n = statement.split(" ");
        op = n[0].toLowerCase();

        if (n.length < 2)
            flow = args = new String[0];
        else {
            // TODO: Make this not look that shitty
            final List<String> flow = new ArrayList<>(),
                               args = new ArrayList<>();

            for (int i = 1; i < n.length; i++) {
                String s = n[i];
                ((s.startsWith(FLOW_IDENTIFIER)) ? flow : args).add(fn(s));
            }

            flow.toArray(this.flow = new String[flow.size()]);
            args.toArray(this.args = new String[args.size()]);
        }
    }

    /**
     * Transform the given string to value (function result), if the string
     * is a function string, or keep it as is otherwise.
     *
     * @param s The string to transform.
     * @return The given string, with functions replaced with values (function results)
     */
    private String fn(final String s) {
        return Functions.execute(s);
    }

    /**
     * Check the action for presence of the given flow controller
     *
     * @param f The flow controller to look for
     * @return true if such flow controller is present, false otherwise
     */
    public boolean hasFlow(final String f) {
        return flow.length > 0 && Arrays.stream(flow).filter(f::equalsIgnoreCase).findAny().orElse(null) != null;
    }

    /**
     * Returns the flow controller located at the specified index.
     *
     * @param index The index to look for flow controllers at.
     * @throws ArrayIndexOutOfBoundsException If the specified index doesn't meet
     *                                        the '0<=index<flow.length' condition
     * @return String, the flow controller located at the given index
     */
    public String getFlowAt(final int index) {
        if ((index < 0) || (index >= flow.length))
            throw new ArrayIndexOutOfBoundsException(String.format("Flow index: %s, flow length: %s", index, flow.length));
        return flow[index];
    }

    /**
     * Check the action for presence of the given argument
     *
     * @param arg The argument to look for
     * @return true if such argument is present, false otherwise
     */
    public boolean hasArg(final String arg) {
        return args.length > 0 && Arrays.stream(args).filter(arg::equalsIgnoreCase).findAny().orElse(null) != null;
    }

    /**
     * Returns the argument located at the specified index.
     *
     * @param index The index to look for arguments at.
     * @throws ArrayIndexOutOfBoundsException If the specified index doesn't meet
     *                                        the '0<=index<args.length' condition
     * @return String, the argument located at the given index
     */
    public String getArgAt(final int index) {
        if ((index < 0) || (index >= args.length))
            throw new ArrayIndexOutOfBoundsException(String.format("Arg index: %s, args length: %s", index, args.length));
        return args[index];
    }

}

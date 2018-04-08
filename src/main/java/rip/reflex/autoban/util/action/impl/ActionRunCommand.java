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

package rip.reflex.autoban.util.action.impl;

import org.bukkit.Bukkit;
import rip.reflex.autoban.util.Strings;
import rip.reflex.autoban.util.action.AbstractAction;
import rip.reflex.autoban.util.action.Op;

/**
 * Construct and run a Bukkit command by merging
 * all the given arguments in one string.
 */
public class ActionRunCommand extends AbstractAction {

    public ActionRunCommand() {
        super(Op.RUN_COMMAND, 1);
    }

    @Override
    protected boolean run0(final String[] args) throws Exception {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Strings.join(0, args, " "));
        return true;
    }

}

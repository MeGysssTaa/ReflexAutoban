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

package rip.reflex.autoban.util.action.impl;

import org.bukkit.entity.Player;
import rip.reflex.api.Cheat;
import rip.reflex.api.ReflexAPI;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.misc.Stats;
import rip.reflex.autoban.util.action.AbstractAction;
import rip.reflex.autoban.util.action.Op;

import java.util.Arrays;

/**
 * Ignore any further violations or PKs the given player might get.
 */
public class ActionIgnoreNext extends AbstractAction {

    public ActionIgnoreNext() {
        super(Op.IGNORE_NEXT, 1);
    }

    @Override
    protected boolean run0(final String[] args) throws Exception {
        final Player p = Misc.findPlayer(args[0]);

        if (p == null) {
            ReflexAutoban.getInstance().getLog().warn("Unreachable player '" + args[0] + "'");
            return false;
        }

        Stats.of(p).setIgnoring(true);

        // Tell Reflex to not check the given player anymore
        ReflexAPI r = ReflexAutoban.getInstance().reflex();
        Arrays.stream(Cheat.values()).forEach(c -> r.allowHacking(p, c, true));

        return true;
    }

}

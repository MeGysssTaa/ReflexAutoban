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
import rip.reflex.autoban.util.io.Files;
import rip.reflex.autoban.util.misc.Misc;
import rip.reflex.autoban.util.misc.Stats;
import rip.reflex.autoban.util.str.VersionConverter;
import rip.reflex.autoban.util.action.AbstractAction;
import rip.reflex.autoban.util.action.Op;

import java.util.Arrays;

/**
 * Save all logs for all checks the given player has scored so far
 * as a fancy and readable string in the file specified in config.
 */
public class ActionSaveLogs extends AbstractAction {

    public ActionSaveLogs() {
        super(Op.SAVE_LOGS, 2);
    }

    @Override
    protected boolean run0(final String[] args) throws Exception {
        final Player p = Misc.findPlayer(args[0]);
        final String id = args[1];

        if (p == null) {
            ReflexAutoban.getInstance().getLog().warn("Unreachable player '" + args[0] + "'");
            return false;
        }

        final Stats stats = Stats.of(p);

        final ReflexAPI r = ReflexAutoban.getInstance().reflex();
        final StringBuilder logs = new StringBuilder();

        logs.append(id).append(" | Player: ").append(p.getName()).append(" (").append(
                r.getMCBrand(p)).append(" ").append(VersionConverter.pv2str(r.getProtocolVersion(p))).append("):\n");
        Arrays.stream(Cheat.values()).filter(stats::hasLogs).forEach(c -> {
            StringBuilder logs4c = new StringBuilder("\t" + c.name() + " logs:\n");

            stats.getLogs(c).forEach(log -> logs4c.append("\t\t").append(log).append('\n'));
            logs.append(logs4c.toString()).append('\n');
        });

        Files.write(ReflexAutoban.getInstance().getCurrentBansFile(), logs.append('\n').toString());

        return true;
    }

}

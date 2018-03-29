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

package rip.reflex.autoban.command.impl;

import org.bukkit.command.CommandSender;
import rip.reflex.autoban.banwave.BanWave;
import rip.reflex.autoban.command.base.ArgumentType;
import rip.reflex.autoban.command.base.CommandArguments;
import rip.reflex.autoban.command.base.CommandConditions;
import rip.reflex.autoban.command.base.ReflexSubCommand;
import rip.reflex.autoban.util.Strings;

import java.util.concurrent.TimeUnit;

/**
 * Current ban wave monitoring and management tools.
 */
public class CommandRabBanwave extends ReflexSubCommand {

    public CommandRabBanwave() {
        super("banwave", rab.getLang("commands.ban_wave.description"), "rab.command.banwave");
    }

    @Override
    protected void check(CommandConditions cc) {
        cc.checkArgument(rab.getReflexMsg("cmdInvalidOperation"), 0, ArgumentType.STRING);
        cc.checkOptionalArg(rab.getReflexMsg("cmdInvalidPlayer"), 1, ArgumentType.STRING);
    }

    @Override
    protected void execute(CommandSender cs, CommandArguments args) {
        final BanWave bw = rab.getCurrentBanWave();

        switch (args.get(0).toLowerCase()) {
            case "run":
                cs.sendMessage(Strings.format(rab.getLang("commands.ban_wave.run")));
                bw.run(false);

                break;

            case "info":
                final StringBuilder players = new StringBuilder();

                for (final String player : bw.getPlayers())
                    players.append(player).append(", ");

                // Milliseconds to next regular schedule
                long millis = bw.getNextSchedule() - System.currentTimeMillis();
                long msToMin = TimeUnit.MILLISECONDS.toMinutes(millis);

                String pstr = (players.length() == 0) ? "..." : players.substring(0, players.length() - 2);
                cs.sendMessage(Strings.format(rab.getLang("commands.ban_wave.info"), "%time%", String.format("%d min, %d s",
                    msToMin, TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(msToMin)),
                    "%players_number%", Integer.toString(bw.size()), "%players_list%", pstr));
                break;

            case "clear":
                cs.sendMessage(Strings.format(rab.getLang("commands.ban_wave.clear")));
                bw.clear();

                break;

            case "addplayer": {
                String target = args.get(1);

                cs.sendMessage(Strings.format(rab.getLang("commands.ban_wave.addplayer"), "%player%", target));
                bw.addPlayer(args.get(1));

                break;
            }

            case "removeplayer": {
                String target = args.get(1);

                cs.sendMessage(Strings.format(rab.getLang("commands.ban_wave.removeplayer"), "%player%", target));
                bw.removePlayer(args.get(1));

                break;
            }
        }
    }

}

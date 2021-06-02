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

package rip.reflex.autoban.command.impl;

import org.bukkit.command.CommandSender;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.api.ReflexAutobanAPI;
import rip.reflex.autoban.command.base.CommandArguments;
import rip.reflex.autoban.command.base.CommandConditions;
import rip.reflex.autoban.command.base.ReflexCommand;
import rip.reflex.autoban.command.base.ReflexSubCommand;
import rip.reflex.autoban.util.str.Strings;

/**
 * The main, base ReflexAutoban command. Used without arguments,
 * prints ReflexAutoban installation details. Otherwise attempts
 * to call the subcommand specified at argument index 0, with
 * args specified at indexes 1 and higher.
 */
public class CommandRab extends ReflexCommand {

    /**
     * Register all other ReflexAutoban commands (subcommands).
     */
    public CommandRab() {
        super("rab", "rab.command.rab", Strings.color(rab.getReflexMsg("noPermissions")));

        addSubCommand(new CommandRabBanwave());

        addSimpleSubCommand("help", rab.getReflexRaw("cmdDescReflexHelp"), "rab.command.help", getNoperm(), this::helpMessage);
        addSimpleSubCommand("reload", rab.getReflexRaw("cmdDescReflexReload"), "rab.command.reload", getNoperm(), this::reloadCommand);

        setSubcmderr(Strings.replace(rab.getReflexMsg("cmdUnknownSubcmd"), "/reflex", "/rab"));
    }

    /**
     * Print ReflexAutoban installation info to the sender.
     */
    @Override
    protected void check(CommandConditions cc) {
        cc.getSender().sendMessage(Strings.wrap(
            "&9&l「&r&bRAB&9&l」" +
              "&rV: &9" + ReflexAutoban.getInstance().getDescription().getVersion() + "&7・" +
              "&rPAPI: &9" + ReflexAutobanAPI.PUBLIC_API_VERSION + "&7・" +
              "&rRAPI: &9" + ReflexAutoban.API_VERSION
        ).colourizeAndGet()); cc.setOk(false);
    }

    @Override
    protected void execute(CommandSender cs, CommandArguments args) { }

    /**
     * Reload ReflexAutoban configuration.
     */
    private void reloadCommand(CommandSender cs, CommandArguments args) {
        ReflexAutoban.getInstance().reloadConfig();
        cs.sendMessage(rab.getReflexMsg("configReloaded"));
    }

    /**
     * Print a list of all ReflexAutoban commands the sender can use.
     */
    private void helpMessage(CommandSender cs, CommandArguments args) {
        cs.sendMessage(rab.getReflexMsg("reflexHelpTitle"));
        cs.sendMessage("  §9rab§7 - §r" + rab.getReflexRaw("cmdDescReflex"));

        for (final ReflexSubCommand sub : subCommands)
            if (cs.hasPermission(sub.getPermission()))
        cs.sendMessage(Strings.wrap("  &9" + sub.getName() + " &7- &r" + sub.getDesc()).colourizeAndGet());
    }

}

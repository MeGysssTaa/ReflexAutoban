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

package rip.reflex.autoban.command.base;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class ReflexCommand implements CommandExecutor {

    @Getter
    private final String name, permission, noperm;

    protected final List<ReflexSubCommand> subCommands = new ArrayList<>();
    private final List<String> permList = new ArrayList<>();

    protected static ReflexAutoban rab = ReflexAutoban.getInstance();

    @Getter @Setter
    private String subcmderr;

    @Setter @Getter
    private boolean hide = ReflexAutoban.getInstance().getSetting("hide", false);

    public ReflexCommand(String name, String permission, String noperm) {
        this.name = name;
        this.permission = permission;
        this.noperm = noperm;
    }

    public static void register(ReflexCommand cmd) {
        cmd.register();
    }

    protected abstract void check(CommandConditions cc);

    protected abstract void execute(CommandSender cs, CommandArguments args);

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ReflexAutoban.getInstance(), () -> {
            if ((cs instanceof Player) && (!(cs.isOp())) && (hide)) {
                boolean any = false;

                for (final String perm : permList)
                    if (any = cs.hasPermission(perm)) break;
                if (!(any)) {
                    cs.sendMessage(SpigotConfig.unknownCommandMessage);
                    return;
                }
            }

            try {
                onCommandAsync(cs, command, s, args);
            } catch (final Exception ex) {
                cs.sendMessage(Strings.color("Error executing command. See console for details"));

                System.out.println("Error executing command '" + Strings.join(0, args, " ") + "'");
                ex.printStackTrace();
            }
        });

        return true;
    }

    public void onCommandAsync(CommandSender cs, Command cmd, String label, String[] sargs) {
        final CommandArguments args = new CommandArguments(sargs);

        if (subCommands.isEmpty() || sargs.length == 0) {
            final CommandConditions cc = new CommandConditions(args, cs);

            check(cc);

            if (!cc.isOk()) return;
            if (!cs.hasPermission(permission)) {
                cs.sendMessage(hide ? SpigotConfig.unknownCommandMessage : noperm);
                return;
            }

            execute(cs, args);

            return;
        }

        final ReflexSubCommand subCommand = args.has(0) ? subCommands
                .stream().filter(rsc -> args.get(0).equalsIgnoreCase(rsc.getName()))
                .findFirst().orElse(null) : null;

        if (subCommand == null) {
            if (subcmderr != null)
                cs.sendMessage(subcmderr);
            return;
        }

        if (!cs.hasPermission(subCommand.getPermission())) {
            cs.sendMessage(hide ? SpigotConfig.unknownCommandMessage : subCommand.getNoperm());
            return;
        }

        final CommandConditions cc = new CommandConditions(args.cut(), cs);

        subCommand.check(cc);

        if (!cc.isOk())
            return;
        subCommand.execute(cs, args);
    }

    public void addSubCommand(ReflexSubCommand rsc) {
        subCommands.add(rsc);

        if (rsc.getNoperm() == null)
            rsc.setNoperm(noperm);
        permList.add(rsc.getPermission());
    }

    public void addSimpleSubCommand(String name, String desc, String permission, String noperm, BiConsumer<CommandSender, CommandArguments> consumer) {
        addSubCommand(new ReflexSubCommand(name, desc, permission, noperm) {
            @Override
            protected void check(CommandConditions cc) {
            }

            @Override
            protected void execute(CommandSender cs, CommandArguments args) {
                consumer.accept(cs, args);
            }
        });

    }

    private void register() {
        ReflexAutoban.getInstance().getCommand(name).setExecutor(this);
    }

}

/*
 * Copyright 2019 DarksideCode
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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.reflex.autoban.util.str.Strings;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class CommandConditions {

    private final CommandArguments args;

    @Getter
    private final CommandSender sender;

    @Getter @Setter
    private boolean ok = true;

    public void checkBool(boolean b, String message) {
        if (!ok || !b)
            return;
        error(message);
    }

    public void usage(String message) {
        if (!ok || args.has(0))
            return;
        error(message);
    }

    public void isPlayer(String message) {
        if (!ok || sender instanceof Player)
            return;
        error(message);
    }

    public void check(String errormsg, Predicate<CommandArguments> pred) {
        if (!ok || pred.test(args))
            return;
        error(errormsg);
    }

    public void checkArgument(String msg, int i, ArgumentType type) {
        if (!ok) return;
        if (args.has(i) && type.getPredicate().test(args.get(i)))
            return;
        error(msg);
    }

    public void checkOptionalArg(String msg, int i, ArgumentType type) {
        if (!ok) return;
        if (!args.has(i) || type.getPredicate().test(args.get(i)))
            return;
        error(msg);
    }

    private void error(String msg) {
        sender.sendMessage(Strings.color(msg));
        ok = false;
    }

}

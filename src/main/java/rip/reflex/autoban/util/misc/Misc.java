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

package rip.reflex.autoban.util.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.action.Action;
import rip.reflex.autoban.util.action.ActionInterpreter;
import rip.reflex.autoban.util.placeholders.PlaceholderApplicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Various utilities which haven't got their
 * place in separate utility classes ._.
 */
public class Misc {

    private static final int UUID_LEN = 36;

    private static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    /**
     * Print the given exception's stacktrace if dev_mode is enabled.
     * @param ex The exception stacktrace of which should be printed
     */
    public static void printStackTrace(final Exception ex) {
        if (ReflexAutoban.getInstance().getSetting("dev_mode", false)) ex.printStackTrace();
    }

    /**
     * Find online player with the name matching the given.
     * Unlike Bukkit#getPlayer, this method uses strict matching, so we
     * can be sure byName("Name123") will not return us a player with
     * name "Name12", "Name" or something like that.
     *
     * @param name The name to search a player by.
     * @return Online Player whose name match
     */
    public static Player findPlayer(final String name) {
        return Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Check if the given string is a number
     *
     * @param s The string to check
     * @return true if the given string is a valid number, false otherwise
     */
    public static boolean isNumber(final String s) {
        for (final char c : s.toCharArray())
            if (!(Character.isDigit(c))) return false;
        return true;
    }

    /**
     * Check if the given string is a boolean
     *
     * @param s The string to check
     * @return true if the given string is a valid boolean, false otherwise
     */
    public static boolean isBoolean(final String s) {
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
    }

    /**
     * Check if the given string is an UUID
     *
     * @param s The string to check
     * @return true if the given string is a valid UUID, false otherwise
     */
    public static boolean isUUID(final String s) {
        return s.length() == UUID_LEN && s.matches(UUID_PATTERN);
    }

    /**
     * Interpret the given actions list at the given player,
     * applying the given custom placeholders (optinal).
     *
     * @param list The list of actions to interpret.
     * @param p The player to execute the actions at.
     * @param placeholderMap Optional map of additional placeholders (0, 2... - keys; 1, 3... - values)
     */
    public static void execute(final List<String> list, final Player p, final String... placeholderMap) {
        final List<Action> actions = new ArrayList<>();

        list.forEach(a ->
            actions.add(new Action(PlaceholderApplicator.apply(
                    a, p, ReflexAutoban.getInstance().reflex(), placeholderMap
            )))
        );

        new ActionInterpreter(actions).run();
    }

}

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

package rip.reflex.autoban.util.placeholders;

import org.bukkit.entity.Player;
import rip.reflex.api.ReflexAPI;
import rip.reflex.autoban.banwave.MissingPlayer;
import rip.reflex.autoban.util.time.Now;
import rip.reflex.autoban.util.str.Strings;
import rip.reflex.util.performance.LagStatus;

/**
 * Used to apply Reflex and ReflexAutoban placeholders to
 * strings containing their codes (%placeholder_code%),
 * among with PlaceholderAPI placeholders (if present).
 */
public class PlaceholderApplicator {

    /**
     * Apply all the placeholders to the given string.
     * Additional (extra) placeholders are also supported. Example:
     *      apply(..., "%custom_placeholder%", "value_for_custom_placeholder")
     */
    public static String apply(final String s, final Player p, final ReflexAPI reflex, final String... other) {
        // Apply standard ReflexAutoban placeholders
        final boolean missing = p instanceof MissingPlayer;
        final Strings.WrappedString ws = Strings.wrap(s).
            replace("%player%", p.getName()).
            replace("%tps%", reflex.getTps()).
            replace("%break%", "\n").
            replace("%lp%", (missing) ? -1 : reflex.getLagPoints(p)).
            replace("%lagstatus%", (missing) ? LagStatus.NOT_ESTIMATED : reflex.getLagStatus(p).fancyName()).
            replace("%date%", Now.date()).
            replace("%time%", Now.time());

        // Apply additional (extra) placeholders if specified
        Strings.formatExtra(ws, other);

        // Apply PlaceholderAPI's placeholders and color codes
        return ws.placeholderAPI(p).colourizeAndGet();
    }

}

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

package rip.reflex.autoban.util.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook {

    /**
     * Cache presence result to avoid multiple execution of the slow Class#forName check
     */
    private static Boolean present = null;

    public static boolean isPresent() {
        if (present == null) {
            try {
                Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                present = true;
            } catch (final ClassNotFoundException ex) {
                present = false;
            }
        }

        return present;
    }

    /**
     * Apply PlaceholderAPI's placeholders to the given string
     */
    public static String replace(final String orig, final Player p) {
        return (isPresent()) ? PlaceholderAPI.setPlaceholders(p, orig) : orig;
    }

}

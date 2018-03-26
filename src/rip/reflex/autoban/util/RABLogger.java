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

package rip.reflex.autoban.util;

import lombok.RequiredArgsConstructor;
import me.flo.flogger.types.FlogLevel;
import rip.reflex.autoban.ReflexAutoban;

/**
 * Used for logging.
 */
@RequiredArgsConstructor
public class RABLogger {

    /**
     * The plugin instance to log to
     */
    private final ReflexAutoban rab;

    /**
     * Format and log the given string using Reflex Logger in case
     * we have already hooked into its API, or using the default
     * Bukkit Logger otherwise, with level INFO. Then self return
     *
     * @param s The string to log
     */
    public RABLogger info(String s) {
        s = format(s);
        if (rab.isReflexReady())
            rab.reflex().log(FlogLevel.INFO, s);
        else rab.getLogger().info(s);
        return this;
    }

    /**
     * Format and log the given string using Reflex Logger in case
     * we have already hooked into its API, or using the default
     * Bukkit Logger otherwise, with level WARN. Then self return
     *
     * @param s The string to log
     */
    public RABLogger warn(String s) {
        s = format(s);
        if (rab.isReflexReady())
            rab.reflex().log(FlogLevel.WARN, s);
        else rab.getLogger().warning(s);
        return this;
    }

    /**
     * Format and log the given string using Reflex Logger in case
     * we have already hooked into its API, with level DEBUG.
     * If Reflex API is not ready yet or if dev_mode is disabled,
     * nothing will be logged.
     *
     * @param s The string to log
     */
    public RABLogger debug(String s) {
        if (rab.isReflexReady())
            rab.reflex().log(FlogLevel.DEBUG, format(s));
        return this;
    }

    /**
     * Do the following magic with the given string:
     *     - add prefix from config
     *     - translate inner encodings (like {ban_bc}, {pk_notify}, etc.)
     *     - translate color codes
     *     - set placeholders (ReflexAutoban's and PlaceholderAPI's)
     *
     * @param s The string to format
     * @return The formatted string
     */
    private String format(final String s) {
        // TODO > Everything...
        return Strings.removeColorCodes(s);
    }

}

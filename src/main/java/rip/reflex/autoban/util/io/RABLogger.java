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

package rip.reflex.autoban.util.io;

import me.flo.flogger.Flogger4J;
import me.flo.flogger.impl.AsyncLogWriterPublisher;
import me.flo.flogger.impl.DailyLogFilePublisher;
import me.flo.flogger.impl.LogConsolePublisher;
import me.flo.flogger.types.FlogFormatter;
import me.flo.flogger.types.FlogLevel;
import me.flo.flogger.types.FlogRecord;
import org.bukkit.Bukkit;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.str.Strings;

/**
 * Used for logging.
 */
public class RABLogger {

    /**
     * The plugin instance to log to
     */
    private final ReflexAutoban rab;

    /**
     * Awesome Flo's logger. Actually, IT is going to be used
     * for logging, with RABLogger being only a convenience class.
     */
    private final Flogger4J flogger;

    /**
     * Initialize RABLogger and attach to the FLogger API.
     * @param rab Main class instance.
     */
    public RABLogger(final ReflexAutoban rab) {
        this.rab = rab;

        try {
            flogger = new Flogger4J("ReflexAutoban");
            flogger.setFormatter(r -> "(" + r.getFlogger().getName() + "/" + r.getLevel() + ") | " + r.getMessage());

            if (rab.getSetting("make_logs", true)) {
                final DailyLogFilePublisher dlogp = new DailyLogFilePublisher(rab.getLocal("logs/")) {
                    @Override
                    public void handle(FlogRecord record, FlogFormatter formatter) {
                        super.handle(record, formatter);
                    }
                };

                flogger.addPublisher(new AsyncLogWriterPublisher(dlogp, 2500) {
                    @Override
                    public void handle(FlogRecord record, FlogFormatter formatter) {
                        super.handle(record, formatter);
                    }

                    @Override
                    protected void startAsync(Runnable r) {
                        Bukkit.getScheduler().runTaskAsynchronously(rab, r);
                    }
                }.level(FlogLevel.DEBUG));
            }

            flogger.addPublisher(new LogConsolePublisher().level((rab.getSetting("dev_mode", false)) ? FlogLevel.DEBUG : FlogLevel.INFO));
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to initialize RABLogger", ex);
        }
    }

    /**
     * Format and log the given string with level INFO.
     * @param s The string to log
     */
    public RABLogger info(final String s) {
        flogger.info(format(s));
        return this;
    }

    /**
     * Format and log the given string with level WARN.
     * @param s The string to log
     */
    public RABLogger warn(final String s) {
        flogger.warning(format(s));
        return this;
    }

    /**
     * Format and log the given string with level DEBUG.
     * @param s The string to log
     */
    public RABLogger debug(final String s) {
        flogger.debug(format(s));
        return this;
    }

    /**
     * Format the given string.
     *
     * @param s The string to format
     * @return The formatted string
     */
    private String format(final String s) {
        return Strings.removeColorCodes(s);
    }

}

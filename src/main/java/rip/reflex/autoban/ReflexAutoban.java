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

package rip.reflex.autoban;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.reflex.api.ReflexAPI;
import rip.reflex.autoban.api.ReflexAutobanAPIProvider;
import rip.reflex.autoban.bananimation.BanAnimations;
import rip.reflex.autoban.banwave.BanWave;
import rip.reflex.autoban.command.base.ReflexCommand;
import rip.reflex.autoban.command.impl.CommandRab;
import rip.reflex.autoban.util.*;
import rip.reflex.autoban.util.action.Actions;
import rip.reflex.autoban.util.action.func.Functions;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReflexAutoban extends JavaPlugin {

    /**
     * The Reflex API version we can work with
     */
    public static final int API_VERSION = 3;

    /**
     * The maximal amount of threads our asynchronous executor is allowed to produce.
     * Note that the fact it is the MAXIMAL thread count doesn't mean it's the USUAL
     * thread count. The limit only exist in order to prevent infinite thread
     * reduplication in case of some critical performance issue.
     */
    private static final int MAX_THREAD_COUNT = 12;

    /**
     * Public access for non-static methods of this class
     */
    @Getter
    private static ReflexAutoban instance;

    /**
     * The Reflex API instance.
     * Used to access Reflex API
     */
    @Setter
    private ReflexAPI reflex;

    /**
     * The logger. Used for logging.
     */
    @Getter
    private RABLogger log;

    /**
     * The current ban wave storing and allowing to
     * access and manage the current ban queue.
     */
    @Getter
    private BanWave currentBanWave = new BanWave();

    /**
     * Used for Bukkit-away asynchronous code execution.
     */
    @Getter
    private ExecutorService executor;

    /**
     * The directory where all the ReflexAutoban files are saved.
     */
    @Getter
    private String workingDir;

    /**
     * Initialize basic stuff
     */
    @Override
    public void onEnable() {
        try {
            // Init working dir
            workingDir = Strings.replace(getConfig().getString("working_directory",
                    "%default_data_folder%"), "%default_data_folder%", getDataFolder().getAbsolutePath());

            // Init logger and assign instance
            log = new RABLogger(instance = this);
            log.info("Starting ReflexAutoban v" + getDescription().getVersion() + " (API: " + API_VERSION + ")");

            executor = Executors.newFixedThreadPool(MAX_THREAD_COUNT);

            // Create a copy of the default configuration if one doesn't exist
            saveDefaultConfig();

            // Register events
            new EventListener(this);

            // Misc
            BanAnimations.init();
            Functions.init();
            Actions.init();
        } catch (final Exception ex) {
            // Looks like everything is very bad
            System.err.println("[ReflexAutoban] FATAL ERROR DURING STARTUP");
            System.err.println("[ReflexAutoban] REFLEX AUTOBAN SHUTTING DOWN");

            Bukkit.getPluginManager().disablePlugin(this);

            throw new RuntimeException("Fatal error during ReflexAutoban startup", ex);
        }

        log.info("Almost done!");
    }

    /**
     * Initialize everything that wasn't initialized on enable
     */
    public void finishInit() {
        // Register commands
        ReflexCommand.register(new CommandRab());

        // Start asynchronous ban wave schedule
        int bwPeriod = getSetting("ban_wave_period", 15) /* minutes */ * 60 /* seconds */ * 20 /* ticks */;
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> currentBanWave.run(true), 1, bwPeriod);

        // Initialize ReflexAutoban API
        ReflexAutobanAPIProvider.init();
        log.info("ReflexAutoban has fully started");

        if (getSetting("dev_mode", false))
            log.info("Developer mode is on! Be aware of many random non-sense debug messages and errors!");
    }

    /**
     * Retreive a setting from the ReflexAutoban configuration file
     * and cast it to the wanted type automatically, or return the
     * specified default value in case of some kind of a failure.
     *
     * @param path The path to look for the setting in
     * @param def The default value to return in case of failure
     *
     * @return the value of the setting located at the given path
     */
    public <T> T getSetting(final String path, final T def) {
        if (!(getConfig().contains(path)))
            return def;

        try {
            //noinspection unchecked
            return (T) getConfig().get(path);
        } catch (final Exception ex) {
            return def;
        }
    }

    /**
     * Retreive a file located in ReflexAutoban working directory.
     *
     * @param s The name of the file to look for.
     * @return The File the absolute path of which is something like {working_dir}/s
     *         where s is the given string - the name of the file to look for.
     */
    public File getRabFile(final String s) {
        final String wd = workingDir;
        return Files.getFile((wd.endsWith("/") ? wd : wd + "/") + s);
    }

    public File getCurrentBansFile() {
        return getRabFile("bans/" + Now.date() + ".txt");
    }

    /**
     * Retreive a string setting located at 'lang.k' where
     * 'k' is the name of the language variable to retreive.
     * The result is equal to (and is retreived as):
     *     (String) getSetting("lang." + k, k)
     * In case the specified language variable is undefined,
     * its untranslated (raw key) version will be returned.
     *
     * @param k The name of the language variable to obtain
     * @return the value of the language setting located at `lang.k`
     *         where 'k' is the name of the language variable to retreive,
     *         with no formatting nor color codes applied.
     */
    public String getLang(final String k) {
        return getSetting("lang." + k, k);
    }

    /**
     * Retreive ReflexAutoban's prefix to display in its message.
     * The result is equal to (and is retreived as):
     *     getLang("prefix")
     *
     * @return ReflexAutoban's prefix unformatted string
     */
    public String getPrefix() {
        return getLang("prefix");
    }

    /**
     * Obtain a translation from the selected Reflex theme at
     * the given key, adding ReflexAutoban's prefix to it and format.
     *
     * @param key The key to look for translations at.
     * @return the translated string from the selected Reflex theme,
     *         with ReflexAutoban's prefix added and colors applied.
     */
    public String getReflexMsg(final String key) {
        return (isReflexReady()) ? Strings.replace(reflex().translateAndFormat(getPrefix(), key), "Reflex", "ReflexAutoban") : key;
    }

    /**
     * Obtain a translation from the selected Reflex theme at
     * the given key, with no any kind of formatting applied.
     *
     * @param key The key to look for translations at.
     * @return the translated string from the selected Reflex theme.
     */
    public String getReflexRaw(final String key) {
        return (isReflexReady()) ? Strings.replace(reflex().translateThemeString(key), "Reflex", "ReflexAutoban") : key;
    }

    /**
     * Check if Reflex API is loaded and ready to use.
     * @return true if Reflex API is loaded and ready to use, false otherwise.
     */
    public boolean isReflexReady() {
        try {
            return reflex() != null;
        } catch (final UnsupportedAPIException ex) {
            return false;
        }
    }

    /**
     * Make sure the Reflex API is loaded and available, and that
     * it's version is equal to the API version of ReflexAutoban.
     *
     * In case of API versions mismatch, an UnsupportedAPIException
     * is raised and ReflexAutoban is forcefully shutted down.
     *
     * @throws UnsupportedAPIException In case of ReflexAutoban and Reflex API versions mismatch
     * @return the ReflexAPI instance if it's loaded and ready, null otherwise
     */
    public ReflexAPI reflex() {
        if (reflex == null) return null;
        if (reflex.apiVersion() != API_VERSION) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new UnsupportedAPIException(reflex.apiVersion());
        }

        return reflex;
    }

}

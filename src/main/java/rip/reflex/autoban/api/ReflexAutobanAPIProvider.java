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

package rip.reflex.autoban.api;

import org.bukkit.Bukkit;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.api.event.ReflexAutobanLoadEvent;

public class ReflexAutobanAPIProvider {

    private static ReflexAutobanAPI api = null;
    private static boolean init = false;

    public static ReflexAutobanAPI getAPI() {
        if ((!(init)) || (api == null))
            throw new IllegalStateException("ReflexAutoban API has not been initialized yet!");
        return api;
    }

    public static void init() {
        if (init)
            throw new IllegalStateException("Cannot initialize ReflexAutoban API twice!");

        api = new ReflexAutobanAPI();
        init = true;

        Bukkit.getPluginManager().callEvent(new ReflexAutobanLoadEvent
                (ReflexAutoban.getInstance().getDescription().getVersion(), ReflexAutobanAPI.PUBLIC_API_VERSION));
        ReflexAutoban.getInstance().getLog().info("ReflexAutoban API v" + ReflexAutobanAPI.PUBLIC_API_VERSION + " has been loaded.");
    }

}

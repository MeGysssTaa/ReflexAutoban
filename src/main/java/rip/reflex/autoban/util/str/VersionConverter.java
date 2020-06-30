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

package rip.reflex.autoban.util.str;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An utility to convert protocol version ints to
 * readable and human-understandable Minecraft versions.
 * And maybe vise-versa. Maybe.
 */
public class VersionConverter {

    /**
     * Protocol version int to "X.Y" version string map
     */
    private static final Map<Integer, String> map = new ConcurrentHashMap<>();

    /**
     * Returned in case of version conversion failure.
     */
    private static final String UNKNOWN = "<unknown version>";

    /**
     * Returns an easily understandable Minecraft version in X.Y format
     * (like 1.8, 1.12, etc.) based on the given protocol version int.
     *
     * @param protocolVersion The protocol version to transform.
     * @return an easily understandable Minecraft version in X.Y format.
     */
    public static String pv2str(int protocolVersion) {
        int bestMatched = -1;

        for (int pver : map.keySet())
            if (protocolVersion >= pver)
                bestMatched = pver;

        return map.getOrDefault(bestMatched, UNKNOWN);
    }

    static {
        // https://wiki.vg/Protocol_version_numbers
        map.put(  4,  "1.7");
        map.put( 47,  "1.8");
        map.put(107,  "1.9");
        map.put(210, "1.10");
        map.put(315, "1.11");
        map.put(335, "1.12");
        map.put(393, "1.13");
        map.put(477, "1.14");
        map.put(573, "1.15");
        map.put(735, "1.16");
    }

}

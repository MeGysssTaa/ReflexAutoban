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
    public static String pv2str(final int protocolVersion) {
        return map.getOrDefault(protocolVersion, UNKNOWN);
    }

    static {
        // wiki.vg
        map.put(  4,  "1.7");
        map.put( 47,  "1.8");
        map.put(107,  "1.9");
        map.put(210, "1.10");
        map.put(315, "1.11");
        map.put(335, "1.12");
    }

}

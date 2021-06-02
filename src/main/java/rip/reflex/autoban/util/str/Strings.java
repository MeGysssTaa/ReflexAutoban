/*
 * Copyright 2021 German Vekhorev (DarksideCode)
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

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import rip.reflex.autoban.ReflexAutoban;
import rip.reflex.autoban.util.placeholders.PlaceholderAPIHook;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Various utilities to simplify work with strings.
 */
public class Strings {

    private static final String BAN_ID_CHARSET = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";

    private static final Random random = new Random();
    private static final String COLOR_CODES = "&§";

    public static String generateBanID() {
        return "#" + randomComp(8, BAN_ID_CHARSET);
    }

    public static String randomComp(final int length, final String source) {
        final String[] s = new String[source.length()];

        for (int i = 0; i < source.length(); i++)
            s[i] = String.valueOf(source.charAt(i));
        return random(length, s);
    }

    public static String color(final String s) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String format(final String s, final String... other) {
        final WrappedString ws = wrap(ReflexAutoban.
                getInstance().getPrefix() + s).replace("%break%", "\n");
        // Apply additional (extra) placeholders if specified
        formatExtra(ws, other);
        return ws.colourizeAndGet();
    }

    public static void formatExtra(WrappedString ws, String[] other) {
        if ((other != null) && (other.length >= 2)) {
            if ((other.length % 2) != 0)
                throw new IllegalArgumentException("Illegal custom placeholders length (now a power of two): " + other.length);
            for (int i = 0; i < other.length; i += 2) ws.replace(other[i], other[i+1]);
        }
    }

    public static String random(final String... source) {
        return source[random.nextInt(source.length)];
    }

    public static String random(final int length, final String... source) {
        final StringBuilder s = new StringBuilder();

        while (s.length() < length)
            s.append(random(source));
        return s.toString();
    }

    public static String join(final int start, final String[] arr, final String separator) {
        final StringBuilder builder = new StringBuilder();

        for (int i = start; i < arr.length; i++)
            builder.append(arr[i]).append(separator);
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String removeColorCodes(final String s) {
        final char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (COLOR_CODES.contains(String.valueOf(chars[i]))) {
                if (i < (chars.length - 1))
                    chars[i + 1] = '\u0000';
                chars[i] = '\u0000';
            }
        }

        return replace(new String(chars), "\u0000", "");
    }

    public static String replace(final String str, final String target, final String replacement) {
        final int targetLength = target.length();

        if (targetLength == 0)
            return str;

        int idx2 = str.indexOf(target), cursor = 0;

        if (idx2 < 0)
            return str;

        final StringBuilder buffer = new
                StringBuilder((targetLength > replacement.length()) ? str.length() : str.length() * 2);
        while (idx2 > -1) {
            buffer.append(str, cursor, idx2);
            buffer.append(replacement);

            cursor = idx2 + targetLength;
            idx2 = str.indexOf(target, cursor);
        }

        buffer.append(str, cursor, str.length());

        return buffer.toString();
    }

    public static Strings.WrappedString wrap(final String s) {
        return new Strings.WrappedString(s);
    }

    public static Strings.WrappedString wrap(final StringBuilder s) {
        return new Strings.WrappedString(s.toString());
    }

    @AllArgsConstructor
    public static class WrappedString {
        private String value;

        public Strings.WrappedString replace(final String target, final Object replace) {
            value = Strings.replace(value, target, (replace instanceof String) ? (String) replace : String.valueOf(replace));
            return this;
        }

        public Strings.WrappedString replace(final String target, final Supplier<Object> supp) {
            if (!(value.contains(target)))
                return this;
            return replace(target, supp.get());
        }

        public Strings.WrappedString colourize() {
            value = color(value);
            return this;
        }

        public Strings.WrappedString noColors() {
            value = removeColorCodes(value);
            return this;
        }

        public String colourizeAndGet() {
            colourize();
            return get();
        }

        public Strings.WrappedString placeholderAPI(Player player) {
            value = PlaceholderAPIHook.replace(value, player);
            return this;
        }

        public String get() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}

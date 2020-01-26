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

package rip.reflex.autoban.command.base;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.reflex.autoban.util.str.Strings;

import java.util.Arrays;
import java.util.UUID;

@SuppressWarnings("MismatchedReadAndWriteOfArray")
@AllArgsConstructor
public class CommandArguments {

    private String[] args;

    public String get(int i) {
        return has(i) ? args[i] : null;
    }

    public String joinFrom(int start) {
        return has(start) ? Strings.join(start, args, " ") : null;
    }

    public int getInt(int i) {
        return has(i) ? Integer.valueOf(get(i)) : -1;
    }

    public Player getPlayer(int i) {
        return has(i) ? Bukkit.getPlayer(get(i)) : null;
    }

    public OfflinePlayer getOfflinePlayer(int i) {
        return has(i) ? Bukkit.getOfflinePlayer(get(i)) : null;
    }

    public UUID getUUID(int i) {
        return has(i) ? UUID.fromString(get(i)) : null;
    }

    public boolean has(int i) {
        return i >= 0 && i < args.length;
    }

    public CommandArguments cut() {
        args = Arrays.copyOfRange(args, 1, args.length);
        return this;
    }

    public Boolean getBool(int i) {
        return has(i) ? Boolean.valueOf(get(i)) : null;
    }

}

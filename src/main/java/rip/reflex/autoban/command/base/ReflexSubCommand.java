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

package rip.reflex.autoban.command.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import rip.reflex.autoban.ReflexAutoban;

@RequiredArgsConstructor
@AllArgsConstructor
public abstract class ReflexSubCommand {

    @Getter
    private final String name, desc, permission;

    @Setter @Getter
    private String noperm;

    protected static ReflexAutoban rab = ReflexAutoban.getInstance();

    protected abstract void check(CommandConditions cc);

    protected abstract void execute(CommandSender cs, CommandArguments args);

}

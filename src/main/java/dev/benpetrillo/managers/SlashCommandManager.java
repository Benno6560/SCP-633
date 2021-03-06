/*
 * Copyright © 2022 Ben Petrillo. All rights reserved.
 *
 * Project licensed under the MIT License: https://www.mit.edu/~amini/LICENSE.md
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * All portions of this software are available for public use, provided that
 * credit is given to the original author(s).
 */

package dev.benpetrillo.managers;

import dev.benpetrillo.Config;
import dev.benpetrillo.SCP633;
import dev.benpetrillo.commands.slash.*;
import dev.benpetrillo.types.ApplicationCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SlashCommandManager extends ListenerAdapter {

    public static ConcurrentHashMap<String, ApplicationCommand> commands;

    public SlashCommandManager(JDA jda, boolean print) {
        commands = new ConcurrentHashMap<>();
        commands.put(new BindCommand().getName(), new BindCommand());
        commands.put(new BranchesCommand().getName(), new BranchesCommand());
        commands.put(new HelpCommand().getName(), new HelpCommand());
        commands.put(new MissionCommand().getName(), new MissionCommand());
        commands.put(new SCPCommand().getName(), new SCPCommand());
        commands.put(new StopCommand().getName(), new StopCommand());
        commands.put(new StreamCommand().getName(), new StreamCommand());
        if (Boolean.parseBoolean(Config.get("DEPLOY-GLOBAL"))) {
            CommandListUpdateAction commands = jda.updateCommands();
            commands.addCommands(
                    new BindCommand().getCommandData(),
                    new BranchesCommand().getCommandData(),
                    new HelpCommand().getCommandData(),
                    new MissionCommand().getCommandData(),
                    new SCPCommand().getCommandData(),
                    new StopCommand().getCommandData(),
                    new StreamCommand().getCommandData()
            ).queue();
            SCP633.logger.info("All slash commands have been deployed.");
        } else if (Boolean.parseBoolean(Config.get("DELETE-GLOBAL"))) {
            CommandListUpdateAction commands = jda.updateCommands();
            commands.addCommands().queue();
            SCP633.logger.info("All global slash commands have been deleted.");
        }
        if (print) {
            RestAction<List<Command>> action = jda.retrieveCommands();
            System.out.println(action);
        }
    }
}

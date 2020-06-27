package dev.dewy.taribone.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.taribone.Taribone;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Objects;

public class FocusCommand extends Command
{
    public FocusCommand()
    {
        this.name = "focus";
        this.help = "Focus in on an account.";
        this.aliases = new String[] {"account", "acc", "fc"};
        this.guildOnly = false;
        this.arguments = "<NAME>";
    }

    @SuppressWarnings("all")
    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Taribone.CONFIG.taribone.subscriberId) || event.getAuthor().getId().equals(Taribone.CONFIG.taribone.operatorId)) && (event.getChannel().getId().equals(Taribone.CONFIG.taribone.channelId) || !event.getMessage().getChannelType().isGuild()))
        {
            try
            {
                if (Taribone.CONFIG.taribone.focusEnabled)
                {
                    String[] args = event.getArgs().split("\\s+");

                    if (Taribone.CONFIG.taribone.focused && !Taribone.CONFIG.taribone.focused)
                    {
                        return;
                    }

                    if (args[0].isEmpty())
                    {
                        return;
                    }

                    if (Taribone.CONFIG.taribone.focused && !Taribone.CONFIG.taribone.accounts.contains(args[0]))
                    {
                        return;
                    }

                    if (!Taribone.CONFIG.taribone.focused && args[0].equalsIgnoreCase(Taribone.CONFIG.taribone.tiedIgn))
                    {
                        Taribone.CONFIG.taribone.focused = true;

                        Taribone.saveConfig();

                        return;
                    }

                    Taribone.CONFIG.taribone.focused = false;

                    Taribone.saveConfig();
                }
            } catch (Throwable t)
            {
                t.printStackTrace();

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Error")
                        .setDescription("An exception occurred whilst executing this command. Debug information has been sent to Dewy to be fixed in following updates. Sorry about any inconvenience!")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn)
                        .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());

                Objects.requireNonNull(event.getJDA().getUserById(Taribone.CONFIG.taribone.operatorId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Taribone.CONFIG.taribone.subscriberId)).getName() + ")")
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a taribone focus command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}

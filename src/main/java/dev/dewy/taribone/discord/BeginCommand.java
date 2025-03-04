package dev.dewy.taribone.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.taribone.Taribone;
import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class BeginCommand extends Command
{
    public BeginCommand()
    {
        this.name = "begin";
        this.help = "Starts a new Taribone session.";
        this.guildOnly = false;
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if ((event.getAuthor().getId().equals(Taribone.CONFIG.taribone.subscriberId) || event.getAuthor().getId().equals(Taribone.CONFIG.taribone.operatorId)) && (event.getChannel().getId().equals(Taribone.CONFIG.taribone.channelId) || !event.getMessage().getChannelType().isGuild()) && Taribone.CONFIG.taribone.focused)
        {
            try
            {
                if (Minecraft.getMinecraft().world != null)
                {
                    event.reply(new EmbedBuilder()
                            .setTitle("**DQS** - Session In Progress")
                            .setDescription("A new Taribone session could not be started because one is already in progress!")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Taribone.CONFIG.taribone.tiedUuid)).toString())
                            .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                            .build());

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Taribone")
                        .setDescription("Starting a new Taribone session...")
                        .setColor(new Color(10144497))
                        .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Taribone.CONFIG.taribone.tiedUuid)).toString())
                        .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());

                Taribone.shouldExec = true;

                Thread.sleep(10000L);

                if (Minecraft.getMinecraft().world != null)
                {
                    Minecraft.getMinecraft().player.sendChatMessage("#cancel");

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Session Start Failed")
                        .setDescription("A new Taribone session could not be started because of a critical error. Debug info has been sent to Dewy and should be fixed soon.")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Taribone.CONFIG.taribone.tiedUuid)).toString())
                        .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                        .build());
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
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of a begin command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/pcSOd3K.png")
                                .build()).queue()));
            }
        }
    }
}

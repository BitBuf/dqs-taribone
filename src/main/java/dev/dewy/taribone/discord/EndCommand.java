package dev.dewy.taribone.discord;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import dev.dewy.taribone.Taribone;
import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class EndCommand extends Command
{
    public EndCommand()
    {
        this.name = "end";
        this.help = "Ends the running Taribone session.";
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
                            .setTitle("**DQS** - Ending")
                            .setDescription("Cancelling all processes and ending the currently active Taribone session...")
                            .setColor(new Color(15221016))
                            .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Taribone.CONFIG.taribone.tiedUuid)).toString())
                            .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                            .build());

                    Minecraft.getMinecraft().player.sendChatMessage("#cancel");
                    Thread.sleep(500L);

                    Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();

                    return;
                }

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - No Session In Progress")
                        .setDescription("No Taribone session could be ended because there are no active Taribone sessions.")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn, new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", Taribone.CONFIG.taribone.tiedUuid)).toString())
                        .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                        .build());
            } catch (Throwable t)
            {
                t.printStackTrace();

                event.reply(new EmbedBuilder()
                        .setTitle("**DQS** - Error")
                        .setDescription("An exception occurred whilst executing this command. Debug information has been sent to Dewy to be fixed in following updates. Sorry about any inconvenience!")
                        .setColor(new Color(15221016))
                        .setFooter("Focused on " + Taribone.CONFIG.taribone.tiedIgn)
                        .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                        .build());

                Objects.requireNonNull(event.getJDA().getUserById(Taribone.CONFIG.taribone.operatorId)).openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage(new EmbedBuilder()
                                .setTitle("**DQS** - Error Report (" + Objects.requireNonNull(event.getJDA().getUserById(Taribone.CONFIG.taribone.subscriberId)).getName() + ")")
                                .setDescription("A " + t.getClass().getSimpleName() + " was thrown during the execution of an end command.\n\n**Cause:**\n\n```" + t.getMessage() + "```")
                                .setColor(new Color(15221016))
                                .setAuthor("DQS " + Taribone.VERSION, null, "https://i.imgur.com/xTd3Ri3.png")
                                .build()).queue()));
            }
        }
    }
}

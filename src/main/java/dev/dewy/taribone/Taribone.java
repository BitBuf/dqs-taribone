package dev.dewy.taribone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import dev.dewy.taribone.discord.*;
import dev.dewy.taribone.utils.Config;
import net.daporkchop.lib.binary.oio.appendable.PAppendable;
import net.daporkchop.lib.binary.oio.reader.UTF8FileReader;
import net.daporkchop.lib.binary.oio.writer.UTF8FileWriter;
import net.daporkchop.lib.common.misc.file.PFiles;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

@Mod(modid = Taribone.MODID, name = Taribone.NAME, version = Taribone.VERSION)
public class Taribone
{
    public static final String MODID = "taribone";
    public static final String NAME = "Taribone";
    public static final String VERSION = "3.0.0";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final File CONIFG_FILE = new File(Minecraft.getMinecraft().gameDir.getAbsolutePath() + "/taribone.json");
    public static Config CONFIG;
    public static JDA jda;
    public static boolean shouldExec = false;
    private static Logger logger;

    public static synchronized void loadConfig()
    {
        Config config;

        if (PFiles.checkFileExists(CONIFG_FILE))
        {
            try (Reader reader = new UTF8FileReader(CONIFG_FILE))
            {
                config = GSON.fromJson(reader, Config.class);
            } catch (IOException e)
            {
                throw new RuntimeException("Unable to load config!", e);
            }
        } else
        {
            logger.error(CONIFG_FILE.getAbsolutePath());
            config = new Config();
        }

        CONFIG = config.doPostLoad();
    }

    public static synchronized void saveConfig()
    {
        if (CONFIG == null)
        {
            CONFIG = new Config().doPostLoad();
        }

        try (PAppendable out = new UTF8FileWriter(PFiles.ensureFileExists(CONIFG_FILE)))
        {
            GSON.toJson(CONFIG, out);
        } catch (IOException e)
        {
            throw new RuntimeException("Unable to save config!", e);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        loadConfig();

        if (CONFIG.taribone.subscriberId.equals("default") || CONFIG.taribone.serverIp.equals("default") || CONFIG.taribone.token.equals("default"))
        {
            logger.error("bruh no tonken");

            FMLCommonHandler.instance().exitJava(1, false);
        }

        CommandClientBuilder commandClient = new CommandClientBuilder();

        commandClient.setPrefix("&");
        commandClient.setOwnerId(CONFIG.taribone.operatorId);
        commandClient.setActivity(Activity.watching("fit em see"));

        commandClient.setHelpWord("JBjEFIWHB213*£(9Q££())(£$^&$FIRHBWEJDBNCJWHE");

        commandClient.addCommands(
                new AntiAFKCommand(),
                new BeginCommand(),
                new EndCommand(),
                new FarmCommand(),
                new FocusCommand(),
                new FollowCommand(),
                new GoalCommand(),
                new GotoCommand(),
                new InvertCommand(),
                new PathCommand(),
                new PauseCommand(),
                new ResumeCommand(),
                new StopCommand()
        );

        try
        {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(CONFIG.taribone.token)
                    .addEventListeners(commandClient.build())
                    .build();
        } catch (LoginException e)
        {
            logger.error(e);
        }
    }
}

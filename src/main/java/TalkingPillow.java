
import Listeners.MassageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TalkingPillow {
    private static final Logger logger = LoggerFactory.getLogger(TalkingPillow.class);

    private static final PropertiesConfiguration config = new PropertiesConfiguration();
    public static void main(String[] arguments) throws Exception
    {
        config.load("application.properties");
        String botToken = config.getString("bot_token");
        JDA api = JDABuilder.create(botToken, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_BANS,
                        GatewayIntent.SCHEDULED_EVENTS)
                .addEventListeners(new MassageListener())
                .build();


    }
}

import Listeners.MassageListener;
import busineslogic.GameManager;
import busineslogic.IGameManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class TalkingPillow {
    private static final String BOT_TOKEN = "MTA1NzM3ODQzNTg3OTY3ODAyMw.GGvTVf.t5oawXwiXmXSpJ7w73KrYsAsiOUbNeB8Xbsb2w";
    private static final IGameManager gameManager = GameManager.getInstance();
    public static void main(String[] arguments) throws Exception
    {
        JDA api = JDABuilder.create(BOT_TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_BANS,
                        GatewayIntent.SCHEDULED_EVENTS)
                .addEventListeners(new MassageListener())
                .build();
    }
}

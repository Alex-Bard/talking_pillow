package busineslogic;

import Listeners.MessageListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameFacade implements IGameFacade {
    private static GameFacade instance;
    private static final Logger logger = LoggerFactory.getLogger(GameFacade.class);
    private IGameManager gameManager = GameManager.getInstance();
    public static synchronized GameFacade getInstance() {
        logger.info("GameFacade instance was requested");
        if (instance == null) {
            instance = new GameFacade();
            logger.info("GameFacade instance was created");
        }
        return instance;
    }
    private GameFacade(){};

    @Override
    public void startGame(Guild guild, AudioChannelUnion audioChannel, MessageChannel messageChannel, Member owner)
            throws IllegalStateException {
        Map<Member, IPlayer> players = new HashMap();
        IPlayer ownerPlayer = new Player(owner,getMemberName(owner));
        logger.debug("Game owner " + ownerPlayer.getName() + " was created");
        for (Member member : audioChannel.getMembers()) {
            if (!member.equals(owner)) {
                IPlayer player = new Player(member,getMemberName(member));
                players.put(member, player);
                logger.debug("Game player " + player.getName() + " was created");
            }
        }
        logger.info("Starting game");
        this.gameManager.startGame(guild, audioChannel, messageChannel, ownerPlayer, players);
    }
    @Override
    public void voteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                            Member whoVoted)  throws IllegalStateException {
        IPlayer player = new Player(whoVoted,getMemberName(whoVoted));
        this.gameManager.voteForStop(guild, channel, messageChannel, player);
    }
    @Override
    public void resetVoteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                 Member whoVoted) throws IllegalStateException {
        IPlayer player = new Player(whoVoted,getMemberName(whoVoted));
        this.gameManager.resetVoteForStop(guild, channel,messageChannel, player);
    }
    @Override
    public void acceptGame(Guild guild, AudioChannelUnion audioChannel, MessageChannel messageChannel,
                           Member whoAccept) throws IllegalStateException {
        IPlayer player = new Player(whoAccept,getMemberName(whoAccept));
        this.gameManager.acceptGame(guild, audioChannel, messageChannel, player);
    }
    @Override
    public void requestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                              Member whoRequest) throws IllegalStateException {
        IPlayer player = new Player(whoRequest,getMemberName(whoRequest));
        this.gameManager.requestPillow(guild, channel,messageChannel, player);
    }
    @Override
    public void resetRequestPillow(Guild guild, AudioChannelUnion channel,MessageChannel messageChannel,
                                   Member whoRequest) throws IllegalStateException {
        IPlayer player = new Player(whoRequest,getMemberName(whoRequest));
        this.gameManager.resetRequestPillow(guild, channel,messageChannel, player);
    }
    @Override
    public void acceptPillowRequest(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                    Member who, Member toWhom) throws IllegalStateException {
        IPlayer playerWho = new Player(who,getMemberName(who));
        IPlayer playerToWhom = new Player(toWhom,getMemberName(toWhom));
        this.gameManager.acceptPillowRequest(guild, channel, messageChannel, playerWho, playerToWhom);
    }
    protected String getMemberName(Member member){
        return member.getNickname() == null ? member.getUser().getName() : member.getNickname();
    }
}

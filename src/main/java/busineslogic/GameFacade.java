package busineslogic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

import java.util.HashMap;
import java.util.Map;

public class GameFacade implements IGameFacade {
    private static GameFacade instance;
    private IGameManager gameManager = GameManager.getInstance();
    public static synchronized GameFacade getInstance() {
        if (instance == null) {
            instance = new GameFacade();
        }
        return instance;
    }
    private GameFacade(){};

    @Override
    public void startGame(Guild guild, AudioChannelUnion audioChannel, MessageChannel messageChannel, Member owner){
        Map<Member, IPlayer> players = new HashMap();
        IPlayer ownerPlayer = new Player(owner);
        for (Member member : audioChannel.getMembers()) {
            if (!member.equals(owner)) {
                players.put(member, new Player(member));
            }
        }
        this.gameManager.startGame(guild, audioChannel, messageChannel, ownerPlayer, players);
    }
    @Override
    public void voteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                            Member whoVoted){
        IPlayer player = new Player(whoVoted);
        this.gameManager.voteForStop(guild, channel, messageChannel, player);
    }
    @Override
    public void resetVoteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                 Member whoVoted){
        IPlayer player = new Player(whoVoted);
        this.gameManager.resetVoteForStop(guild, channel,messageChannel, player);
    }
    @Override
    public void acceptGame(Guild guild, AudioChannelUnion audioChannel, MessageChannel messageChannel,
                           Member whoAccept){
        IPlayer player = new Player(whoAccept);
        this.gameManager.acceptGame(guild, audioChannel, messageChannel, player);
    }
    @Override
    public void requestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                              Member whoRequest){
        IPlayer player = new Player(whoRequest);
        this.gameManager.requestPillow(guild, channel,messageChannel, player);
    }
    @Override
    public void resetRequestPillow(Guild guild, AudioChannelUnion channel,MessageChannel messageChannel,
                                   Member whoRequest){
        IPlayer player = new Player(whoRequest);
        this.gameManager.resetRequestPillow(guild, channel,messageChannel, player);
    }
    @Override
    public void acceptPillowRequest(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                    Member who, Member toWhom){
        IPlayer playerWho = new Player(who);
        IPlayer playerToWhom = new Player(toWhom);
        this.gameManager.acceptPillowRequest(guild, channel, messageChannel, playerWho, playerToWhom);
    }
}

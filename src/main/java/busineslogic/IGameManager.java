package busineslogic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

import java.util.Map;

public interface IGameManager {

    IGame getGame(Guild guild);
    void startGameForcibly(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer owner,
                           Map<Member, IPlayer> players) throws IllegalStateException;

    void createGame(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                    IPlayer owner, Map<Member, IPlayer> players) throws IllegalStateException;

    //always check users connecting to avoid problems with mute.
    void connectUserToChannel(Guild guild, AudioChannelUnion channel, IPlayer player)
            throws IllegalStateException;

    void print(MessageChannel messageChannel, String text);

    void voteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer player) throws IllegalStateException;

    void resetVoteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer whoVoted) throws IllegalStateException;

    void acceptGame(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer whoAccept) throws IllegalStateException;

    void requestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer whoRequest);

    void resetRequestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer whoRequest);

    void acceptPillowRequest(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer Who, IPlayer ToWhom);
    void changeMicrophoneStatuses(Map<Member,Boolean> micStatuses);
    void markGameNotActual(IGame game);
}

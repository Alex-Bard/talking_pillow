package busineslogic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

public interface IGameFacade {

    void createGame(Guild guild, AudioChannelUnion audioChannel, MessageChannel messageChannel, Member owner);

    void voteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                     Member whoVoted);
    void resetVoteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                          Member whoVoted);

    void acceptGame(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                    Member whoAccept);
    void requestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                       Member whoRequest);
    void resetRequestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                            Member whoRequest);
    void acceptPillowRequest(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                             Member who, Member toWhom);
}

package businesligic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

public interface IGameFacade {
    void startGame(Guild guild, AudioChannelUnion channel, Member owner);

    void voteForStop(Guild guild, AudioChannelUnion channel, Member whoVoted);

    void resetVoteForStop(Guild guild, AudioChannelUnion channel, Member whoVoted);

    void acceptGame(Guild guild, AudioChannelUnion channel, Member whoAccept);

    void requestPillow(Guild guild, AudioChannelUnion channel, Member whoRequest);

    void resetRequestPillow(Guild guild, AudioChannelUnion channel, Member whoRequest);

    void acceptPillowRequest(Guild guild, AudioChannelUnion channel, Member who, Member toWhom);
}

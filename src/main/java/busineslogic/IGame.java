package busineslogic;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

import java.util.Map;

public interface IGame {
    void addPlayer(IPlayer player);

    void initGame();

    void acceptGame(IPlayer whoAccept);

    void voteForStop(IPlayer player);

    void resetVoteForStop(IPlayer player);

    void requestPillow(IPlayer who);

    void resetRequestPillow(IPlayer who);

    void acceptPillowRequest(IPlayer who, IPlayer toWhom) throws IllegalStateException;

    IPlayer getOwner();
    Map<Member, IPlayer> getPlayers();
    IPlayer getTalker();
    Guild getGuild();
    AudioChannelUnion getChannel();
    //todo add getter for time change pillow recipient

}

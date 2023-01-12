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

    boolean canRequestPillow(IPlayer who);

    void requestPillow(IPlayer who);

    void resetRequestPillow(IPlayer who);

    boolean canAcceptPillowRec(IPlayer who, IPlayer toWhom);

    void acceptPillowRequest(IPlayer who, IPlayer toWhom) throws IllegalStateException;

    Map<Member, Boolean> getMicrophoneStatuses();

    int getNumOfStatuses(EPlayerStatusType Status);

    IPlayer getOwner();
    Map<Member, IPlayer> getPlayers();
    IPlayer getTalker();
    Guild getGuild();
    AudioChannelUnion getChannel();

    EGameStatusType getGameStatus();
    //todo add getter for time change pillow recipient

}

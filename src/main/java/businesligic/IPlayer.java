package businesligic;

import net.dv8tion.jda.api.entities.Member;

public interface IPlayer {
    Member getMember();
    EPlayerStatusType getStatus();
    void setStatus(EPlayerStatusType status);

    EPlayerStatusType getInGameStatus();

    void setInGameStatus(EPlayerStatusType inGameStatus);

    EPlayerStatusType getOutGameStatus();

    void setOutGameStatus(EPlayerStatusType outGameStatus);
}

package businesligic;

import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class Player implements IPlayer{
    private final Member member;
    private EPlayerStatusType inGameStatus;
    private EPlayerStatusType outGameStatus;

    public Player(Member member) {
        this.member = member;
    }
    @Override
    public Member getMember() {
        return this.member;
    }
    @Override
    public EPlayerStatusType getInGameStatus() {
        return inGameStatus;
    }
    @Override
    public void setInGameStatus(EPlayerStatusType inGameStatus) {
        this.inGameStatus = inGameStatus;
    }
    @Override
    public EPlayerStatusType getOutGameStatus() {
        return outGameStatus;
    }
    @Override
    public void setOutGameStatus(EPlayerStatusType outGameStatus) {
        this.outGameStatus = outGameStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return member.equals(player.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member);
    }
}

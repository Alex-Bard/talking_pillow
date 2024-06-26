package busineslogic;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;
/**
 * This class represents a player.
 */
public class Player implements IPlayer{
    private Member member;
    private EPlayerStatusType inGameStatus;
    private EPlayerStatusType outGameStatus;
    /**
     * The name of the player.
     */

    private String name;
    /**
     * @return The name of the player.
     */
    @Override
    public String getName() {
        return name;
    }
    public Player(Member member, String name) {
        this.member = member;
        this.name = name;
    }
    @Override
    public Member getMember() {
        return this.member;
    }
    public void setMember(Member member){
        this.member = member;
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

package busineslogic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageActivity;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.internal.entities.MemberImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.dv8tion.jda.api.entities.Member;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void initPlayersStatuses() {
        Map<Member,IPlayer> playerMocks = new HashMap<>();
        IGameManager gameManagerMock = Mockito.mock(GameManager.class);
        IPlayer ownerMock = Mockito.mock(IPlayer.class);
        Member member1Mock = Mockito.mock(Member.class);
        playerMocks.put(member1Mock, Mockito.mock(Player.class));

        Guild guildMock = Mockito.mock(Guild.class);
        AudioChannelUnion audioChannelMock = Mockito.mock(AudioChannelUnion.class);
        MessageChannel messageChannelMock = Mockito.mock(MessageChannel.class);
        Game game = new Game(gameManagerMock, ownerMock, playerMocks, guildMock, audioChannelMock, messageChannelMock);

        Mockito.verify(ownerMock, Mockito.times(1))
                .setOutGameStatus(EPlayerStatusType.NOT_READY);
        Mockito.verify(playerMocks.get(member1Mock), Mockito.times(1))
                .setOutGameStatus(EPlayerStatusType.NOT_READY);
    }

    @Test
    void acceptGame() {
        Map<Member,IPlayer> playerMocks = new HashMap<>();
        IGameManager gameManagerMock = Mockito.mock(GameManager.class);
        IPlayer ownerMock = Mockito.mock(IPlayer.class);
        Member ownerMemberMock = Mockito.mock(Member.class);
        Mockito.when(ownerMock.getMember()).thenReturn(ownerMemberMock);
        Member member1Mock = Mockito.mock(Member.class);
        IPlayer player1Mock = Mockito.mock(Player.class);
        Mockito.when(player1Mock.getMember()).thenReturn(member1Mock);
        playerMocks.put(member1Mock, player1Mock);

        Mockito.when(playerMocks.get(member1Mock).getOutGameStatus()).thenReturn(EPlayerStatusType.READY);
        Mockito.when(ownerMock.getOutGameStatus()).thenReturn(EPlayerStatusType.READY);
        Mockito.when(ownerMemberMock.getNickname()).thenReturn("owner");
        Mockito.when(member1Mock.getNickname()).thenReturn("player");

        Guild guildMock = Mockito.mock(Guild.class);
        AudioChannelUnion audioChannelMock = Mockito.mock(AudioChannelUnion.class);
        MessageChannel messageChannelMock = Mockito.mock(MessageChannel.class);

        Game game = new Game(gameManagerMock, ownerMock, playerMocks, guildMock, audioChannelMock, messageChannelMock);
        game.initGame();

        for (Map.Entry<Member, IPlayer> playerEntry : playerMocks.entrySet()){
            Mockito.verify(playerEntry.getValue(), Mockito.times(1))
                    .setOutGameStatus(EPlayerStatusType.NOT_READY);
        }
        Mockito.verify(ownerMock, Mockito.times(1))
                .setOutGameStatus(EPlayerStatusType.NOT_READY);
        Mockito.verify(ownerMock, Mockito.times(1))
                .setOutGameStatus(EPlayerStatusType.READY);
        assertEquals(EGameStatusType.PENDING, game.getGameStatus());

        IPlayer playerWhoAccept = playerMocks.get(member1Mock);

        game.acceptGame(playerWhoAccept);

        for (Map.Entry<Member, IPlayer> playerEntry : playerMocks.entrySet()){
            Mockito.verify(playerEntry.getValue(), Mockito.times(1))
                    .setOutGameStatus(EPlayerStatusType.READY);
        }
        for (Map.Entry<Member, IPlayer> playerEntry : playerMocks.entrySet()){
            Mockito.verify(playerEntry.getValue(), Mockito.times(1))
                    .setInGameStatus(EPlayerStatusType.LISTEN);
        }
        Mockito.verify(ownerMock, Mockito.times(1))
                .setInGameStatus(EPlayerStatusType.TALKING);
        assertEquals(EGameStatusType.GOING, game.getGameStatus());

    }

    @Test
    void checkCanStart() {
        Map<Member,IPlayer> playerMocks = new HashMap<>();
        IGameManager gameManagerMock = Mockito.mock(GameManager.class);
        IPlayer ownerMock = Mockito.mock(IPlayer.class);
        Member member1Mock = Mockito.mock(Member.class);

        IPlayer player1Mock = Mockito.mock(Player.class);
        Mockito.when(player1Mock.getMember()).thenReturn(member1Mock);
        Mockito.when(player1Mock.getOutGameStatus()).thenReturn(EPlayerStatusType.NOT_READY);
        Mockito.when(ownerMock.getOutGameStatus()).thenReturn(EPlayerStatusType.NOT_READY);

        playerMocks.put(member1Mock, player1Mock);

        Guild guildMock = Mockito.mock(Guild.class);
        AudioChannelUnion audioChannelMock = Mockito.mock(AudioChannelUnion.class);
        MessageChannel messageChannelMock = Mockito.mock(MessageChannel.class);

        Game game = new Game(gameManagerMock, ownerMock, playerMocks, guildMock, audioChannelMock, messageChannelMock);

        assertEquals(false, game.checkCanStart());

        playerMocks = new HashMap<>();
         ownerMock = Mockito.mock(IPlayer.class);
         member1Mock = Mockito.mock(Member.class);

         player1Mock = Mockito.mock(Player.class);
        Mockito.when(player1Mock.getMember()).thenReturn(member1Mock);

        playerMocks.put(member1Mock, player1Mock);
        Mockito.when(player1Mock.getOutGameStatus()).thenReturn(EPlayerStatusType.READY);
        Mockito.when(ownerMock.getOutGameStatus()).thenReturn(EPlayerStatusType.READY);
        game = new Game(gameManagerMock, ownerMock, playerMocks, guildMock, audioChannelMock, messageChannelMock);
        game.initGame();

        assertEquals(true, game.checkCanStart());

        playerMocks = new HashMap<>();
        ownerMock = Mockito.mock(IPlayer.class);
        member1Mock = Mockito.mock(Member.class);

        player1Mock = Mockito.mock(Player.class);
        Mockito.when(player1Mock.getMember()).thenReturn(member1Mock);

        playerMocks.put(member1Mock, player1Mock);
        Mockito.when(player1Mock.getOutGameStatus()).thenReturn(EPlayerStatusType.READY);
        Mockito.when(ownerMock.getOutGameStatus()).thenReturn(EPlayerStatusType.NOT_READY);
        game = new Game(gameManagerMock, ownerMock, playerMocks, guildMock, audioChannelMock, messageChannelMock);
        game.initGame();

        assertEquals(false, game.checkCanStart());
    }
}
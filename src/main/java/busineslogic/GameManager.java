package busineslogic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import org.apache.commons.collections4.map.HashedMap;
import view.IMicrophoneManager;
import view.MicrophoneManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameManager implements IGameManager {
    private IMicrophoneManager microphoneManager = MicrophoneManager.getInstance();
    private Map<Guild, IGame> actualGames;
    private List<IGame> historyGames;
    private static GameManager instance;

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    protected GameManager() {
        this.actualGames = new HashedMap<>();
        this.historyGames = new LinkedList<>();
    }

    @Override
    public IGame getGame(Guild guild) {
        return null;
    }

    @Override
    public void startGameForcibly(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer owner,
                                  Map<Member, IPlayer> players) throws IllegalStateException {
        startGame(guild, channel, messageChannel, owner, players);
        for (Map.Entry<Member, IPlayer> player : players.entrySet()) {
            acceptGame(guild, channel, messageChannel, player.getValue());
        }

    }

    @Override
    public void startGame(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                          IPlayer owner, Map<Member, IPlayer> players) throws IllegalStateException {
        if (actualGames.containsKey(guild)) {
            print(messageChannel, "game is already on!");
            return;
        }
        if (players.size() == 0) {
            print(messageChannel, "You need at least 2 players to start the game");
            return;
        }
        IGame game = new Game(this, owner, players, guild, channel, messageChannel);
        game.initGame();

        this.actualGames.put(guild, game);
        print(messageChannel, "???????? ??????????????. ?????????????????? ?????????????????????????? ???????????? ??????????????!");

    }

    @Override
    public void connectUserToChannel(Guild guild, AudioChannelUnion channel, IPlayer player)
            throws IllegalStateException {
        Map<Member, Boolean> playerMicStatuses;
        if (actualGames.containsKey(guild)) {
            actualGames.get(guild).addPlayer(player);
            playerMicStatuses = actualGames.get(guild).getMicrophoneStatuses();
        } else {
            playerMicStatuses = new HashMap<>();
            playerMicStatuses.put(player.getMember(), true);
        }
        changeMicrophoneStatuses(playerMicStatuses);
    }

    @Override
    public void print(MessageChannel messageChannel, String text) {
        messageChannel.sendMessage(text).queue();
    }

    @Override
    public void voteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer player)
            throws IllegalStateException {
        if (!actualGames.containsKey(guild)) {
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(player.getMember())) {
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        IGame game = this.actualGames.get(guild);
        game.voteForStop(player);
        print(messageChannel, "" + player.getName() + " ???????????????????????? ???? ???????????????????? ????????!");
        print(messageChannel, "?????????? ?????????????????? " + game.getNumOfStatuses(EPlayerStatusType.WAITING_COMPLETION)
                + " ?????????????? ???? " + game.getPlayers().size());
        if (game.getGameStatus() == EGameStatusType.ENDED) {
            print(messageChannel, "???????? ??????????????????!");
            markGameNotActual(game);
        }
        changeMicrophoneStatuses(game.getMicrophoneStatuses());
    }

    @Override
    public void resetVoteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                 IPlayer whoVoted) throws IllegalStateException {
        if (!actualGames.containsKey(guild)) {
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoVoted.getMember())) {
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }

        this.actualGames.get(guild).resetVoteForStop(whoVoted);
        print(messageChannel, "" + whoVoted.getName() + " ?????????????? ?????????? ???? ???????????????????? ????????!");

    }

    @Override
    public void acceptGame(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer whoAccept)
            throws IllegalStateException {
        if (!actualGames.containsKey(guild)) {
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoAccept.getMember())) {
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        IGame game = this.actualGames.get(guild);
        game.acceptGame(whoAccept);
        print(messageChannel, "" + whoAccept.getName() + " ?????????????? ????????????????????!");
        print(messageChannel, "???????????? " + game.getNumOfStatuses(EPlayerStatusType.READY)
                + " ?????????????? ???? " + game.getPlayers().size());
        if (game.getGameStatus() == EGameStatusType.GOING) {
            print(messageChannel, "???????? ????????????????!");
            print(messageChannel, "?????????????? ?? " + game.getTalker().getName() + "!");
        }
        changeMicrophoneStatuses(game.getMicrophoneStatuses());
    }

    @Override
    public void requestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                              IPlayer whoRequest) throws IllegalStateException {
        if (!actualGames.containsKey(guild)) {
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoRequest.getMember())) {
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        IGame game = this.actualGames.get(guild);
        if (game.canRequestPillow(whoRequest)){
            game.requestPillow(whoRequest);
            print(messageChannel, "" + whoRequest.getName() + " ?????????? ?????? ??????????????!");
        }
        else {
            print(messageChannel, "" + whoRequest.getName() + ", ???? ???? ???????????? ?????????????????? ??????????????!");
        }
    }

    @Override
    public void resetRequestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                   IPlayer whoRequest) throws IllegalStateException {
        if (!actualGames.containsKey(guild)) {
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoRequest.getMember())) {
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        this.actualGames.get(guild).resetRequestPillow(whoRequest);
        print(messageChannel, "" + whoRequest.getName() + " ?????????????????? ????????!");
    }

    @Override
    public void acceptPillowRequest(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                    IPlayer who, IPlayer toWhom) throws IllegalStateException {
        if (!actualGames.containsKey(guild)) {
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(who.getMember())
                && !actualGames.get(guild).getPlayers().containsKey(toWhom.getMember())) {
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        IGame game =  this.actualGames.get(guild);
        if (game.canAcceptPillowRec(who, toWhom)){
            this.actualGames.get(guild).acceptPillowRequest(who, toWhom);
            print(messageChannel, "" + who.getName() + " ???????????????? ?????????????? ???????????? "
                    + toWhom.getName());
        }
        else {
            print(messageChannel, "???? ???? ???????????? ???????????????? ??????????????!");
        }

        changeMicrophoneStatuses(game.getMicrophoneStatuses());
    }

    @Override
    public void changeMicrophoneStatuses(Map<Member, Boolean> micStatuses) {
        microphoneManager.changeMicrophoneStatuses(micStatuses);
    }
    @Override
    public void markGameNotActual(IGame game) {
        actualGames.remove(game.getGuild());
        historyGames.add(game);
    }
}

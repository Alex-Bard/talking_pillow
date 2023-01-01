package businesligic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import org.apache.commons.collections4.map.HashedMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameManager implements IGameManager {
    private Map<Guild,IGame> actualGames;
    private List<IGame> historyGames;
    private static GameManager instance;

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    private GameManager (){
        this.actualGames = new HashedMap<>();
        this.historyGames = new LinkedList<>();
    }
    @Override
    public IGame getGame(Guild guild) {
        return null;
    }

    @Override
    public void startGameForcibly(Guild guild, AudioChannelUnion channel, IPlayer owner,
                                  Map<Member, IPlayer> players) throws IllegalStateException {
        startGame(guild, channel,owner,players);
        for (Map.Entry<Member,IPlayer> player : players.entrySet()){
            acceptGame(guild, channel, player.getValue());
        }
    }

    @Override
    public void startGame(Guild guild, AudioChannelUnion channel,
                          IPlayer owner, Map<Member, IPlayer> players) throws IllegalStateException {
        if (actualGames.containsKey(guild)){
            throw new IllegalStateException("Game is already on");
        }
        IGame game = new Game(this, owner, players, guild, channel);
        game.initGame();
        this.actualGames.put(guild, game);
        //todo add view
        //todo add handler if owner is only one in chat

    }

    @Override
    public void connectUserToChannel(Guild guild, AudioChannelUnion channel, Member member) {
        if (actualGames.containsKey(guild)){
            actualGames.get(guild).addPlayer(new Player(member));
        }
        else {
            Map<Member,Boolean> playerMicStatus = new HashMap<>();
            playerMicStatus.put(member, true);
            changeMicrophoneStatuses(playerMicStatus);
        }
    }

    @Override
    public void print(String text) {

    }

    @Override
    public void voteForStop(Guild guild, AudioChannelUnion channel, IPlayer player) throws IllegalStateException {
        if (!actualGames.containsKey(guild)){
            throw new IllegalStateException("Game not found");
        }
        if (!actualGames.get(guild).getPlayers().containsKey(player.getMember())){
            throw new IllegalStateException("player must be a game member to perform this action");
        }

        this.actualGames.get(guild).voteForStop(player);
        //todo add view
    }
    @Override
    public void resetVoteForStop(Guild guild, AudioChannelUnion channel, IPlayer whoVoted) throws IllegalStateException{
        if (!actualGames.containsKey(guild)){
            throw new IllegalStateException("Game not found");
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoVoted.getMember())){
            throw new IllegalStateException("player must be a game member to perform this action");
        }

        this.actualGames.get(guild).resetVoteForStop(whoVoted);
        //todo add view
    }
    @Override
    public void acceptGame(Guild guild, AudioChannelUnion channel, IPlayer whoAccept) throws IllegalStateException{
        if (!actualGames.containsKey(guild)){
            throw new IllegalStateException("Game not found");
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoAccept)){
            throw new IllegalStateException("player must be a game member to perform this action");
        }
        this.actualGames.get(guild).acceptGame(whoAccept);
        //todo add view
    }
    @Override
    public void requestPillow(Guild guild, AudioChannelUnion channel, IPlayer whoRequest){
        if (!actualGames.containsKey(guild)){
            throw new IllegalStateException("Game not found");
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoRequest)){
            throw new IllegalStateException("player must be a game member to perform this action");
        }
        this.actualGames.get(guild).requestPillow(whoRequest);
        //todo add view
    }
    @Override
    public void resetRequestPillow(Guild guild, AudioChannelUnion channel, IPlayer whoRequest){
        if (!actualGames.containsKey(guild)){
            throw new IllegalStateException("Game not found");
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoRequest)){
            throw new IllegalStateException("player must be a game member to perform this action");
        }
        this.actualGames.get(guild).resetRequestPillow(whoRequest);
        //todo add view
    }
    @Override
    public void acceptPillowRequest(Guild guild, AudioChannelUnion channel, IPlayer Who, IPlayer ToWhom){
        if (!actualGames.containsKey(guild)){
            throw new IllegalStateException("Game not found");
        }
        if (!actualGames.get(guild).getPlayers().containsKey(Who)
                && !actualGames.get(guild).getPlayers().containsKey(ToWhom)){
            throw new IllegalStateException("player must be a game member to perform this action");
        }
        this.actualGames.get(guild).acceptPillowRequest(Who, ToWhom);
        //todo add view
    }

    @Override
    public void changeMicrophoneStatuses(Map<Member, Boolean> micStatuses) {

    }
    @Override
    public void markGameNotActual(IGame game) {
        actualGames.remove(game.getGuild());
        historyGames.add(game);
    }
}

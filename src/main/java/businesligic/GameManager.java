package businesligic;

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
    public void startGameForcibly(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer owner,
                                  Map<Member, IPlayer> players){
        try {
            startGame(guild, channel, messageChannel, owner, players);
            for (Map.Entry<Member,IPlayer> player : players.entrySet()){
                acceptGame(guild, channel, messageChannel, player.getValue());
            }
        }
        catch (IllegalStateException e){
            print(messageChannel, e.getMessage());
        }

    }

    @Override
    public void startGame(Guild guild, AudioChannelUnion channel,  MessageChannel messageChannel,
                          IPlayer owner, Map<Member, IPlayer> players){
        if (actualGames.containsKey(guild)){
            print(messageChannel, "game is already on!");
            return;
        }
        IGame game = new Game(this, owner, players, guild, channel,messageChannel);
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
    public void print(MessageChannel messageChannel, String text) {
        messageChannel.sendMessage(text).queue();
    }

    @Override
    public void voteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer player){
        if (!actualGames.containsKey(guild)){
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(player.getMember())){
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }

        this.actualGames.get(guild).voteForStop(player);
        print(messageChannel, "" + player.getMember().getNickname() + " проголосовал за завершение игры!");
    }
    @Override
    public void resetVoteForStop(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                 IPlayer whoVoted){
        if (!actualGames.containsKey(guild)){
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoVoted.getMember())){
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }

        this.actualGames.get(guild).resetVoteForStop(whoVoted);
        print(messageChannel, "" + whoVoted.getMember().getNickname() + " отменил голос за завершение игры!");

    }
    @Override
    public void acceptGame(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel, IPlayer whoAccept){
        if (!actualGames.containsKey(guild)){
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoAccept.getMember())){
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        this.actualGames.get(guild).acceptGame(whoAccept);
        print(messageChannel, "" + whoAccept.getMember().getNickname() + " выразил готовность!");

    }
    @Override
    public void requestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                              IPlayer whoRequest){
        if (!actualGames.containsKey(guild)){
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoRequest.getMember())){
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        this.actualGames.get(guild).requestPillow(whoRequest);
        print(messageChannel, "" + whoRequest.getMember().getNickname() + " имеет что сказать!");
    }
    @Override
    public void resetRequestPillow(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                   IPlayer whoRequest){
        if (!actualGames.containsKey(guild)){
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(whoRequest.getMember())){
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        this.actualGames.get(guild).resetRequestPillow(whoRequest);
        print(messageChannel, "" + whoRequest.getMember().getNickname() + " проглотил язык!");
    }
    @Override
    public void acceptPillowRequest(Guild guild, AudioChannelUnion channel, MessageChannel messageChannel,
                                    IPlayer Who, IPlayer ToWhom){
        if (!actualGames.containsKey(guild)){
            print(messageChannel, "Game not found");
            return;
        }
        if (!actualGames.get(guild).getPlayers().containsKey(Who.getMember())
                && !actualGames.get(guild).getPlayers().containsKey(ToWhom.getMember())){
            print(messageChannel, "player must be a game member to perform this action");
            return;
        }
        this.actualGames.get(guild).acceptPillowRequest(Who, ToWhom);
        print(messageChannel, "" + Who.getMember().getNickname() + " передает подушку игроку "
                + ToWhom.getMember().getNickname());
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

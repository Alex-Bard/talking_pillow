package businesligic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Game implements IGame{
    private static int SECONDS_TO_DENY_GAME = 30;
    private IPlayer owner;
    private Map<Member, IPlayer> players;
    private Guild guild;
    private AudioChannelUnion channel;
    private EGameStatusType gameStatus;
    private IGameManager gameManager;

    public Game(IGameManager gameManager, IPlayer owner, Map<Member, IPlayer> players, Guild guild, AudioChannelUnion channel) {
        this.guild = guild;
        this.channel = channel;
        this.gameManager = gameManager;
        this.players = new HashMap<>(players);
        for (Map.Entry<Member, IPlayer> playerEntry : this.players.entrySet()){
            playerEntry.getValue().setStatus(EPlayerStatusType.NOT_READY);
        }
        this.owner.setStatus(EPlayerStatusType.NOT_READY);

    }
    @Override
    public void addPlayer(@NotNull IPlayer player){
        player.setInGameStatus(EPlayerStatusType.LISTEN);
        players.put(player.getMember(), player);
        gameManager.changeMicrophoneStatuses(getMicrophoneStatuses());

    }
    @Override
    public void initGame(){
        this.owner.setStatus(EPlayerStatusType.READY);
        this.gameStatus = EGameStatusType.PENDING;
        //todo add start timer
    }
    @Override
    public void acceptGame(IPlayer whoAccept){
        if (this.gameStatus == EGameStatusType.DENIED){
            this.gameManager.print("Игра отклонена");
            return;
        }
        else if(this.gameStatus == EGameStatusType.PENDING){
            this.players.get(whoAccept.getMember()).setOutGameStatus(EPlayerStatusType.READY);
            if (checkCanStart()){
                startGame();
            }
        }
    }

    @Override
    public void voteForStop(IPlayer player){
        getPlayer(player).setOutGameStatus(EPlayerStatusType.WAITING_COMPLETION);
        if (getNumOfStatuses(EPlayerStatusType.WAITING_COMPLETION) >= players.size() / 2){
            stopGame();
        }
    }
    @Override
    public void resetVoteForStop(IPlayer player){
        getPlayer(player).setOutGameStatus(EPlayerStatusType.READY);
    }
    private void stopGame(){
        gameStatus = EGameStatusType.ENDED;
        owner.setInGameStatus(EPlayerStatusType.LISTEN);
        for (Map.Entry<Member,IPlayer> player : players.entrySet()){
            player.getValue().setInGameStatus(EPlayerStatusType.LISTEN);
        }
        gameManager.changeMicrophoneStatuses(getMicrophoneStatuses());
        gameManager.print("Игра закончена!");
        gameManager.markGameNotActual(this);

    }
    private void startGame(){
        gameStatus = EGameStatusType.GOING;
        for (Map.Entry<Member, IPlayer> player : players.entrySet()){
            player.getValue().setInGameStatus(EPlayerStatusType.LISTEN);
        }
        owner.setInGameStatus(EPlayerStatusType.TALKING);
        gameManager.changeMicrophoneStatuses(getMicrophoneStatuses());
        gameManager.print("Игра началась!");
        gameManager.print("Подушка у " + owner.getMember().getNickname() + "!");
    }
    @Override
    public void requestPillow(IPlayer who){
        players.get(who.getMember()).setInGameStatus(EPlayerStatusType.WAIT_PILLOW);
    }
    @Override
    public void resetRequestPillow(IPlayer who){
        players.get(who.getMember()).setInGameStatus(EPlayerStatusType.LISTEN);
    }
    @Override
    public void acceptPillowRequest(IPlayer who, IPlayer toWhom) throws IllegalStateException{
        if (!isPlayerExists(who) && isPlayerExists(toWhom)){
            throw new IllegalStateException("Someone from players is not exists");
        }
        if (getPlayer(who).getInGameStatus() != EPlayerStatusType.TALKING
                && getPlayer(toWhom).getInGameStatus() != EPlayerStatusType.WAIT_PILLOW){
            throw new IllegalStateException("someone from players has illegal state");
        }
        getPlayer(who).setInGameStatus(EPlayerStatusType.LISTEN);
        getPlayer(toWhom).setInGameStatus(EPlayerStatusType.TALKING);
        gameManager.changeMicrophoneStatuses(getMicrophoneStatuses());
        gameManager.print("" + who.getMember().getNickname()
                + " передает подушку " + toWhom.getMember().getNickname());
    }
    private Map<Member, Boolean> getMicrophoneStatuses(){
        Map<Member, Boolean> micStatuses = new HashMap<>();
        micStatuses.put(owner.getMember(),
                owner.getInGameStatus() == EPlayerStatusType.TALKING ? true : false);
        for (Map.Entry<Member, IPlayer> player : players.entrySet()){
            micStatuses.put(player.getValue().getMember(),
                    player.getValue().getInGameStatus() == EPlayerStatusType.TALKING ? true : false);
        }
        return micStatuses;
    }
    private boolean isPlayerExists(IPlayer player){
        return players.containsKey(player.getMember()) || owner.getMember().equals(player.getMember());
    }
    private IPlayer getPlayer(IPlayer player) throws IllegalStateException{
        if (players.containsKey(player.getMember())){
            return players.get(player.getMember());
        }
        else if (owner.equals(player.getMember())){
            return owner;
        }
        else {
            throw new IllegalStateException("player not exists");
        }
    }
    private boolean checkCanStart(){
        return gameStatus == EGameStatusType.PENDING
                && players.size() == getNumOfStatuses(EPlayerStatusType.READY)
                && owner.getOutGameStatus() == EPlayerStatusType.READY;
    }
    private int getNumOfStatuses(EPlayerStatusType Status){
        int count = 0;
        for (Map.Entry<Member,IPlayer> player : this.players.entrySet()){
            if (player.getValue().getOutGameStatus() == Status){
                count++;
            }
        }
        return count;
    }
    @Override
    public IPlayer getOwner() {
        return owner;
    }

    @Override
    public Map<Member, IPlayer> getPlayers() {
        Map<Member, IPlayer> res = new HashMap<>(players);
        res.put(owner.getMember(), owner);
        return res;
    }


    @Override
    public IPlayer getTalker() {
        IPlayer talker = null;
        if (owner.getInGameStatus() == EPlayerStatusType.TALKING){
            return owner;
        }
        else {

            for (Map.Entry<Member,IPlayer> player : players.entrySet()){
                if (player.getValue().getInGameStatus() == EPlayerStatusType.TALKING){
                    talker = player.getValue();
                }
            }
            if (talker == null) throw new IllegalStateException("Talker not found");
        }
        return talker;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public AudioChannelUnion getChannel() {
        return channel;
    }
    public EGameStatusType getGameStatus() {
        return gameStatus;
    }
}

package Listeners;

import busineslogic.GameFacade;
import busineslogic.IGameFacade;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MassageListener  extends ListenerAdapter {
    private final IGameFacade gameFacade = GameFacade.getInstance();
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        Guild guild = event.getGuild();
        Member member = event.getMember();
        MessageChannel messageChannel = event.getChannel();
        if (member.getVoiceState().getChannel() == null){
            messageChannel.sendMessage("Вы должны быть в голосовом канале").queue();
            return;
        }


        if (content.equals("!start")) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            gameFacade.startGame(guild, audioChannel, event.getChannel(), member);
            //messageChannel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
        } else if (content.equals("!accept")) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            gameFacade.acceptGame(guild, audioChannel, event.getChannel(), member);
            //event.getMember().deafen(false).queue();
        } else if (content.equals("!stop")) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            gameFacade.voteForStop(guild, audioChannel, event.getChannel(), member);
            //event.getMember().deafen(false).queue();
        } else if (content.equals("!resetStop")) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            gameFacade.resetVoteForStop(guild, audioChannel, event.getChannel(), member);
            //event.getMember().deafen(false).queue();
        } else if (content.equals("!rec")) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            gameFacade.requestPillow(guild, audioChannel, event.getChannel(), member);
            //event.getMember().deafen(false).queue();
        } else if (content.equals("!resetRec")) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            gameFacade.resetRequestPillow(guild, audioChannel, event.getChannel(), member);
            //event.getMember().deafen(false).queue();
        } else if (content.contains("!acceptRec") && message.getMentions().getMembers().size() == 1) {
            AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
            Member toWhom = message.getMentions().getMembers().get(0);
            gameFacade.acceptPillowRequest(guild, audioChannel, event.getChannel(), toWhom, member);
            event.getMember().deafen(false).queue();
        }
    }
}

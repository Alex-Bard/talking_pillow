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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListener extends ListenerAdapter {
    private final IGameFacade gameFacade = GameFacade.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        Guild guild = event.getGuild();
        Member member = event.getMember();
        MessageChannel messageChannel = event.getChannel();
        logger.info("got message \"" + message + "\" from " + member.getNickname() == null ?
                member.getUser().getName() :member.getNickname());

        if (member.getVoiceState().getChannel() == null){
            logger.info("member " + member.getNickname() == null ?
                    member.getUser().getName() : member.getNickname() + "is not in voce channel. return");
            messageChannel.sendMessage("Вы должны быть в голосовом канале").queue();
            return;
        }

        try {
            if (content.equals("!start")) {
                logger.info("processing command START for member " + member.getNickname() == null ?
                        member.getUser().getName() :member.getNickname());
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                gameFacade.startGame(guild, audioChannel, event.getChannel(), member);
            } else if (content.equals("!accept")) {
                logger.info("processing command ACCEPT for member " + member.getNickname() == null ?
                        member.getUser().getName() :member.getNickname());
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                gameFacade.acceptGame(guild, audioChannel, event.getChannel(), member);
            } else if (content.equals("!stop")) {
                logger.info("processing command STOP for member " + member.getNickname() == null ?
                        member.getUser().getName() :member.getNickname());
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                gameFacade.voteForStop(guild, audioChannel, event.getChannel(), member);
            } else if (content.equals("!resetStop")) {
                logger.info("processing command RESETSTOP for member " + member.getNickname() == null ?
                        member.getUser().getName() :member.getNickname());
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                gameFacade.resetVoteForStop(guild, audioChannel, event.getChannel(), member);
            } else if (content.equals("!rec")) {
                logger.info("processing command REQUEST for member " + member.getNickname() == null ?
                        member.getUser().getName() :member.getNickname());
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                gameFacade.requestPillow(guild, audioChannel, event.getChannel(), member);
            } else if (content.equals("!resetRec")) {
                logger.info("processing command RESETREQUEST for member " + member.getNickname() == null ?
                        member.getUser().getName() :member.getNickname());
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                gameFacade.resetRequestPillow(guild, audioChannel, event.getChannel(), member);
            } else if (content.contains("!acceptRec") && message.getMentions().getMembers().size() == 1) {
                AudioChannelUnion audioChannel = member.getVoiceState().getChannel();
                Member toWhom = message.getMentions().getMembers().get(0);
                logger.info("processing command ASSEPTREQUEST("
                        + toWhom.getNickname() == null ? toWhom.getUser().getName() :toWhom.getNickname()
                        + ") for member "
                        + member.getNickname() == null ? member.getUser().getName() :member.getNickname());
                gameFacade.acceptPillowRequest(guild, audioChannel, event.getChannel(), member, toWhom);
                event.getMember().deafen(false).queue();
            }
        }
        catch (IllegalStateException e){
            logger.warn(e.getMessage(),e);
            messageChannel.sendMessage("error occurred").queue();
        }
    }
}

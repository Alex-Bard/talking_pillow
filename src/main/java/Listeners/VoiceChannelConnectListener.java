package Listeners;

import busineslogic.GameFacade;
import busineslogic.IGameFacade;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChannelConnectListener extends ListenerAdapter {
    private final IGameFacade gameFacade = GameFacade.getInstance();
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event)
    {
        Guild guild = event.getGuild();
        Member member = event.getMember();
//        if (event.getChannelJoined() != null){
//            gameFacade.connectUser(event.getChannelJoined(), member);
//        }
//        else if (event.getChannelLeft() != null){
//            gameFacade.dicsonnectUser(event.getChannelLeft(),member);
//        }

//        MessageChannel messageChannel = event.getChannel();
//        if (member.getVoiceState().getChannel() == null){
//            messageChannel.sendMessage("Вы должны быть в голосовом канале").queue();
//            return;
//        }
//
//        try {
//
//        }
//        catch (IllegalStateException e){
//            e.printStackTrace();
//            messageChannel.sendMessage(e.getMessage()).queue();
//        }
    }
}

package view;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class MassagePrinter implements IMassagePrinter {

    @Override
    public void print(MessageChannel messageChannel, String text) {
        messageChannel.sendMessage(text).queue();
    }
}

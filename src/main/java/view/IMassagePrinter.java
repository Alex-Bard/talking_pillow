package view;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public interface IMassagePrinter {
     void print(MessageChannel messageChannel, String text);
}

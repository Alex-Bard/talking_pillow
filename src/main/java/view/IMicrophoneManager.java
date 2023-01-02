package view;

import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public interface IMicrophoneManager {
    public void changeMicrophoneStatuses(Map<Member,Boolean> micStatuses);
}

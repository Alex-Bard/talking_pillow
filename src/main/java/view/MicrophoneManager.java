package view;

import businesligic.GameManager;
import net.dv8tion.jda.api.entities.Member;
import java.util.Map;

public class MicrophoneManager implements IMicrophoneManager {
    private static IMicrophoneManager instance;
    private MicrophoneManager(){}
    public static synchronized IMicrophoneManager getInstance() {
        if (instance == null) {
            instance = new MicrophoneManager();
        }
        return instance;
    }
    public void changeMicrophoneStatuses(Map<Member,Boolean> micStatuses){
        for(Map.Entry<Member,Boolean> player : micStatuses.entrySet()){
            player.getKey().mute(!player.getValue());
        }
    }
}

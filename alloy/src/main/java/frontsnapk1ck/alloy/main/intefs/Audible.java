package frontsnapk1ck.alloy.main.intefs;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;

public interface Audible {

    public GuildMusicManager getGuildAudioPlayer(Guild g);

    public AudioPlayerManager getPlayerManager();
    
}

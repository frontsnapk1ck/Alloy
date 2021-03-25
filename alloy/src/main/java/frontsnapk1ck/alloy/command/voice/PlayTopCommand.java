package frontsnapk1ck.alloy.command.voice;

import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import frontsnapk1ck.alloy.audio.GuildMusicManager;
import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.handler.command.AudioHandler;
import frontsnapk1ck.alloy.input.AlloyInputUtil;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.intefs.Audible;
import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import frontsnapk1ck.alloy.utility.job.jobs.DelayJob;
import frontsnapk1ck.disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class PlayTopCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.MANAGER;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        User author = data.getUser();
        TextChannel channel = data.getChannel();
        Audible audible = data.getAudible();
        Member m = g.getMember(author);

        GuildMusicManager musicManager = audible.getGuildAudioPlayer(g);
        AudioPlayerManager playerManager = audible.getPlayerManager();

        String[] args = AlloyInputUtil.getArgs(data);

        if (!DisPermUtil.checkPermission(m, getPermission())) 
        {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length == 0)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom(getClass());
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String trackUrl = AudioHandler.getUrl(args);
        playerManager.loadItemOrdered( musicManager , trackUrl , new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) 
            {
                if (playTop(data, musicManager, track))
                {   
                    Template t = Templates.addedSongToTop(track);
                    SendableMessage sm = new SendableMessage();
                    sm.setFrom(getClass());
                    sm.setChannel(channel);
                    sm.setMessage(t.getEmbed());
                    bot.send(sm);
                }
        
            }
        
            @Override
            public void playlistLoaded(AudioPlaylist playlist) 
            {
                AudioTrack firstTrack = playlist.getSelectedTrack();
        
                if (firstTrack == null)
                    firstTrack = playlist.getTracks().get(0);

                if (playTop(data, musicManager, firstTrack))
                {
                    Template t = Templates.addedSongToTop(playlist);
                    SendableMessage sm = new SendableMessage();
                    sm.setFrom(getClass());
                    sm.setChannel(channel);
                    sm.setMessage(t.getEmbed());
                    bot.send(sm);
                }
            }
        
            @Override
            public void noMatches() 
            {
                Template t = Templates.notingFoundBy(args);
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }
        
            @Override
            public void loadFailed(FriendlyException exception) 
            {
                Template t = Templates.couldNotPlay(exception);
                SendableMessage sm = new SendableMessage();
                sm.setFrom(getClass());
                sm.setChannel(channel);
                sm.setMessage(t.getEmbed());
                bot.send(sm);
            }
        });
    }

    private boolean playTop (AlloyInputData data, GuildMusicManager musicManager, AudioTrack track)
    {
        Guild g = data.getGuild();
        Queueable q = data.getQueue();
        
        if ( AudioHandler.isConnected(g))
        {
            musicManager.getScheduler().queueTop(track);
            return true;
        }
        else
        {
            JoinCommand command = new JoinCommand();
            command.execute(data);

            Consumer<AudioTrack> consumer = new Consumer<AudioTrack>()
            {
                @Override
                public void accept(AudioTrack t) 
                {
                    musicManager.getScheduler().queue(t);
                }
            };

            DelayJob<AudioTrack> j = new DelayJob<AudioTrack>(consumer , track);
    
            q.queueIn(j, 2000L );
            return true;
        }
    }
    
}

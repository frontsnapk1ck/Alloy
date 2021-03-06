package alloy.main.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import alloy.audio.GuildMusicManager;
import alloy.event.DebugListener;
import alloy.event.DiscordInterface;
import alloy.gameobjects.Server;
import alloy.handler.command.audio.VoiceHandler;
import alloy.input.console.Console;
import alloy.io.loader.JobQueueLoaderText;
import alloy.io.loader.util.JobQueueData;
import alloy.main.Alloy;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.job.AlloyEventHandler;
import alloy.utility.job.jobs.DayJob;
import io.Saver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import utility.event.EventManager.ScheduledJob;
import utility.event.Job;
import utility.event.Worker;

public class AlloyData {

    private Map<Long, TextChannel> modLogs = new HashMap<Long, TextChannel>();
    protected Map<Long, List<Long>> cooldownUsers = new HashMap<Long, List<Long>>();
    protected Map<Long, List<Long>> xpCooldownUsers = new HashMap<Long, List<Long>>();
    private Console console = new Console();
    private AlloyEventHandler eventManger;
    
    //audio
    private AudioPlayerManager playerManager;
    private Map<Long, GuildMusicManager> musicManagers;  
    
    private JDA jda;
    private DiscordInterface discordInterface;
    
    private Alloy alloy;
    
    public AlloyData(JDA jda, Alloy alloy) 
    {
        this.jda = jda;
        this.alloy = alloy;
        console.setHandler(alloy,alloy);
        configHandlers();
    }
    
    private void configHandlers() 
    {
        configAudio();
    }

    private void configAudio() 
    {
        this.musicManagers = new HashMap<Long, GuildMusicManager>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        VoiceHandler.setAudible(alloy);
    }

    private AlloyEventHandler loadEventManager() 
    {
        AlloyEventHandler handler = new AlloyEventHandler();
        JobQueueLoaderText jqlt = new JobQueueLoaderText();
        JobQueueData data = new JobQueueData(this.alloy, AlloyUtil.EVENT_FILE);
        PriorityBlockingQueue<ScheduledJob> jobQueue = jqlt.load(data);
        if (jobQueue.size() == 0)
            Alloy.LOGGER.info("AlloyData", "there was nothing to load in the queue");
        else
            Alloy.LOGGER.info("AlloyData", "loaded the queue, there are " + jobQueue.size() + " events");
        handler.setJobQueue(jobQueue);
        Saver.clear(AlloyUtil.EVENT_FILE);
        return handler;
    }

    private Map<Long, TextChannel> loadModLogs() 
    {
        Map<Long , TextChannel> map = new HashMap<Long , TextChannel>();
       
        List<Server> servers = AlloyUtil.loadAllServers();

        for (Server server : servers) 
        {
            long id = server.getId();
            Guild g = AlloyUtil.getGuild(server , jda);

            if (g == null)
                continue;

            long mid = server.getModLogChannel();
            TextChannel tc = g.getTextChannelById(mid);
            map.put(id, tc);
        }

        return map;
    }

    public TextChannel getModLogChannel(Long gid) 
    {
        loadModLogs();
      	return this.modLogs.get(gid);
    }

    public Map<Long, List<Long>> getCooldownUsers() 
    {
        return cooldownUsers;
    }
    
    public Map<Long, List<Long>> getXpCooldownUsers()
    {
        return xpCooldownUsers;
    }

    public List<Long> getCooldownUsers(Guild g) 
    {
		return this.cooldownUsers.get(g.getIdLong());
    }

    public List<Long> getXpCooldownUsers(Guild g)
    {
        return xpCooldownUsers.get(g.getIdLong() );
    }
    
    public void update()
    {
        this.modLogs = loadModLogs();
        this.eventManger = loadEventManager();
        this.updateCooldownUsers();
        this.updateXpCooldownUsers();
        this.console.setHandler(alloy, alloy);
    }

    private void updateXpCooldownUsers() 
    {
        List<Guild> guilds = this.jda.getGuilds();
        for (Guild guild : guilds) 
        {
            long id = guild.getIdLong();
            if (!this.xpCooldownUsers.containsKey(id))
                this.xpCooldownUsers.put(id, new ArrayList<Long>());
        }
    }

    private void updateCooldownUsers() 
    {
        List<Guild> guilds = this.jda.getGuilds();
        for (Guild guild : guilds) 
        {
            long id = guild.getIdLong();
            if (!this.cooldownUsers.containsKey(id))
                this.cooldownUsers.put(id, new ArrayList<Long>());
            
        }
    }

    public void queue(Job action) 
    {
        this.eventManger.queue(action);
	}

    public void queueIn(Job action, long offset) 
    {
        if (this.eventManger == null)
            return;
        this.eventManger.queueIn(action, offset);
	}

    public AlloyEventHandler getEventHandler() 
    {
		return this.eventManger;
	}

    public boolean unQueue(Job job) 
    {
		return this.eventManger.unQueue(job);
	}

    public void makeJobs() 
    {
        Job j = new DayJob( this.jda );
        this.eventManger.queueIn( j, 86400000L );
	}

    public DebugListener getDiscordInterface() 
    {
		return this.discordInterface;
	}

    public void setDiscordInterface(DiscordInterface discordInterface) 
    {
        this.discordInterface = discordInterface;
	}

	public void addGuildMap(Guild g) 
    {
        this.cooldownUsers.put(g.getIdLong(),new ArrayList<Long>());
        this.xpCooldownUsers.put(g.getIdLong(),new ArrayList<Long>());
	}

	public List<Worker> getWorkers() 
    {
        return this.eventManger.getWorkers();
	}

    public synchronized GuildMusicManager getGuildMusicManager(Guild g)
    {
        long guildId = Long.parseLong(g.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);
    
        if (musicManager == null) {
          musicManager = new GuildMusicManager(playerManager);
          musicManagers.put(guildId, musicManager);
        }
    
        g.getAudioManager().setSendingHandler(musicManager.getSendHandler());
    
        return musicManager;
    }

    public AudioPlayerManager getPlayerManager() 
    {
        return playerManager;
    }
    

}

package alloy.event;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import alloy.gameobjects.Server;
import alloy.handler.command.audio.VoiceHandler;
import alloy.handler.util.EventHandler;
import alloy.input.discord.AlloyInput;
import alloy.input.discord.AlloyInputEvent;
import alloy.main.Alloy;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.intefs.handler.AlloyHandler;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.job.jobs.DelayJob;
import io.Saver;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utility.event.annotation.RequiredJob;

public class JDAEvents extends ListenerAdapter {

    private AlloyHandler bot;
    private Sendable sendable;
    private Queueable queueable;

    public JDAEvents(Alloy alloy) {
        this.bot = alloy;
        this.sendable = alloy;
        this.queueable = alloy;
    }

    @Override
    public void onStatusChange(StatusChangeEvent event) {
        super.onStatusChange(event);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) 
    {
        Consumer<GuildJoinEvent> con = new Consumer<GuildJoinEvent>()
        {
            public void accept(GuildJoinEvent t) 
            {
                onGuildJoinImp(e);    
            };
        };
        DelayJob<GuildJoinEvent> j = new DelayJob<GuildJoinEvent>(con , e );
        this.queueable.queue(j);
    }

    @RequiredJob
    protected void onGuildJoinImp(GuildJoinEvent e) 
    {
        Guild g = e.getGuild();
        Alloy.LOGGER.info("JDAEvents", "JOINED SERVER! " + g.getName());
        
        EventHandler.onGuildJoinEvent(g);

        Server s = AlloyUtil.loadServer(g);
        s.setLoaded(false);

        List<Member> members = g.getMembers();
        for (Member member : members)
            EventHandler.onMemberJoinEvent(member);

        this.bot.guildCountUpdate();
        this.bot.addGuildMap(g);
        s.setLoaded(true);
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) 
    {
        Guild g = e.getGuild();
        Alloy.LOGGER.info("JDAEvents", "LEFT SERVER! " + g.getName());
        EventHandler.onGuildLeaveEvent(g);

        this.bot.guildCountUpdate();
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) 
    {
        Guild g = e.getGuild();
        TextChannel c = e.getChannel();
        User u = e.getAuthor();
        Message m = e.getMessage();

        AlloyInputEvent event = new AlloyInputEvent(g, c, u, m, e);

        AlloyInput in = new AlloyInput("USER", event);
        bot.handleMessage(in);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) 
    {
        String tag = e.getAuthor().getAsTag();
        Message message = e.getMessage();
        if (e.getAuthor().equals(e.getJDA().getSelfUser()))
            return;
        if (e.getMessage().getAttachments().size() != 0) 
        {

            DiscordInterface i = Alloy.LOGGER.getDisInterface();
            for (Attachment attachment : message.getAttachments()) {
                String path = AlloyUtil.TMP_FOLDER;
                path += message.getChannel().getId() + "-" + message.getId();
                path += "." + attachment.getFileExtension();
                File f = new File(path);

                try 
                {
                    File down = attachment.downloadToFile(f).get();
                    Message m = new MessageBuilder("Message From " + tag ).build();
                    if (i.debugFile(m,down))
                        Saver.deleteFile(down.getAbsolutePath());
                    else
                        Alloy.LOGGER.info("JDAEvents", "There is a new file in the tmp folder");
                }
                catch (InterruptedException | ExecutionException e1) 
                {
                    Alloy.LOGGER.error("JDAEvents", e1);
                }
            }
        }
        Alloy.LOGGER.debug("JDA Events", "Private message received from " + tag + "\n\n" + (message.getContentRaw() == "" ? "no content" : message.getContentRaw() ));
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        super.onGuildMemberUpdateNickname(event);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) 
    {
        Consumer<GuildMemberJoinEvent> con = new Consumer<GuildMemberJoinEvent>()
        {
            public void accept(GuildMemberJoinEvent t) 
            {
                onMemberJoinImp(t);    
            };
        };
        DelayJob<GuildMemberJoinEvent> j = new DelayJob<GuildMemberJoinEvent>(con, e);
        this.queueable.queue(j);
    }
    
    protected void onMemberJoinImp(GuildMemberJoinEvent e) 
    {
        Member m = e.getMember();
        EventHandler.onMemberJoinEvent(m);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) 
    {
        Member m = e.getMember();
        EventHandler.onMemberLeaveEvent(m);
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) 
    {
        super.onGuildVoiceJoin(event);
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) 
    {
        super.onGuildVoiceMove(event);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) 
    {
        if (e.getMember().equals(e.getGuild().getSelfMember()))
            VoiceHandler.clearQueue(e.getGuild());
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) 
    {
        super.onRoleDelete(event);
    }

    @Override
    public void onCategoryCreate(CategoryCreateEvent event) 
    {
        super.onCategoryCreate(event);
    }

    @Override
    public void onCategoryDelete(CategoryDeleteEvent event) 
    {
        super.onCategoryDelete(event);
    }

    @Override
    public void onCategoryUpdateName(CategoryUpdateNameEvent event) 
    {
        super.onCategoryUpdateName(event);
    }

    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event) 
    {
        super.onGuildInviteCreate(event);
    }

    @Override
    public void onGuildInviteDelete(GuildInviteDeleteEvent event) 
    {
        super.onGuildInviteDelete(event);
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) 
    {
        super.onGuildUnban(event);
    }

    @Override
    public void onGuildBan(GuildBanEvent e) 
    {
        EventHandler.onGuildBan(e.getGuild() , e.getUser() , this.sendable);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) 
    {
        super.onGuildMemberRoleAdd(event);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) 
    {
        super.onGuildMemberRoleRemove(event);
    }

    @Override
    public void onRoleCreate(RoleCreateEvent event) 
    {
        super.onRoleCreate(event);
    }

    @Override
    public void onRoleUpdateColor(RoleUpdateColorEvent event) 
    {
        super.onRoleUpdateColor(event);
    }

    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event) 
    {
        super.onRoleUpdateName(event);
    }

    @Override
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) 
    {
        super.onRoleUpdatePermissions(event);
    }
}

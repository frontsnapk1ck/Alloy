package alloy.command.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Server;
import alloy.handler.command.fun.RankHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPerm;
import alloy.utility.discord.perm.DisPermUtil;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.Util;

public class SetCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission())) {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args.length < 1) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("spam"))
            setSpam(data);
        else if (args[0].equalsIgnoreCase("xp"))
            setXp(data);
        else if (args[0].equalsIgnoreCase("admin-bypass"))
            adminBypass(data);
        else if (args[0].equalsIgnoreCase("mod-log"))
            modLog(data);
        else if (args[0].equalsIgnoreCase("mute"))
            mute(data);
        else if (args[0].equalsIgnoreCase("appeal"))
            appeal(data);
            
    }
    

    private void appeal(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        try 
        {
            if (!args[1].equalsIgnoreCase("none"))
                new URL(args[1]);
        }
        catch (MalformedURLException e) 
        {
            Template t = Templates.invalidURL(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
		}

        Server s = AlloyUtil.loadServer(g);
        s.setBanAppealLink(args[1]);

        Template t = Templates.banAppealChanged(args[1]);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

    private void mute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String role = args[1];

        Role r = DisUtil.parseRole( role , g );
        if (r == null && !role.equalsIgnoreCase("none"))
        {
            Template t = Templates.roleNotFound(role);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            sm.setFrom(getClass());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        s.setMuteRole(r.getIdLong());

        Template t = Templates.muteRoleChanged(role);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

    private void modLog(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String channelT = args[1];

        TextChannel tc = DisUtil.findChannel(g, channelT);
        if (tc == null && !channelT.equalsIgnoreCase("none"))
        {
            Template t = Templates.channelNotFound(channelT);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            sm.setFrom(getClass());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        try 
        {
            s.setModLog(tc.getIdLong());
        } 
        catch (NullPointerException e) 
        {
            s.setModLog(0);
        }

        Template t = Templates.modLogChanged(channelT);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        sm.setFrom(getClass());
        bot.send(sm);
    }

    private void adminBypass(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        if (args.length < 2) 
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String bool = args[1];
        Server s = AlloyUtil.loadServer(g);
        if (bool.equalsIgnoreCase("false") || bool.equalsIgnoreCase("off"))
        {
            s.changeAdminCooldownBypass(false);

            Template t = Templates.adminBypassCooldown(false);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        else if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("on"))
        {
            s.changeAdminCooldownBypass(true);

            Template t = Templates.adminBypassCooldown(true);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }
        else
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
        }


    }

    private void setXp(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        User tarU = DisUtil.parseUser(args[1]);

        if (args.length < 3) 
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (tarU == null) {
            Template t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member target = g.getMember(tarU);

        if (target == null) {
            Template t = Templates.userNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!Util.validInt(args[2])) {
            Template t = Templates.invalidNumberFormat(args[2]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        int xp = Integer.parseInt(args[2]);

        RankHandler.setXP(target, xp);
        Template t = Templates.xpSetSuccess(target, xp);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        return;

    }

    private void setSpam(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        TextChannel target = DisUtil.findChannel(g, DisUtil.mentionToId(args[1]));
        if (target == null) {
            Template t = Templates.invalidChannel(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Server s = AlloyUtil.loadServer(g);
        s.changeSpamChannel(target.getIdLong());
        Template t = Templates.spamChannelChanged(target);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);

    }

}

package alloy.command.administration;

import alloy.builder.loaders.PlayerLoaderText;
import alloy.command.util.AbstractCommand;
import alloy.gameobjects.Warning;
import alloy.gameobjects.player.Player;
import alloy.handler.WarningHandeler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.DisUtil;
import alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;


public class WarnCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {

        Guild g = data.getGuild();
        TextChannel chan = data.getChannel();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);

        if ( args.length < 2 )
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setFrom("WarnCommand");
            sm.setChannel(channel);
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("del"))
        {
            delwarn(data);
            return;
        }

        Member target = DisUtil.findMember(g, args[0] );

        if (target == null)
        {
            Template t = Templates.userNotFound( args[0] );
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member warner = g.getMember(author);
        if (!DisPermUtil.checkPermission(warner, getPermission()))
        {
            Template t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Warning w = WarningHandeler.newWarning(args , (TextChannel) channel , target , author , g);

        PlayerLoaderText plt = new PlayerLoaderText();
        String path = AlloyUtil.SERVERS_PATH + AlloyUtil.SUB + g.getId() + AlloyUtil.USER_FOLDER + AlloyUtil.SUB + m.getIdLong();
        Player p = plt.load(path);
        
        p.addWanring(w);
        PrivateChannel pc = target.getUser().openPrivateChannel().complete();
        if (pc == null)
        {
            Template t = Templates.privateMessageFailed(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WarnCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Template warn = Templates.getWarn(w);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setFrom("WarnCommand");
        sm.setMessage(warn.getEmbed());
        bot.send(sm);

        Template t = Templates.warnSucsess(target , w , author);
        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(chan);
        sm2.setFrom("WarnCommand");
        sm2.setMessage(t.getEmbed());
        bot.send(sm2);
    }

    private void delwarn (AlloyInputData data )
    {
        Guild guild = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = guild.getMember(author);

        if (!DisPermUtil.checkPermission(m, getPermission()))
        {
            Template t = Templates.noPermission(getPermission() , author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RemoveWarnings");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (!WarningHandeler.warningExists(guild , args[1]))
        {
            Template t = Templates.warningNotFound(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("RemoveWarnings");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        Member warned = WarningHandeler.removeWarnings(guild , args[1] );

        Template t = Templates.warningsRemovedSucsess(args[0] , warned);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("RemoveWarnings");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

}
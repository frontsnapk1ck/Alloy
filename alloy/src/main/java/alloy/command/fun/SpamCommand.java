package alloy.command.fun;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.command.fun.SpamHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.intefs.handler.CooldownHandler;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SpamCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        String[] args = AlloyInputUtil.getArgs(data);
        Sendable bot = data.getSendable();
        CooldownHandler handler = data.getCooldownHandler();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable queueable = data.getQueue();

        if (userOnCooldown(author, g, handler)) {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (SpamHandler.isStart(args)) 
        {
            MessageEmbed embed = startSpam(channel, args, author, queueable , bot);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(embed);
            bot.send(sm);
            return;
        }

        else if (SpamHandler.isStop(args)) {
            MessageEmbed embed = stopSpam(args);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(embed);
            bot.send(sm);
            return;
        }

        Template t = Templates.argumentsNotRecognized(data.getMessageActual());
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private MessageEmbed stopSpam(String[] args) {
        Long id = 0l;
        try {
            id = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            Template t = Templates.invalidNumberFormat(args);
            return t.getEmbed();
        }

        if (SpamHandler.stopSpam(id)) {
            Template t = Templates.spamRunnableStopped(id);
            return t.getEmbed();
        } else {
            Template t = Templates.spamRunnableIdNotFound(id);
            return t.getEmbed();
        }
    }

    private MessageEmbed startSpam(TextChannel chan, String[] args, User author, Queueable queueable, Sendable bot) 
    {
        if (!SpamHandler.validCommand(args)) 
        {
            Template temp = Templates.invalidNumberFormat(args);
            return temp.getEmbed();
        }

        Long id = SpamHandler.makeRunnable(chan.getGuild(), args, author, bot, queueable).getID();

        Template temp = Templates.spamRunnableCreated(id);
        return temp.getEmbed();
    }

}

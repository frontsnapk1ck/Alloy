package alloy.command.fun;

import alloy.command.util.AbstractCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Queueable;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import alloy.utility.job.jobs.RemindJob;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import utility.StringUtil;
import utility.time.TimeUtil;

public class RemindCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) {
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();

        if (args.length < 1) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        if (args[0].equalsIgnoreCase("dm"))
            remindDM(data);
        else
            remindNormal(data);
    }

    private void remindNormal(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        long delay = TimeUtil.toMillis(args[0]);

        if (delay == 0l) {
            Template t = Templates.timeNotRecognized(args[0]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String out = StringUtil.joinStrings(args, 1);

        Template template = Templates.remindCard(args[0], out);

        Template t = Templates.remindMe(out);
        Message outM = new MessageBuilder().setEmbed(t.getEmbed()).append(m.getAsMention()).build();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(outM);

        RemindJob job = new RemindJob(bot, sm);
        q.queueIn(job, delay);

        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(channel);
        sm2.setFrom(getClass());
        sm2.setMessage(template.getEmbed());
        bot.send(sm2);
    }

    private void remindDM(AlloyInputData data) {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        String[] args = AlloyInputUtil.getArgs(data);
        TextChannel channel = data.getChannel();
        Message msg = data.getMessageActual();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();

        if (args.length < 2) {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        long delay = TimeUtil.toMillis(args[1]);

        if (delay == 0l) {
            Template t = Templates.timeNotRecognized(args[1]);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        PrivateChannel pc = m.getUser().openPrivateChannel().complete();
        if (pc == null) {
            Template t = Templates.privateMessageFailed(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }

        String out = StringUtil.joinStrings(args, 2);

        Template template = Templates.remindCard(args[1], out);

        Template t = Templates.remindMeDM(out, msg);
        Message outM = new MessageBuilder().setEmbed(t.getEmbed()).append(m.getAsMention()).build();
        SendableMessage sm = new SendableMessage();
        sm.setChannel(pc);
        sm.setFrom(getClass());
        sm.setMessage(outM);

        RemindJob job = new RemindJob(bot, sm);
        q.queueIn(job, delay);

        SendableMessage sm2 = new SendableMessage();
        sm2.setChannel(channel);
        sm2.setFrom(getClass());
        sm2.setMessage(template.getEmbed());
        bot.send(sm2);

    }

}

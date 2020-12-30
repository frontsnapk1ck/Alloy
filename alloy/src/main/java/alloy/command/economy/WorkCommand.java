package alloy.command.economy;

import java.util.List;

import alloy.command.util.AbstractCooldownCommand;
import alloy.gameobjects.Server;
import alloy.gameobjects.player.Player;
import alloy.input.discord.AlloyInputData;
import alloy.main.Queueable;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.main.handler.CooldownHandler;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.AlloyUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class WorkCommand extends AbstractCooldownCommand {

    public static final int MIN_WORK = 30;
    public static final int WORK_RANGE = 120;

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        CooldownHandler handler = data.getCooldownHandler();
        Member m = g.getMember(author);
        Queueable q = data.getQueue();
        
        if (userOnCooldown(author, g, handler))
        {
            Template t = Templates.onCooldown(m);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("WorkCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);  
            return;
        }

        int amt = getAmt();
        String option = getOption(g);
        Player p = AlloyUtil.loadPlayer(g, m);
        p.addBal(amt);

        Template t = Templates.workSucsess( option , amt );
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("WorkCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);

        addUserCooldown(author, g, handler, getCooldownTime(g) , q);

    }

    private String getOption(Guild g) 
    {
        Server s = AlloyUtil.loadServer(g);
        List<String> workOptions = AlloyUtil.loadWorkOptions(s);

        int num = (int) (Math.random() * workOptions.size());
        return workOptions.get(num);
    }

    private int getAmt() 
    {
        int amt = (int) (Math.random() * WORK_RANGE + MIN_WORK);
        return amt;
    }
    
}
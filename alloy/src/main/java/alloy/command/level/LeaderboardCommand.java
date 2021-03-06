package alloy.command.level;

import java.util.List;

import alloy.command.util.AbstractCooldownCommand;
import alloy.handler.command.econ.BankHandler;
import alloy.handler.command.fun.RankHandler;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import alloy.templates.Templates;
import disterface.util.template.Template;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class LeaderboardCommand extends AbstractCooldownCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        String[] args = AlloyInputUtil.getArgs(data);

        if (args.length == 0)
            rankLB(data);
        else if (   args[0].equalsIgnoreCase("cash") ||
                    args[0].equalsIgnoreCase("money") )
            rankMoney(data);
    }

    private void rankMoney(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        List<String> lb = BankHandler.loadLeaderboardMoney(g);
        Template t = Templates.leaderboard(lb);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

    private void rankLB(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        
        List<String> lb = RankHandler.loadLeaderboard(g);
        Template t = Templates.leaderboard(lb);
        SendableMessage sm = new SendableMessage();
        sm.setFrom(getClass());
        sm.setChannel(channel);
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }

}

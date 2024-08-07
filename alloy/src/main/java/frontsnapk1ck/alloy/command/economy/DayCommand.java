package frontsnapk1ck.alloy.command.economy;

import java.util.List;

import frontsnapk1ck.alloy.command.util.AbstractCommand;
import frontsnapk1ck.alloy.gameobjects.player.Player;
import frontsnapk1ck.alloy.input.discord.AlloyInputData;
import frontsnapk1ck.alloy.main.Alloy;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.util.SendableMessage;
import frontsnapk1ck.alloy.templates.AlloyTemplate;
import frontsnapk1ck.alloy.templates.Templates;
import frontsnapk1ck.alloy.utility.discord.AlloyUtil;
import frontsnapk1ck.alloy.utility.discord.perm.DisPerm;
import frontsnapk1ck.alloy.utility.discord.perm.DisPermUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;


public class DayCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Guild g = data.getGuild();
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        Member m = g.getMember(author);
        
        if (!DisPermUtil.checkPermission(m, getPermission()))
        {
            AlloyTemplate t = Templates.noPermission(getPermission(), author);
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom(getClass());
            sm.setMessage(t.getEmbed());
            bot.send(sm);   
            return;
        }

        List<Player> players = AlloyUtil.loadAllPlayers(g);
        for (Player p : players)
            p.day();

        AlloyTemplate t = Templates.daySuccess(g);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);  
    }

    public static void dayAll(List<Guild> guilds) 
    {
        for (Guild guild : guilds) 
        {
            List<Player> players = AlloyUtil.loadAllPlayers(guild);
            for (Player p : players)
            {
                try
                {
                    p.day();      
                }
                catch (Exception e)
                {
                    User u = AlloyUtil.getMember(p, guild.getJDA()).getUser();
                    Alloy.LOGGER.warn("DayCommand", "Could not do the day command for the user " + u.getAsTag() + " " + u.getId() + " in the guild " + guild.getName() + " " + guild.getId() + " with the error type of " + e.getClass().getSimpleName() + " with the message: " + e.getMessage());
                }

            }
        }
        Alloy.LOGGER.info("DayCommand", "the day has advanced for all guilds");
	}
    
}

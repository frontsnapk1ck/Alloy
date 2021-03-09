package alloy.command.info;

import alloy.command.util.AbstractCommand;
import alloy.input.discord.AlloyInputData;
import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import disterface.util.template.Template;
import alloy.templates.Templates;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class DonateCommand extends AbstractCommand {

    @Override
    public void execute(AlloyInputData data) 
    {
        User author = data.getUser();
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();

        Template t = Templates.donate(author);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom(getClass());
        sm.setMessage(t.getEmbed());
        bot.send(sm);
    }
    
}

package alloy.utility.job.jobs;

import java.util.List;

import alloy.main.intefs.Sendable;
import alloy.main.util.SendableMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import utility.event.Job;

public class HelpJob extends Job {

    private Sendable bot;
    private List<MessageEmbed> messages;
    private User u;

    public HelpJob(User u, List<MessageEmbed> messages, Sendable bot) 
    {
        this.u = u;
        this.messages = messages;
        this.bot = bot;
    }

    @Override
    public void execute() 
    {
        PrivateChannel c = u.openPrivateChannel().complete();

        for (MessageEmbed embed : messages) 
        {
            SendableMessage sm = new SendableMessage();
            sm.setChannel(c);
            sm.setFrom("HelpCommand");
            sm.setMessage(embed);
            bot.send(sm);
        }
    }
    
}

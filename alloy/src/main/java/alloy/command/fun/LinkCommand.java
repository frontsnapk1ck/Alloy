package alloy.command.fun;

import java.util.function.Consumer;

import alloy.command.util.AbstractCommand;
import alloy.input.AlloyInputUtil;
import alloy.input.discord.AlloyInputData;
import alloy.main.Alloy;
import alloy.main.Sendable;
import alloy.main.SendableMessage;
import alloy.templates.Template;
import alloy.templates.Templates;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import utility.StringUtil;

public class LinkCommand extends AbstractCommand {

    @Override
    public DisPerm getPermission() 
    {
        return DisPerm.ADMINISTRATOR;
    }

    @Override
    public void execute(AlloyInputData data) 
    {
        Sendable bot = data.getSendable();
        TextChannel channel = data.getChannel();
        String[] args = AlloyInputUtil.getArgs(data);
        Message msg = data.getMessageActual();

        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("KickCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, consumer);
        
        if (args.length < 2)
        {
            Template t = Templates.argumentsNotSupplied(args, getUsage());
            SendableMessage sm = new SendableMessage();
            sm.setChannel(channel);
            sm.setFrom("CooldownCommand");
            sm.setMessage(t.getEmbed());
            bot.send(sm);
            return;
        }
        String link = args[0];
        String text = StringUtil.joinStrings(args,1);
        Template t = Templates.linkEmbed(link,text);
        SendableMessage sm = new SendableMessage();
        sm.setChannel(channel);
        sm.setFrom("CooldownCommand");
        sm.setMessage(t.getEmbed());
        bot.send(sm);
        
        msg.delete().queue(null,handler);

    }
    
}
package alloy.command.administration;

import java.util.function.Consumer;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.main.Alloy;
import alloy.main.Sendable;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class BanCommand extends AbstractModerationCommand {

    @Override
    public String getDescription()
    {
        return "bans a member from your guild";
    }

    @Override
    public String getCommand()
    {
        return "ban";
    }

    @Override
    public String[] getAliases()
    {
        return new String[0];
    }

    @Override
    protected DisPerm getRequiredPermission()
    {
        return DisPerm.BAN_MEMBERS;
    }

    @Override
    protected PunishType getPunishType()
    {
        return PunishType.BAN;
    }

    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member) 
    {
        Consumer<ErrorResponseException> consumer = new Consumer<ErrorResponseException>() 
        {
            @Override
            public void accept(ErrorResponseException t) 
            {
                Alloy.LOGGER.warn("BanCommand", t.getMessage());
            }

            @Override
            public Consumer<ErrorResponseException> andThen(Consumer<? super ErrorResponseException> after) 
            {
                return Consumer.super.andThen(after);
            }
        };
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, consumer);

        guild.ban(member, 7).queue(null,handler);
        return true;
    }
    
}

package alloy.command.administration;

import java.util.function.Consumer;

import alloy.command.util.AbstractModerationCommand;
import alloy.command.util.PunishType;
import alloy.gameobjects.Server;
import alloy.main.Alloy;
import alloy.main.intefs.Sendable;
import alloy.utility.discord.AlloyUtil;
import alloy.utility.discord.perm.DisPerm;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class UnmuteCommand extends AbstractModerationCommand {

    @Override
    protected DisPerm getRequiredPermission() 
    {
        return DisPerm.MANAGE_ROLES;
    }

    @Override
    protected PunishType getPunishType() 
    {
        return PunishType.UNMUTE;
    }

    @Override
    protected boolean punish(Sendable bot, Guild guild, Member member) 
    {

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
        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, consumer);


        Server s = AlloyUtil.loadServer(guild);
        Role mute = guild.getRoleById(s.getMuteRollID());
        if (mute == null)
            return false;
        
        guild.removeRoleFromMember(member, mute).queue(null, handler);
        return true;
    }

}

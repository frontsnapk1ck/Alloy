package botcord.components.message.message.button;

import java.util.Set;

import botcord.BotCord;
import botcord.components.message.message.button.util.AbstractMessageButtons;
import botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;

@SuppressWarnings("serial")
public class MessagePinButton extends AbstractMessageButtons {

    public MessagePinButton(Message message) 
    {
        super(message);
	}

	@Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void config() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() 
    {
        updateImage();
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        try 
        {
            if (getMessage().isPinned())
                getMessage().unpin().complete();
            else
                getMessage().pin().complete();
        }
        catch (Exception e) 
        {
            String action = this.getMessage().isPinned() ? "unpin" : "pin";
            BotCord.LOGGER.warn("MessagePinButton", "i couldn't " + action + " this message");
        }
    }

    @Override
    protected String getImageLink() 
    {
        return BCUtil.PIN_ICON;
    }
    
}

package frontsnapk1ck.botcord.components.message.message.button;

import java.util.Set;

import frontsnapk1ck.botcord.components.message.message.button.util.AbstractMessageButtons;
import frontsnapk1ck.botcord.util.BCUtil;
import net.dv8tion.jda.api.entities.Message;

public class MessageReplyButton extends AbstractMessageButtons {

    public MessageReplyButton(Message message) 
    {
        super(message);
	}

	@Override
    public void init() 
    {
    }

    @Override
    public void config() 
    {
        setImage();
    }

    @Override
    public void update() 
    {
        updateImage();
    }

    @Override
    protected void onRightClick(Set<MouseModifiers> modifiers) 
    {
        // coming soon
    }

    @Override
    protected String getImageLink() 
    {
        return BCUtil.REPLY_ICON;
    }
    
}

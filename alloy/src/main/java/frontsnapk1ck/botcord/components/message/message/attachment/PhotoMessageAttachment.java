package frontsnapk1ck.botcord.components.message.message.attachment;

public class PhotoMessageAttachment extends AbstractMessageAttachment {

	private String link;

    public PhotoMessageAttachment(String string) 
    {
        this.link = string;
        init();
        config();
    }

    @Override
    public void config() 
    {
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateBounds() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }
    
}

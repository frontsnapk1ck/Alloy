package frontsnapk1ck.alloy.input.console;

import java.util.List;

import frontsnapk1ck.alloy.main.intefs.Queueable;
import frontsnapk1ck.alloy.main.intefs.Sendable;
import frontsnapk1ck.alloy.main.intefs.handler.AlloyHandler;
import net.dv8tion.jda.api.JDA;

public class ConsoleInputData {

    private Sendable sendable;
    private AlloyHandler bot;
    private Queueable queue;
    private List<String> args;
    private JDA jda;

    public ConsoleInputData() {
        super();
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }

    public void setQueue(Queueable queue) {
        this.queue = queue;
    }

    public void setSendable(Sendable sendable) {
        this.sendable = sendable;
    }

    public void setBot(AlloyHandler bot) {
        this.bot = bot;
    }

    public List<String> getArgs() {
        return args;
    }

    public JDA getJda() {
        return jda;
    }

    public Queueable getQueue() {
        return queue;
    }

    public Sendable getSendable() {
        return sendable;
    }

    public AlloyHandler getBot() {
        return bot;
    }

}

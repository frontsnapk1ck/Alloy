package alloy.input.builder;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import alloy.input.console.ConsoleInput;
import input.Input;
import input.InputMap;
import net.dv8tion.jda.api.JDA;

public class ConsoleInputMapBuilder {
    
    public static InputMap load(Element file, JDA jda) 
    {
        file.normalize();

        List<Input> inputs = loadAllInputs(file , jda);

        InputMap map = new InputMap();

        for (Input input : inputs)
            map.put(input, input.getName());

        return map;
    }

    private static List<Input> loadAllInputs(Element elem, JDA jda) 
    {
        NodeList nList = elem.getElementsByTagName("command");

        List<Input> inputs = new ArrayList<Input>();

        for (int i = 0; i < nList.getLength(); i++) 
        {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) 
            {
                Element cE = (Element) node;
                inputs.addAll(loadSubInputs(cE , jda));
            }
        }

        return inputs;

    }

    private static List<Input> loadSubInputs(Element cE, JDA jda) 
    {
        List<String> triggers = loadTrigger(cE);
        List<String> descriptions = loadDescriptions(cE , triggers.size() );
        List<String> subs = loadSubs(cE , triggers);
        List<Input> inputs = new ArrayList<Input>();


        for (int i = 0; i < subs.size(); i++) 
        {
            String command = subs.get(i);
            int index = i % descriptions.size();
            String description = descriptions.get(index);
            Input in = makeInput( cE , command , description , jda );
            inputs.add(in);
        }

        return inputs;
    }

    private static List<String> loadDescriptions(Element cE, int j) 
    {
        List<String> des = new ArrayList<String>();
        NodeList subcommands = cE.getElementsByTagName("subcommand");

        for (int i = 0; i < subcommands.getLength(); i++) 
        {
            int index = i % subcommands.getLength();
            Node n = subcommands.item(index);
            Element e = (Element) n;
            des.add(e.getAttribute("description"));
        }
        return des;
    }

    private static List<String> loadTrigger(Element cE) 
    {
        List<String> des = new ArrayList<String>();
        NodeList subcommands = cE.getElementsByTagName("subcommand");
        for (int i = 0; i < subcommands.getLength(); i++) 
        {
            Node n = subcommands.item(i);
            Element e = (Element) n;
            des.add(e.getAttribute("text"));
        }
        return des;
    }

    private static Input makeInput(Element elem, String command, String descrption, JDA jda) 
    {
        String name = getName(elem);

        Input in = new ConsoleInput(name, command, descrption , jda );
        return in;
    }

    private static String getName(Element elem) 
    {
        return elem.getAttribute("name");
    }

    private static List<String> loadSubs(Element elem, List<String> subs) 
    {
        String childName = "trigger";
        String attribute = "text";
        List<String> list = new ArrayList<String>();
        List<String> triggers = new ArrayList<String>();

        int len = elem.getElementsByTagName(childName).getLength();
        for (int i = 0; i < len; i++) 
        {
            Node childN = elem.getElementsByTagName(childName).item(i);
            Element child = (Element) childN;
            String sub = child.getAttribute(attribute);
            triggers.add(sub);
        }

        for (String t : triggers) 
        {
            for (String s : subs) 
            {
                String command = t;
                command += " " + s;
                command = command.trim();
                list.add(command);    
            }    
        }

        return list;
    }

}
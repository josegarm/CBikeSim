package es.cbikesim.lib.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class responsible for invoking method execute() of Command
 */
public class Invoker {

    private List<Command> commands = new ArrayList<>();

    public void setCommand(Command command) {
        this.commands.clear();
        this.commands.add(command);
    }

    public void addCommand(Command command){
        this.commands.add(command);
    }

    public void invoke(){
        for (Command command : commands) {
            command.execute();
        }
    }

}

package es.cbikesim.lib.pattern;

import es.cbikesim.lib.exception.UseCaseException;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class responsible for invoking method execute() of Command
 */
public class Invoker {

    private List<Command> commands = new ArrayList<>();

    public void setCommand(Command command) {
        this.clear();
        this.commands.add(command);
    }

    public void addCommand(Command command){
        this.commands.add(command);
    }

    public void clear(){
        this.commands.clear();
    }

    public void invoke() throws UseCaseException{
        for (Command command : commands) {
            command.execute();
        }
    }

}

package es.cbikesim.lib.pattern;


public class Invoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void invoke(){
        this.command.execute();
    }

}

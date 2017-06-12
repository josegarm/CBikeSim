package es.cbikesim.lib.pattern;

import es.cbikesim.lib.exception.UseCaseException;

/**
 * Implemented by all use cases.
 * Define method execute.
 */
public interface Command {

    /**
     * Definition of method responsible for executing the use case.
     *
     * @return void
     */
    public void execute() throws UseCaseException;

}

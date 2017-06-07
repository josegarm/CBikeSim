package es.cbikesim.game.presenter;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.usecase.CreateScenarioUseCase;
import es.cbikesim.lib.pattern.Command;
import es.cbikesim.lib.pattern.Invoker;

public class GamePresenter implements Game.Presenter {

    private Game.View view;
    private Scenario scenario;

    private Invoker invoker = new Invoker();

    @Override
    public void createScenario(int difficult) {
        this.scenario = new Scenario();
        Command createScenario = new CreateScenarioUseCase(scenario);
        invoker.setCommand(createScenario);
        invoker.invoke();

    }

    @Override
    public void setView(Game.View view) {
        this.view = view;
    }


}

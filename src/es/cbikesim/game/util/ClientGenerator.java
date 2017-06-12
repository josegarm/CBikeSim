package es.cbikesim.game.util;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Client;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.util.strategies.CriticalStrategy;
import es.cbikesim.game.util.strategies.NullStrategy;
import es.cbikesim.game.util.strategies.RandomStrategy;
import es.cbikesim.game.util.strategies.Strategy;
import es.cbikesim.lib.util.StopRun;

public class ClientGenerator extends Thread implements StopRun{

    public final static int NULL = 0, RANDOM = 1, CRITICAL_MORNING = 2, CRITICAL_AFTERNOON = 3;

    private boolean alive;
    private int waitSeconds;

    private Game.Presenter context;
    private Scenario scenario;
    private Strategy strategy;

    public ClientGenerator(int strategy, Scenario scenario, Game.Presenter context, int waitSeconds) {
        this.scenario = scenario;
        this.context = context;
        this.waitSeconds = waitSeconds;
        this.changeStrategy(strategy);
    }

    @Override
    public void run() {
        this.alive = true;
        while (alive) {
            try {
                Thread.sleep(waitSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client client = generateClient();
            if (client != null) {
                context.notifyNewClient(client);
            }
        }
    }

    @Override
    public void stopRun() {
        this.alive = false;
    }

    public Client generateClient() {
        return strategy.generateClient();
    }

    public void changeStrategy(int selectedStrategy) {
        switch (selectedStrategy) {
            case NULL:
                strategy = new NullStrategy();
                break;
            case RANDOM:
                strategy = new RandomStrategy(scenario);
                break;
            case CRITICAL_MORNING:
                strategy = new CriticalStrategy(scenario, Station.MORNING);
                break;
            case CRITICAL_AFTERNOON:
                strategy = new CriticalStrategy(scenario, Station.AFTERNOON);
                break;
            default:
                strategy = null;
        }
    }

    public void setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
    }
}

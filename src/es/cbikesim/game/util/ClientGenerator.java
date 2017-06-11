package es.cbikesim.game.util;

import es.cbikesim.game.contract.Game;
import es.cbikesim.game.model.Scenario;
import es.cbikesim.game.model.Station;
import es.cbikesim.game.util.strategies.CriticalStrategy;
import es.cbikesim.game.util.strategies.RandomStrategy;
import es.cbikesim.game.util.strategies.Strategy;

public class ClientGenerator extends Thread {

    public final static int RANDOM = 0, CRITICAL_MORNING = 1, CRITICAL_AFTERNOON = 2;

    private boolean alive;
    private int waitTime;

    private Game.Presenter context;
    private Scenario scenario;
    private Strategy strategy;

    public ClientGenerator(Scenario scenario, Game.Presenter context, int waitTime){
        this.scenario = scenario;
        this.context = context;
        this.waitTime = waitTime;
        this.strategy = new RandomStrategy(scenario);
    }

    @Override
    public void run(){
        this.alive = true;
        while (alive){
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            generateClient();
        }
    }

    public void cancel(){
        this.alive = false;
    }

    public void generateClient(){
        strategy.generateClient();
    }

    public void changeStrategy(int selectedStrategy){

        switch (selectedStrategy) {
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

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}

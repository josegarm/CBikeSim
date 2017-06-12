package es.cbikesim.game.util;

import es.cbikesim.lib.util.StopRun;

import java.util.Timer;
import java.util.TimerTask;

import static es.cbikesim.game.presenter.GamePresenter.*;

public class ClientGeneratorStrategySelector implements StopRun {

    private ClientGenerator clientGenerator;
    private Timer timer;

    private int time;
    private int difficulty;

    public ClientGeneratorStrategySelector(ClientGenerator clientGenerator, int time, int difficulty){
        this.clientGenerator = clientGenerator;
        this.time = time;
        this.difficulty = difficulty;
        this.timer = new Timer();
    }

    public void start(){
        timer.schedule(
                new TimerTask() {
                    private int timeLeft = time;
                    @Override
                    public void run() {
                        checkOption(timeLeft);
                        timeLeft--;
                    }
        }, 0, 1000);
    }

    @Override
    public void stopRun(){
        timer.cancel();
        timer.purge();
    }

    public void checkOption(int timeLeft){
        switch (difficulty) {
            case EASY:
                checkEasy(timeLeft);
                break;
            case NORMAL:
                checkNormal(timeLeft);
                break;
            case HARD:
                checkHard(timeLeft);
                break;
            case CUSTOM:
                checkNormal(timeLeft);
                break;
            default:
        }
    }

    public void checkEasy(int timeLeft){
        if(timeLeft == (time/10) * 9){
            clientGenerator.changeStrategy(ClientGenerator.CRITICAL_MORNING);
        }
        else if (timeLeft == (time/10) * 8){
            clientGenerator.changeStrategy(ClientGenerator.NULL);
        }
        else if (timeLeft == (time/10) * 7){
            clientGenerator.changeStrategy(ClientGenerator.RANDOM);
        }
        else if (timeLeft == (time/10) * 3){
            clientGenerator.changeStrategy(ClientGenerator.CRITICAL_AFTERNOON);
        }
        else if (timeLeft == (time/10)){
            clientGenerator.changeStrategy(ClientGenerator.RANDOM);
        }
    }

    public void checkNormal(int timeLeft){
        if(timeLeft == (time/10) * 9){
            clientGenerator.changeStrategy(ClientGenerator.CRITICAL_MORNING);
        }
        else if (timeLeft == (time/10) * 7){
            clientGenerator.changeStrategy(ClientGenerator.RANDOM);
        }
        else if (timeLeft == (time/10) * 4){
            clientGenerator.changeStrategy(ClientGenerator.CRITICAL_AFTERNOON);
        }
        else if (timeLeft == (time/10 * 2)){
            clientGenerator.changeStrategy(ClientGenerator.RANDOM);
        }
    }

    public void checkHard(int timeLeft){
        if(timeLeft == (time/10) * 9){
            clientGenerator.changeStrategy(ClientGenerator.CRITICAL_MORNING);
        }
        else if (timeLeft == (time/10) * 6){
            clientGenerator.changeStrategy(ClientGenerator.RANDOM);
        }
        else if (timeLeft == (time/10) * 3){
            clientGenerator.changeStrategy(ClientGenerator.CRITICAL_AFTERNOON);
        }
    }
}

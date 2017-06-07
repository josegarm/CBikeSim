package es.cbikesim.game.contract;

public interface Game {

    interface View {
        void start();
    }

    interface Presenter {
        void createScenario(int difficult);
        void setView(Game.View view);
    }

}

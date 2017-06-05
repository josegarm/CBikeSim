package es.cbikesim.game.contract;

public interface Game {

    interface View {

    }

    interface Presenter {
        void setView(Game.View view);
    }

}

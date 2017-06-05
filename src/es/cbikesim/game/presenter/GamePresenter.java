package es.cbikesim.game.presenter;

import es.cbikesim.game.contract.Game;

public class GamePresenter implements Game.Presenter {

    private Game.View view;

    @Override
    public void setView(Game.View view) {
        this.view = view;
    }
}

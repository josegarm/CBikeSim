package es.cbikesim.mainMenu.presenter;

import es.cbikesim.mainMenu.contract.MainMenu;

public class MainMenuPresenter implements MainMenu.Presenter{

    private MainMenu.View view;


    @Override
    public void setView(MainMenu.View view) {
        this.view = view;
    }
}

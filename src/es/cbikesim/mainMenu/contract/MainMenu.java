package es.cbikesim.mainMenu.contract;

public interface MainMenu {

    interface View{

    }

    interface Presenter{

        void setView(MainMenu.View view);

    }

}

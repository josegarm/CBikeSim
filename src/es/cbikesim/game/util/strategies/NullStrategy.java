package es.cbikesim.game.util.strategies;

import es.cbikesim.game.model.Client;

public class NullStrategy implements Strategy {

    public NullStrategy() {
    }

    @Override
    public Client generateClient() {
        return null;
    }


}

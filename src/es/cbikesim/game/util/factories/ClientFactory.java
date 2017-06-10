package es.cbikesim.game.util.factories;

import es.cbikesim.game.model.Client;

public class ClientFactory {

    private static int id = 0;

    public static Client makeClient(){
        return new Client("Client " + id++);
    }
}

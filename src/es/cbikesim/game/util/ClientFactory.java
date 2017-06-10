package es.cbikesim.game.util;

import es.cbikesim.game.model.Client;

public class ClientFactory {

    private static int id = 0;

    public static Client makeClient(){
        return new Client("Client " + id);
    }
}

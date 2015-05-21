/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.IOException;

/**
 *
 * @author Jeynald
 */
public class NetworkClient {
    public static final int APP_PORT_NUMBER = 8888;
    public static final String APP_HOST_ADDRESS = "localhost";
    Client myClient;
    
    public NetworkClient() {
        try {
            myClient = Network.connectToServer(APP_HOST_ADDRESS, APP_PORT_NUMBER);
            myClient.start();
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
        }
    }
    
}

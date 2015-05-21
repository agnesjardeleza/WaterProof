package waterproof;

import com.jme3.app.SimpleApplication;

import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;

/**
 * test
 * @author jjlendaya
 */
public class ClientMain extends SimpleApplication {
    
    public Client client;
    private GameAppState gameAppState;
    private MainMenuAppState menuAppState;
    
    public static void main(String[] args) {
        ClientMain app = new ClientMain();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //Setup the Camera
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0, 0, 0.5f));
        getFlyByCamera().setEnabled(false);
        
        setDisplayFps(false);
        setDisplayStatView(false);
        
        try {
            client = Network.connectToServer(ServerMain.APP_HOST_ADDRESS, ServerMain.APP_PORT_NUMBER);
            ServerMain.registerMessageClasses();
            createListeners();
            client.start();
            while(!client.isConnected()) System.out.println("Connecting to server...");
            System.out.println("Connected to server!");
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
        }
        
        gameAppState = new GameAppState(settings);
        stateManager.attach(gameAppState);
        gameAppState.setEnabled(true);
    }
    
    private void createListeners() {
        client.addMessageListener(new ClientListener(), PlayerNodeState.class);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }
    
    public void sendNewPlayerMessage() {
        NewPlayerMessage message = new NewPlayerMessage(settings.getWidth(), settings.getHeight());
        client.send(message);
    }
    
    public void sendUserKeyInputMessage(String command, boolean isPressed) {
        UserKeyInputMessage message = new UserKeyInputMessage(command, isPressed);
        client.send(message);
    }
    
    public int getID() { return client.getId(); }
    
    private class ClientListener implements MessageListener<Client> {
        
        @Override
        public void messageReceived(Client source, Message message) {
            if (gameAppState == null || gameAppState.readyForUpdates == false) return;
            if (message instanceof PlayerNodeState) {
                PlayerNodeState pns = (PlayerNodeState) message;
                gameAppState.updatePlayerNode(pns);
            }
        }
    }
    
}

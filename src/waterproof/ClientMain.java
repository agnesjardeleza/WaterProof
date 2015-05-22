package waterproof;

import com.jme3.app.SimpleApplication;

import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
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
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        
        BloomFilter bloom = new BloomFilter();
        bloom.setBloomIntensity(2.37f);
        bloom.setExposurePower(2);
        bloom.setExposureCutOff(0f);
        bloom.setBlurScale(1.5f);
        fpp.addFilter(bloom);
        
        guiViewPort.addProcessor(fpp);
        guiViewPort.setClearColor(true);
    }
    
    private void createListeners() {
        client.addMessageListener(new ClientListener(), PlayerNodeState.class);
        client.addMessageListener(new ClientListener(), RainNodeState.class);
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
            if (message instanceof RainNodeState) {
                RainNodeState pns = (RainNodeState) message;
                gameAppState.updateRainNode(pns);
            }
        }
    }
    
}

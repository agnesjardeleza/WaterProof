package waterproof;

import com.jme3.app.SimpleApplication;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

/**
 * test
 * @author jjlendaya
 */
public class ClientMain extends SimpleApplication {

    private NetworkClient myClient;
    
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
        
        myClient = new NetworkClient();
        GameAppState gameAppState = new GameAppState(settings);
        gameAppState.setEnabled(true);
        stateManager.attach(gameAppState);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

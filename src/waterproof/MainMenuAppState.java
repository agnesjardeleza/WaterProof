/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;

/**
 *
 * @author Jeynald
 */
public class MainMenuAppState extends AbstractAppState {
    
    private SimpleApplication app;
    private InputManager inputManager;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private Node rootNode;
    private Node guiNode;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        
        this.inputManager = this.app.getInputManager();
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = stateManager;
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}

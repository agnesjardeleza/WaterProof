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
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

/**
 *
 * @author Jeynald
 */
public class MainMenuAppState extends AbstractAppState implements ActionListener{
    
    private SimpleApplication app;
    private InputManager inputManager;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private Node rootNode;
    private Node guiNode;
    private AppSettings settings;    
    
    private Node menuNode;
    
    public MainMenuAppState(AppSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        
        this.inputManager = this.app.getInputManager();
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = stateManager;
        
        menuNode = new Node("MainMenu2");
        addBackground(menuNode);
        guiNode.attachChild(menuNode);
        
        mapInputs();
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        inputManager.removeListener(this);
        guiNode.detachChild(menuNode);
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {        
        ((ClientMain)app).triggerGameState();
    }
    public void waitForPlayer(){
        
    }
    private void addBackground(Node node) {
        
        //load Picture
        String name = node.getName();
        Picture pic = new Picture(name);
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/"+name+".png");
        pic.setTexture(assetManager, tex, true);
        
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width);
        pic.setHeight(height);
        //pic.move(-width/2f, -height/2f, 0);
        
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);
        
        node.attachChild(pic);
    
    }
    
     private void mapInputs() {
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_ENTER, UserKeyInputMessage.KEY_TRIGGER_INPUT_ENTER);
        
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_ENTER);
    }
}

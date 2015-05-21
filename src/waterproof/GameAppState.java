/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.app.Application;
import com.jme3.app.SettingsDialog;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
public class GameAppState extends AbstractAppState implements ActionListener {
    
    private KeyTrigger KEY_TRIGGER_INPUT_UP = new KeyTrigger(KeyInput.KEY_UP);
    private KeyTrigger KEY_TRIGGER_INPUT_DOWN = new KeyTrigger(KeyInput.KEY_DOWN);
    private KeyTrigger KEY_TRIGGER_INPUT_LEFT = new KeyTrigger(KeyInput.KEY_LEFT);
    private KeyTrigger KEY_TRIGGER_INPUT_RIGHT = new KeyTrigger(KeyInput.KEY_RIGHT);
    private KeyTrigger KEY_TRIGGER_INPUT_UP_ALT = new KeyTrigger(KeyInput.KEY_W);
    private KeyTrigger KEY_TRIGGER_INPUT_DOWN_ALT = new KeyTrigger(KeyInput.KEY_S);
    private KeyTrigger KEY_TRIGGER_INPUT_LEFT_STRAFE = new KeyTrigger(KeyInput.KEY_A);
    private KeyTrigger KEY_TRIGGER_INPUT_RIGHT_STRAFE = new KeyTrigger(KeyInput.KEY_D);
    
    private final static String KEY_INPUT_UP = "up";
    private final static String KEY_INPUT_LEFT = "left";
    private static final String KEY_INPUT_DOWN = "down";
    private static final String KEY_INPUT_RIGHT = "right";
    private static final String KEY_INPUT_LEFT_STRAFE = "left_strafe";
    private static final String KEY_INPUT_RIGHT_STRAFE = "right_strafe";
    
    private SimpleApplication app;
    private Node rootNode;
    private Node guiNode;
    private InputManager inputManager;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private AppSettings settings;
    
    private Spatial player;
    
    public GameAppState(AppSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = (SimpleApplication) app;
        this.rootNode = this.app.getRootNode();
        this.guiNode = this.app.getGuiNode();
        this.inputManager = this.app.getInputManager();
        this.stateManager = stateManager;
        this.assetManager = this.app.getAssetManager();
        player = null;
        
        initializePlayer();
        
        mapInputs();
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
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (player != null & (Boolean) player.getUserData(PlayerControl.ALIVE)) {
            if (name.equals(KEY_INPUT_UP)) {
                 player.getControl(PlayerControl.class).setMovement(PlayerControl.UP, false, isPressed);
            } else if (name.equals(KEY_INPUT_LEFT)) {
                player.getControl(PlayerControl.class).setMovement(PlayerControl.LEFT, false, isPressed);
            } else if (name.equals(KEY_INPUT_DOWN)) {
                player.getControl(PlayerControl.class).setMovement(PlayerControl.DOWN, false, isPressed);
            } else if (name.equals(KEY_INPUT_RIGHT)) {
                player.getControl(PlayerControl.class).setMovement(PlayerControl.RIGHT, false, isPressed);
            } else if (name.equals(KEY_INPUT_LEFT_STRAFE)) {
                player.getControl(PlayerControl.class).setMovement(PlayerControl.LEFT, true, isPressed);
            } else if (name.equals(KEY_INPUT_RIGHT_STRAFE)) {
                player.getControl(PlayerControl.class).setMovement(PlayerControl.RIGHT, true, isPressed);
            }
        }
    }
    
    private void initializePlayer() {
        player = getSpatial("Player");
        player.move(settings.getWidth()/2,settings.getHeight()/2,0);
        player.addControl(new PlayerControl());
        player.getControl(PlayerControl.class).initializeData();
        this.guiNode.attachChild(player);
    }
    
    private void mapInputs() {
        inputManager.addMapping(KEY_INPUT_UP, KEY_TRIGGER_INPUT_UP);
        inputManager.addMapping(KEY_INPUT_DOWN, KEY_TRIGGER_INPUT_DOWN);
        inputManager.addMapping(KEY_INPUT_LEFT, KEY_TRIGGER_INPUT_LEFT);
        inputManager.addMapping(KEY_INPUT_RIGHT, KEY_TRIGGER_INPUT_RIGHT);
        inputManager.addMapping(KEY_INPUT_UP, KEY_TRIGGER_INPUT_UP_ALT);
        inputManager.addMapping(KEY_INPUT_DOWN, KEY_TRIGGER_INPUT_DOWN_ALT);
        inputManager.addMapping(KEY_INPUT_LEFT_STRAFE, KEY_TRIGGER_INPUT_LEFT_STRAFE);
        inputManager.addMapping(KEY_INPUT_RIGHT_STRAFE, KEY_TRIGGER_INPUT_RIGHT_STRAFE);
        
        inputManager.addListener(this, KEY_INPUT_UP);
        inputManager.addListener(this, KEY_INPUT_DOWN);
        inputManager.addListener(this, KEY_INPUT_LEFT);
        inputManager.addListener(this, KEY_INPUT_RIGHT);
        inputManager.addListener(this, KEY_INPUT_LEFT_STRAFE);
        inputManager.addListener(this, KEY_INPUT_RIGHT_STRAFE);
        
    }
    
    private Spatial getSpatial(String name) {
        Node node = new Node(name);
        
        //load Picture
        Picture pic = new Picture(name);
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/"+name+".png");
        pic.setTexture(assetManager, tex, true);
        
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width);
        pic.setHeight(height);
        pic.move(-width/2f, -height/2f, 0);
        
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);
        
        node.setUserData("radius", width/2);
        
        node.attachChild(pic);
        return node;
    }

}

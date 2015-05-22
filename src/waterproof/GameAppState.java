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
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.sun.org.apache.bcel.internal.generic.LLOAD;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *
 * @author Jeynald
 */
public class GameAppState extends AbstractAppState implements ActionListener {
    
    private final Object rainLock = new Object();
    private final Object playerLock = new Object();
    
    //Fields for application management
    private SimpleApplication app;
    private InputManager inputManager;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private AppSettings settings;
    
    //Fields for Nodes
    private Node rootNode;
    private Node guiNode;
    private Node playerNode;
    private Node playerNodeForUpdate;
    private Node rainNodeForUpdate;
    private Node rainNode;
    
    public ScheduledThreadPoolExecutor executor;
    
    public boolean readyForUpdates = false;
    
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
        
        playerNode = new Node();
        guiNode.attachChild(playerNode);
        rainNode = new Node();
        guiNode.attachChild(rainNode);
        readyForUpdates = true;
        ((ClientMain)app).sendNewPlayerMessage();
        
        mapInputs();
        
        executor = new ScheduledThreadPoolExecutor(4);
        
    }
    
    @Override
    public void update(float tpf) {
        updatePlayerNodeForDisplay();
        updateRainNodeForDisplay();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        executor.shutdown();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        ((ClientMain)app).sendUserKeyInputMessage(name, isPressed);
    }
    
    public void updatePlayerNode(PlayerNodeState state) {
        synchronized (playerLock) {
            if (playerNodeForUpdate == null) playerNodeForUpdate = new Node();
            if (state.shouldNodeReset()) {
                playerNodeForUpdate.detachAllChildren();
                System.out.println("Updating...");
                for (int i = 0; i < state.getPlayerNum(); i++) {
                    System.out.println(state.getPos(i).x);
                    playerNodeForUpdate.attachChild(createPlayer(state.getPlayerID(i), state.getPos(i), state.getRotation(i), state.getWins(i), state.getLifeStatus(i)));
                }
            } else {
                for (int i = 0; i < state.getPlayerNum(); i++) {
                    updatePlayer(i, state.getPlayerID(i), state.getPos(i), state.getRotation(i));
                }
            }
        }
    }
    public void updateRainNode(RainNodeState state) {
        synchronized(rainLock) {
            if (rainNodeForUpdate == null) rainNodeForUpdate = new Node();
            if (state.shouldNodeReset()) {
                rainNodeForUpdate.detachAllChildren();
                System.out.println("Updating...");
                for (int i = 0; i < state.getRainNum(); i++) {
                    System.out.println(state.getPos(i).x);
                    rainNodeForUpdate.attachChild(createRain(state.getPos(i), state.getVector(i)));
                }
            } else {
                for (int i = 0; i < state.getRainNum(); i++) {
                    updateRain(i, state.getPos(i), state.getVector(i));
                }
            }
        }
    }
    public void updateRainNodeForDisplay() {
        synchronized(rainLock) {
            if (rainNodeForUpdate != null) {
                //rainNode.detachAllChildren();
                for (int i = 0; i < rainNodeForUpdate.getQuantity(); i++) {
                    if (i < rainNode.getQuantity()) rainNode.detachChildAt(i);
                    rainNode.attachChildAt(rainNodeForUpdate.getChild(i).clone(), i);
                }
                int excess = rainNode.getQuantity() - rainNodeForUpdate.getQuantity();
                for (int i = 0; i < excess; i++) {
                    rainNode.detachChildAt(rainNode.getQuantity() - 1);
                }
            }
        }
    }
    
    public void updatePlayerNodeForDisplay() {
        synchronized (playerLock) {
            if (playerNodeForUpdate != null) {
                for (int i = 0; i < playerNodeForUpdate.getQuantity(); i++) {
                    if (i < playerNode.getQuantity()) playerNode.detachChildAt(i);
                    playerNode.attachChildAt(playerNodeForUpdate.getChild(i).clone(), i);
                }
                int excess = playerNode.getQuantity() - playerNodeForUpdate.getQuantity();
                for (int i = 0; i < excess; i++) {
                    playerNode.detachChildAt(playerNode.getQuantity() - 1);
                }
            }
        }
    }
    
    public Spatial createPlayer(int playerID, Vector3f position, float rotation, int wins, boolean lifeStatus) {
        Spatial newPlayer = getSpatial("Player1");
        newPlayer.move(position);
        newPlayer.rotate(0, 0, rotation);
        return newPlayer;
    }
    
    public Spatial createRain (Vector3f position, Vector3f velocity) {
        Spatial newRain = getSpatial("Rain");
        newRain.rotateUpTo(velocity.normalize());
        newRain.rotate(0,0,FastMath.PI/2f);
        newRain.move(position);
        return newRain;
    }
    
    public void updatePlayer(int i, int playerID, Vector3f position, float rotation) {
        Spatial player = playerNodeForUpdate.getChild(i);
        //player.getControl(PlayerControl.class).setClientID(playerID);
        //player.getControl(PlayerControl.class).faceTo(rotation);
        player.move(player.getLocalTranslation().subtract(position));
        player.rotate(0, 0, rotation);
    }
    public void updateRain(int i, Vector3f position, Vector3f velocity) {
        Spatial rain = rainNode.getChild(i);
        //player.getControl(PlayerControl.class).setClientID(playerID);
        //player.getControl(PlayerControl.class).faceTo(rotation);
        rain.move(rain.getLocalTranslation().subtract(position));
        rain.rotateUpTo(velocity.normalize());
        rain.rotate(0,0,FastMath.PI/2f);
    }
    
    private void mapInputs() {
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_UP, UserKeyInputMessage.KEY_TRIGGER_INPUT_UP);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_DOWN, UserKeyInputMessage.KEY_TRIGGER_INPUT_DOWN);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_LEFT, UserKeyInputMessage.KEY_TRIGGER_INPUT_LEFT);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_RIGHT, UserKeyInputMessage.KEY_TRIGGER_INPUT_RIGHT);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_UP_STRAFE, UserKeyInputMessage.KEY_TRIGGER_INPUT_UP_STRAFE);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_DOWN_STRAFE, UserKeyInputMessage.KEY_TRIGGER_INPUT_DOWN_STRAFE);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_LEFT_STRAFE, UserKeyInputMessage.KEY_TRIGGER_INPUT_LEFT_STRAFE);
        inputManager.addMapping(UserKeyInputMessage.KEY_INPUT_RIGHT_STRAFE, UserKeyInputMessage.KEY_TRIGGER_INPUT_RIGHT_STRAFE);
        
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_UP);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_DOWN);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_LEFT);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_RIGHT);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_UP_STRAFE);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_DOWN_STRAFE);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_LEFT_STRAFE);
        inputManager.addListener(this, UserKeyInputMessage.KEY_INPUT_RIGHT_STRAFE);
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

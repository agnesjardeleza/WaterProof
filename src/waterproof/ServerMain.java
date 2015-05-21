/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Jeynald
 */
public class ServerMain extends SimpleApplication {
    
    public static final int APP_PORT_NUMBER = 8888;
    public static final String APP_HOST_ADDRESS = "127.0.0.1";
    
    private Node playerNode;
    public Server server;
    
    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start(JmeContext.Type.Headless);
    }
    
    @Override
    public void simpleInitApp() {
        playerNode = new Node();
        guiNode.attachChild(playerNode);
        
        try {
            server = Network.createServer(APP_PORT_NUMBER);
            registerMessageClasses();
            createMessageListeners();
            server.start();
            if (server.isRunning()) System.out.println("Server is online.");
        } catch (IOException e) {
            System.out.println("Could not start server.");
        }
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        broadcastPlayerNodeState(playerNode, true);
    }
    
    
    @Override
    public void destroy() {
        server.close();
    }
    
    
    public void broadcastPlayerNodeState(Node playerNode, boolean recreatePlayerNode) {
        PlayerNodeState message = new PlayerNodeState(playerNode, recreatePlayerNode);
        server.broadcast(message);
    }
    
    public void createNewPlayer(int clientID, int screenWidth, int screenHeight) {
        System.out.println("Called by " + clientID);
        Spatial newPlayer = getSpatial("Player");
        newPlayer.addControl(new PlayerControl());
        newPlayer.getControl(PlayerControl.class).initializeData(clientID);
        newPlayer.move(screenWidth/2 + (new Random()).nextInt(300), screenHeight/2, 0);
        this.playerNode.attachChild(newPlayer);
        System.out.println(playerNode.getQuantity());
        //broadcastPlayerNodeState(playerNode, true);
    }
    
    public static void registerMessageClasses() {
        Serializer.registerClass(NewPlayerMessage.class);
        Serializer.registerClass(UserKeyInputMessage.class);
        Serializer.registerClass(PlayerNodeState.class);
    }
    
    private void createMessageListeners() {
        server.addMessageListener(new ServerListener(), UserKeyInputMessage.class);
        server.addMessageListener(new ServerListener(), NewPlayerMessage.class);
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
    
    private class ServerListener implements MessageListener<HostedConnection> {
        
        @Override
        public void messageReceived(HostedConnection source, Message message) {
            if (message instanceof NewPlayerMessage) {
                NewPlayerMessage npm = (NewPlayerMessage) message;
                ServerMain.this.createNewPlayer(source.getId(), npm.screenWidth, npm.screenHeight);
            } else if (message instanceof UserKeyInputMessage) {
                UserKeyInputMessage inputMessage = (UserKeyInputMessage) message;
                String command = inputMessage.getUserCommand();
                Spatial player = null;
                for (int i = 0; i < playerNode.getQuantity(); i++) {
                    if (playerNode.getChild(i).getControl(PlayerControl.class).getClientID() == source.getId()) {
                        player = playerNode.getChild(i);
                        System.out.println("Player ID: " + i);
                        break;
                    }
                }
                PlayerControl playerControl = player.getControl(PlayerControl.class);
                if (command.equals(UserKeyInputMessage.KEY_INPUT_UP)) {
                    playerControl.setMovement(PlayerControl.UP, false, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_LEFT)) {
                    playerControl.setMovement(PlayerControl.LEFT, false, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_DOWN)) {
                    playerControl.setMovement(PlayerControl.DOWN, false, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_RIGHT)) {
                    playerControl.setMovement(PlayerControl.RIGHT, false, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_UP_STRAFE)) {
                    playerControl.setMovement(PlayerControl.UP, true, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_LEFT_STRAFE)) {
                    playerControl.setMovement(PlayerControl.LEFT, true, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_DOWN_STRAFE)) {
                    playerControl.setMovement(PlayerControl.DOWN, true, inputMessage.isPressed());
                } else if (command.equals(UserKeyInputMessage.KEY_INPUT_RIGHT_STRAFE)) {
                    playerControl.setMovement(PlayerControl.RIGHT, true, inputMessage.isPressed());
                }
                System.out.println("Key Pressed: " + inputMessage.getUserCommand() + " on Client #" + source.getId());
            }
        }
        
    }
    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
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
    
    public static final int APP_PORT_NUMBER = 8889;
    public static final String APP_HOST_ADDRESS = "127.0.0.1";
    
    private Node playerNode;
     private Node enemyNode;
    public Server server;
    private long enemySpawnCooldown;
    private int screenWidth;
    private int screenHeight;
    private float spawnCenter;
    
    private float enemySpawnChance = 80;
    
    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start(JmeContext.Type.Headless);
    }
    
    @Override
    public void simpleInitApp() {
        playerNode = new Node();        
        playerNode.setUserData("alive", true);
        enemyNode = new Node("enemies");
        guiNode.attachChild(enemyNode);
        guiNode.attachChild(playerNode);
        enemySpawnCooldown = System.currentTimeMillis();
        screenWidth = settings.getWidth();
        screenHeight = settings.getHeight();
        spawnCenter = settings.getWidth()/2;
        
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
       
        if ((Boolean) playerNode.getUserData("alive")) {
            spawnEnemies();
            handleCollisions();
        }
        broadcastPlayerNodeState(playerNode, true);
        broadcastRainNodeState(enemyNode, true);
    }
    
    
    @Override
    public void destroy() {
        server.close();
    }
    
    
    public void broadcastPlayerNodeState(Node playerNode, boolean recreatePlayerNode) {
        PlayerNodeState message = new PlayerNodeState(playerNode, recreatePlayerNode);
        message.setReliable(false);
        server.broadcast(message);
    }
     public void broadcastRainNodeState(Node rainNode, boolean recreateRainNode) {
        RainNodeState message = new RainNodeState(rainNode, recreateRainNode);
        message.setReliable(false);
        server.broadcast(message);
    }
    
    public void createNewPlayer(int clientID, int scrnWidth, int scrnHeight) {
        //System.out.println("Called by " + clientID);
        Spatial newPlayer = getSpatial("Player1");
        newPlayer.addControl(new PlayerControl());
        newPlayer.getControl(PlayerControl.class).initializeData(clientID);
        newPlayer.move(screenWidth/2 + (new Random()).nextInt(300), screenHeight/2, 0);
        this.playerNode.attachChild(newPlayer);
        screenWidth = scrnWidth;
        screenHeight = scrnHeight;
        System.out.println(playerNode.getQuantity());
        //broadcastPlayerNodeState(playerNode, true);
    }
    private void spawnEnemies() {
        
        if(System.currentTimeMillis() - enemySpawnCooldown >= 15 && enemyNode.getQuantity() < 50) {
            enemySpawnCooldown = System.currentTimeMillis();
                if (new Random().nextInt(100) < 100 - enemySpawnChance) {
                    createSeeker();
                }
            if (enemySpawnChance >= 1.1f) {
                enemySpawnChance -= 0.005f;
            }
        }
    }
    
    private void createSeeker() {
        Spatial seeker = getSpatial("Rain");
        seeker.setLocalTranslation(getSpawnPosition());
        seeker.addControl(new SeekerControl(enemyNode,screenWidth,screenHeight));
        seeker.setUserData("active",false);
        enemyNode.attachChild(seeker);
        //sound.spawn();
    }
    private Vector3f getSpawnPosition() {
        Vector3f pos;
        do {
            //int x = new Random().nextInt(2);
            spawnCenter = screenWidth/2 +  (screenWidth)*FastMath.sin((System.currentTimeMillis()%5000));
            int x = 0;
            if (x == 0) {
               pos = new Vector3f(spawnCenter, screenHeight + 10,0);
            } else {
               pos = new Vector3f(screenWidth + 10,200 + new Random().nextInt(screenHeight-200),0); 
            }           
            
        } while (pos.x < -screenWidth/2);
        return pos;
    }
    
    private void handleCollisions() {
        for (int j = 0; j < playerNode.getQuantity(); j++){
            if((Boolean) playerNode.getChild(j).getUserData("alive")){
                for (int i = 0; i < enemyNode.getQuantity(); i++) {
                    if((Boolean) enemyNode.getChild(i).getUserData("active")) {
                        if (checkCollision(playerNode.getChild(j),enemyNode.getChild(i))) {
                            killPlayer(playerNode.getChild(j));
                        }
                    }

                }
             }
        }
    
       /* int i=0;
        while (i < enemyNode.getQuantity()) {
            int j = 0;
            while (j < bulletNode.getQuantity()){
                if (checkCollision(enemyNode.getChild(i),bulletNode.getChild(j))) {
                    if (enemyNode.getChild(i).getName().equals("Seeker")) {
                        hud.addPoints(2);
                    } else if (enemyNode.getChild(i).getName().equals("Wanderer")) {
                        hud.addPoints(1);
                    }
                    particleManager.enemyExplosion(enemyNode.getChild(i).getLocalTranslation());
                    enemyNode.detachChildAt(i);
                    bulletNode.detachChildAt(j);
                    sound.explosion();
                    break;
                }
                j++;
            }
            i++;
        }
        //black hole
        for (i = 0; i < blackHoleNode.getQuantity(); i++) {
            Spatial blackHole = blackHoleNode.getChild(i);
            if ((Boolean) blackHole.getUserData("active")) {
                if (checkCollision(player,blackHole)) {
                    killPlayer();
                }
                //enemies
                int j=0;
                while (j < enemyNode.getQuantity()) {
                    if (checkCollision(enemyNode.getChild(j), blackHole)) {
                        particleManager.enemyExplosion(enemyNode.getChild(j).getLocalTranslation());
                        enemyNode.detachChildAt(j);
                    }
                    j++;
                }
                //bullets
                j=0;
                while (j < bulletNode.getQuantity()) {
                    if (checkCollision(bulletNode.getChild(j),blackHole)) {
                        bulletNode.detachChildAt(j);
                        blackHole.getControl(BlackHoleControl.class).wasShot();
                        if (blackHole.getControl(BlackHoleControl.class).isDead()) {
                            blackHoleNode.detachChild(blackHole);
                            sound.explosion();
                        }
                    }
                    j++;
                }
            }
        }*/
    }
    
    private boolean checkCollision(Spatial a, Spatial b) {
        float distance = a.getLocalTranslation().distance(b.getLocalTranslation());
        float maxDistance = (Float)a.getUserData("radius") + (Float)b.getUserData("radius");
        
        return distance <= maxDistance;
    }
    
    private void killPlayer(Spatial a) {
        a.removeFromParent();
        //playerNode.getControl(PlayerControl.class).reset();
        playerNode.setUserData("alive", false);
        a.setUserData("dieTime",System.currentTimeMillis());
        /*if (!hud.removeLife()) {
            hud.endGame();
            gameOver = true;
        }*/
        enemyNode.detachAllChildren();
        //blackHoleNode.detachAllChildren();
    }
    
    public static void registerMessageClasses() {
        Serializer.registerClass(NewPlayerMessage.class);
        Serializer.registerClass(UserKeyInputMessage.class);
        Serializer.registerClass(PlayerNodeState.class);
        Serializer.registerClass(RainNodeState.class);
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

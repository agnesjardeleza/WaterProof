/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Jeynald
 */
@Serializable
public class PlayerNodeState extends AbstractMessage {
    
    private int[] playerID;
    private float[] xPos;
    private float[] yPos;
    private float[] rotation;
    private boolean[] lifeStatus;
    private int[] wins;
    
    boolean recreatePlayerNode;
    
    public PlayerNodeState() {
        
    }
    
    public PlayerNodeState(Node playerNode, boolean recreatePlayerNode) {
        Spatial player;
        PlayerControl playerControl;
        Vector3f pos;
        
        int playerNum = playerNode.getQuantity();
        playerID = new int[playerNum];
        xPos = new float[playerNum];
        
        yPos = new float[playerNum];
        rotation = new float[playerNum];
        lifeStatus = new boolean[playerNum];
        wins = new int[playerNum];
        System.out.println("//-----");
        for (int i = 0; i < playerNode.getQuantity(); i++) {
            player = playerNode.getChild(i);
            pos = player.getLocalTranslation();
            xPos[i] = pos.x;
            yPos[i] = pos.y;
            System.out.println(pos.x + " , " + pos.y);
            
            playerControl = player.getControl(PlayerControl.class);
            playerID[i] = playerControl.getClientID();
            rotation[i] = playerControl.getRotation();
            lifeStatus[i] = playerControl.getLifeStatus();
            wins[i] = playerControl.getWins();
        }
        
        this.recreatePlayerNode = recreatePlayerNode;
    }
    
    public PlayerNodeState(int[] playerID, float[] xPos, float[] yPos, float[] rotation) {
        this.playerID = playerID;
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
    }
    
    public Vector3f getPos(int i) {
        return new Vector3f(xPos[i], yPos[i], 0);
    }
    
    public float getRotation(int i) {
        return rotation[i];
    }
    
    public int getPlayerID(int i) {
        return playerID[i];
    }
    
    public int getWins(int i) {
        return wins[i];
    }
    
    public boolean getLifeStatus(int i) {
        return lifeStatus[i];
    }
    
    public boolean shouldNodeReset() {
        return recreatePlayerNode;
    }
    
    public int getPlayerNum() { return playerID.length; }
}

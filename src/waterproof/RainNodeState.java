/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC
 */
@Serializable
public class RainNodeState extends AbstractMessage {
    private float[] xPos;
    private float[] yPos;
    private float[] rotation;
    private int rainNum;
    
    boolean recreateRainNode;
    public RainNodeState() {
        
    }
    public RainNodeState (Node rainNode, boolean recreateRainNode){
        Spatial rain;
        SeekerControl rainControl;
        Vector3f pos;
        
        rainNum = rainNode.getQuantity();
        xPos = new float[rainNum];
        
        yPos = new float[rainNum];
        rotation = new float[rainNum];

        for (int i = 0; i < rainNode.getQuantity(); i++) {
            rain = rainNode.getChild(i);
            pos = rain.getLocalTranslation();
            xPos[i] = pos.x;
            yPos[i] = pos.y;
            
            rainControl = rain.getControl(SeekerControl.class);
            rotation[i] = getAngleFromVector(rainControl.getVector());
        }
        
        this.recreateRainNode = recreateRainNode;
    }
    public RainNodeState(float[] xPos, float[] yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    public Vector3f getPos(int i) {
        return new Vector3f(xPos[i], yPos[i], 0);
    }
    
    public boolean shouldNodeReset() {
        return recreateRainNode;
    }
    
    public int getRainNum() { return rainNum; }
    
    public float getRotation(int i) {
        return rotation[i];
    }
    
    private float getAngleFromVector(Vector3f velocity) {
        return new Vector2f(velocity.x, velocity.y).getAngle();
    }
}

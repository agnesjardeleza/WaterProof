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
 * @author PC
 */
@Serializable
public class RainNodeState extends AbstractMessage {
    private float[] xPos;
    private float[] yPos;
    private Vector3f[] velocity;
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
        velocity = new Vector3f[rainNum];
        //System.out.println("//-----");
        for (int i = 0; i < rainNode.getQuantity(); i++) {
            rain = rainNode.getChild(i);
            pos = rain.getLocalTranslation();
            xPos[i] = pos.x;
            yPos[i] = pos.y;
            
            //System.out.println(pos.x + " , " + pos.y);
            
            rainControl = rain.getControl(SeekerControl.class);
            velocity[i] = rainControl.getVector();
        }
        
        this.recreateRainNode = recreateRainNode;
    }
    public RainNodeState(float[] xPos, float[] yPos, Vector3f[] velocity) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.velocity = velocity;
    }
    
    public Vector3f getPos(int i) {
        return new Vector3f(xPos[i], yPos[i], 0);
    }
    
    public Vector3f getVector(int i) {
        return velocity[i];
    }
    public boolean shouldNodeReset() {
        return recreateRainNode;
    }
    
    public int getRainNum() { return rainNum; }
}

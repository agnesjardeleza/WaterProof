/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Jeynald
 */
@Serializable
public class PositionMessage extends AbstractMessage {
    
    public static final String UNIT_TYPE_PLAYER = "player";
    public static final String UNIT_TYPE_RAINDROP = "raindrop";
    
    private String unitType;
    private Vector3f position;
    private int unitID;
    
    public PositionMessage() {
        
    }
    
    public PositionMessage(String unitType, Vector3f position, int unitID) {
        this.unitType = unitType;
        this.position = position;
    }

    /**
     * @return the unitType
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }
}

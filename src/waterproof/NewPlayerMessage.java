/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Jeynald
 */
@Serializable
public class NewPlayerMessage extends AbstractMessage {
    
    int screenWidth;
    int screenHeight;
    
    public NewPlayerMessage() {
        
    }
    
    public NewPlayerMessage(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    
}

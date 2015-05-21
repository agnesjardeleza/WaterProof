/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.scene.Node;

/**
 *
 * @author Jeynald
 */
public class Broadcaster {
    
    ServerMain serverMain;
    
    public Broadcaster(ServerMain serverMain) {
        this.serverMain = serverMain;
    }
    
    public void broadcastPlayerNodeState(Node playerNode, boolean recreatePlayerNode) {
        PlayerNodeState message = new PlayerNodeState(playerNode, recreatePlayerNode);
        serverMain.server.broadcast(message);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author PC
 */
@Serializable
public class ScoreMessage extends AbstractMessage {
    
    int score1;
    int score2;
    boolean gameOver = false;
    
    public ScoreMessage() {
        
    }
    
    public ScoreMessage(int score1, int score2) {
        this.score1 = score1;
        this.score2 = score2;
    }
    public ScoreMessage(boolean gameOver) {
        this.gameOver = gameOver;
    }
}


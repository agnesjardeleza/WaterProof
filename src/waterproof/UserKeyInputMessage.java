/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Jeynald
 */
@Serializable
public class UserKeyInputMessage extends AbstractMessage {
    
    public static final KeyTrigger KEY_TRIGGER_INPUT_UP = new KeyTrigger(KeyInput.KEY_UP);
    public static final KeyTrigger KEY_TRIGGER_INPUT_DOWN = new KeyTrigger(KeyInput.KEY_DOWN);
    public static final KeyTrigger KEY_TRIGGER_INPUT_LEFT = new KeyTrigger(KeyInput.KEY_LEFT);
    public static final KeyTrigger KEY_TRIGGER_INPUT_RIGHT = new KeyTrigger(KeyInput.KEY_RIGHT);
    public static final KeyTrigger KEY_TRIGGER_INPUT_UP_STRAFE = new KeyTrigger(KeyInput.KEY_W);
    public static final KeyTrigger KEY_TRIGGER_INPUT_DOWN_STRAFE = new KeyTrigger(KeyInput.KEY_S);
    public static final KeyTrigger KEY_TRIGGER_INPUT_LEFT_STRAFE = new KeyTrigger(KeyInput.KEY_A);
    public static final KeyTrigger KEY_TRIGGER_INPUT_RIGHT_STRAFE = new KeyTrigger(KeyInput.KEY_D);
    public static final KeyTrigger KEY_TRIGGER_INPUT_ENTER = new KeyTrigger(KeyInput.KEY_RETURN);
    
    public static final String KEY_INPUT_UP = "up";
    public static final String KEY_INPUT_LEFT = "left";
    public static final String KEY_INPUT_DOWN = "down";
    public static final String KEY_INPUT_RIGHT = "right";
    public static final String KEY_INPUT_UP_STRAFE = "up_strafe";
    public static final String KEY_INPUT_LEFT_STRAFE = "left_strafe";
    public static final String KEY_INPUT_DOWN_STRAFE = "down_strafe";
    public static final String KEY_INPUT_RIGHT_STRAFE = "right_strafe";
    public static final String KEY_INPUT_ENTER = "enter";
    
    private String userCommand;
    private boolean isPressed;
    
    public UserKeyInputMessage() {}
    
    public UserKeyInputMessage(String userCommand, boolean isPressed) {
        this.userCommand = userCommand;
        this.isPressed = isPressed;
    }

    /**
     * @return the userCommand
     */
    public String getUserCommand() {
        return userCommand;
    }

    /**
     * @param userCommand the userCommand to set
     */
    public void setUserCommand(String userCommand) {
        this.userCommand = userCommand;
    }

    /**
     * @return the isPressed
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * @param isPressed the isPressed to set
     */
    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }
    
}

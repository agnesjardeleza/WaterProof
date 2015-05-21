/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Jeynald
 */
public class PlayerControl extends AbstractControl {

    public static final String WINS = "wins";
    public static final String ACTIVE_POWER_UP = "currentpowerup";
    public static final String ALIVE = "alive";
    public static final String RADIUS = "radius";
    
    public static final String ACTIVE_POWER_UP_NONE = "none";
    
    //ATTRIBUTE CONSTANTS
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int STRAFE = 4;
    
    //STATUS VARIABLES
    private boolean moveArray[];
    
    //ATTRIBUTE VARIABLES
    private static final float speed = 800f;
    
    public int getWins() { return spatial.getUserData(WINS); }
    public String getPowerUp() { return (String) spatial.getUserData(ACTIVE_POWER_UP); }
    public boolean getLifeStatus() { return (Boolean) spatial.getUserData(ALIVE); }
    public float getRadius() { return spatial.getUserData(RADIUS); }
    
    public void setWins(int wins) { spatial.setUserData(WINS, wins); }
    public void setPowerUp(String powerUp) { spatial.setUserData(ACTIVE_POWER_UP, powerUp); }
    public void setLifeStatus(boolean lifeStatus) { spatial.setUserData(ALIVE, lifeStatus); }
    public void setRadius(float radius) { spatial.setUserData(RADIUS, radius); }
    
    public void setMovement(int moveDirection, boolean strafe, boolean motion) {
        moveArray[moveDirection] = motion;
        moveArray[STRAFE] = strafe;
    }
    
    public void initializeData() {
        setWins(0);
        setPowerUp(ACTIVE_POWER_UP_NONE);
        setLifeStatus(true);
        
        moveArray = new boolean[5];
        for (int i = 0; i < moveArray.length; i++) moveArray[i] = false;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        move(tpf);
    }
    
    private void move(float tpf) {
        if (getLifeStatus() == false) return;
        
        if (moveArray[UP]) {
            spatial.move(0, speed*tpf, 0);
        } else if (moveArray[RIGHT]) {
            spatial.move(speed*tpf, 0, 0);
        } else if (moveArray[LEFT]) {
            spatial.move(-speed*tpf, 0, 0);
        } else if (moveArray[DOWN]) {
            spatial.move(0, -speed*tpf, 0);
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        PlayerControl control = new PlayerControl();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
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
    
    public static final String ACTIVE_POWER_UP_NONE = "none";
    
    public int getWins() { return spatial.getUserData(WINS); }
    public String getPowerUp() { return spatial.getUserData(ACTIVE_POWER_UP); }
    public String getLifeStatus() { return spatial.getUserData(ALIVE); }
    
    public void setWins(int wins) { spatial.setUserData(WINS, wins); }
    public void setPowerUp(String powerUp) { spatial.setUserData(ACTIVE_POWER_UP, powerUp); }
    public void setLifeStatus(boolean lifeStatus) { spatial.setUserData(ALIVE, lifeStatus); }
    
    public void initializeData() {
        setWins(0);
        setPowerUp(ACTIVE_POWER_UP_NONE);
        setLifeStatus(true);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
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

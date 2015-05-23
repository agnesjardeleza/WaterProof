/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.ui.Picture;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author PC
 */
public class SeekerControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    private Spatial player;
    private Vector3f velocity;
    private long spawnTime;
    private int screenWidth, screenHeight;    
    public static final String RADIUS = "radius";
    private float addVelocity;
    private Vector3f playerDirection;
    
    public SeekerControl(Spatial player, int screenWidth, int screenHeight, float addVelocity) {
        this.player = player;
        velocity = new Vector3f(0,0,0);
        spawnTime = System.currentTimeMillis();  
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
        if (spawnTime % 45000 < 15000) {
            playerDirection = new Vector3f(-1, -1, 0);
        } else if (spawnTime % 45000 < 30000) {
            playerDirection = new Vector3f(0, -1, 0);
        } else {
            playerDirection = new Vector3f(1, -1, 0);
        }
        
        playerDirection.normalizeLocal().multLocal(500f*addVelocity + new Random().nextFloat()*1400);
        
    }
    @Override
    protected void controlUpdate(float tpf) {
        if ((Boolean) spatial.getUserData("active")) {
            velocity.addLocal(playerDirection);
            velocity.multLocal(0.8f);
            spatial.move(velocity.mult(tpf*0.1f));
        } else {
            long dif = System.currentTimeMillis() - spawnTime;
            if (dif >= 1000f) {
                spatial.setUserData("active",true);
            }
        }
        Vector3f loc = spatial.getLocalTranslation();
        if (loc.y < 0) {
            spatial.removeFromParent();
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        SeekerControl control = new SeekerControl(player, screenWidth, screenHeight, addVelocity);
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
    public Vector3f getVector() { return velocity; }
    public float getRadius() { return spatial.getUserData(RADIUS); }
}

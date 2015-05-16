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
    
    public SeekerControl(Spatial player, int screenWidth, int screenHeight) {
        this.player = player;
        velocity = new Vector3f(0,0,0);
        spawnTime = System.currentTimeMillis();  
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        if ((Boolean) spatial.getUserData("active")) {
            Vector3f playerDirection = new Vector3f(-screenWidth/2f,-screenHeight/2f,0);
            playerDirection.normalizeLocal();
            playerDirection.multLocal(1500f);
            velocity.addLocal(playerDirection);
            velocity.multLocal(0.8f);
            spatial.move(velocity.mult(tpf*0.1f));
            
            if (velocity != Vector3f.ZERO) {
                spatial.rotateUpTo(velocity.normalize());
                spatial.rotate(0,0,FastMath.PI/2f);
            }
        } else {
            long dif = System.currentTimeMillis() - spawnTime;
            if (dif >= 1000f) {
                spatial.setUserData("active",true);
            }
            
            ColorRGBA color = new ColorRGBA(1,1,1,dif/1000f);
            Node spatialNode = (Node) spatial;
            Picture pic = (Picture) spatialNode.getChild("Rain");
            pic.getMaterial().setColor("Color", color);
        }
        Vector3f loc = spatial.getLocalTranslation();
        if (loc.x > screenWidth || 
            loc.y > screenHeight ||
            loc.x < 0 ||
            loc.y < 0) {
            spatial.removeFromParent();
        }
    }
    
    public void applyGravity(Vector3f gravity) {
        velocity.addLocal(gravity);
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        SeekerControl control = new SeekerControl(player,screenWidth,screenHeight);
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
    public Vector3f getVector() { return velocity; }
}

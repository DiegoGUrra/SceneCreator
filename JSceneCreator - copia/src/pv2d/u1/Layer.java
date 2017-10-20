/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv2d.u1;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class Layer 
{
    private ArrayList<SceneSprite> sprites;
    private boolean visible;
    public Layer()
    {
        this.sprites = new ArrayList<SceneSprite>();
        this.visible = true;
    }
    public boolean add(BufferedImage e)
    {
        sprites.add( new SceneSprite(e, 0, 0));
        return true;
    }

    public ArrayList<SceneSprite> getSprites() 
    {
        return sprites;
    }

    public void setSprites(ArrayList<SceneSprite> sprites) 
    {
        this.sprites = sprites;
    }

    public boolean isVisible() 
    {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}

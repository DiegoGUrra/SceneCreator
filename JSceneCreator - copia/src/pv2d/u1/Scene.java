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
public class Scene 
{
    
    private ArrayList<Layer> layers;
    private int width;
    private int height;
    
    public Scene(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<Layer>();
    }
    
    public boolean add(BufferedImage e,Layer layer)
    {
        layer.add(e);
        layers.add(layer);
        
        
        return true;
    }
    
    public boolean add(Layer layer)
    {
        return layers.add(layer);
    }
    public boolean remove(Layer layer)
    {
        return layers.remove(layer);
    }
    public Layer remove(int i)
    {
        return this.layers.remove(i);
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
}

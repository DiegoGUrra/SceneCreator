/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv2d.u1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author Pablo Rojas
 */
class JScenePanel extends JPanel implements MouseListener, MouseMotionListener
{
    final static float dash1[] = {5.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    
    //private ArrayList<SceneSprite> sprites;
    private Scene scene;
    private ArrayList<SceneSprite> selectedSprites;
    private Point lastMousePoint;
    private boolean mousePressedOnCanvas; 
    
    private boolean mousePressedOnResizer;
    private SceneSprite.Resizer whichResizer;
    
    private Point selectionRectangleStart;
    private Point selectionRectangleEnd;
    
    public JScenePanel()
    {
        //super.setPreferredSize(new Dimension(500, 400));
        
        //dejar que el ususario defina el tamaño de la escena
        this.scene = new Scene(1000,1000);
        super.setPreferredSize(new Dimension(1000,1000));
        //this.sprites = new ArrayList<>();
        this.selectedSprites = new ArrayList<>();
        this.mousePressedOnCanvas = true;
        this.mousePressedOnCanvas = false;
        super.setBackground(Color.WHITE);
        
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
    }

    public boolean add(BufferedImage e)
    {
        this.scene.add(e, new Layer());
        //sprites.add( new SceneSprite(e, 0, 0));
        repaint();
        return true;
        
    }
    public void add(Scene scene)
    {
        this.scene = scene;  
        super.setPreferredSize(new Dimension(this.scene.getHeight(),this.scene.getWidth()));
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for(int i = 0;i< this.scene.getLayers().size();i++)
        {
            if(this.scene.getLayers().get(i).isVisible())
            {
                for(SceneSprite sprite: this.scene.getLayers().get(i).getSprites())
                {
                    sprite.paint(g);
                }
            }
            
        }
        /*
        for (SceneSprite sprite : sprites)
        {
            sprite.paint(g);
        }
        */
        if( this.mousePressedOnCanvas && this.selectionRectangleStart != null && this.selectionRectangleEnd != null )
        {
            Graphics2D g2d = (Graphics2D)g;
            Stroke previousStroke = g2d.getStroke();
            Color previousColor = g2d.getColor();
            
            g2d.setColor(Color.GRAY);
            g2d.setStroke(JScenePanel.dashed);
            g2d.draw(this.getFixedRectangle());
            g2d.setColor(previousColor);
            g2d.setStroke(previousStroke);
        }
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        this.mousePressedOnCanvas = true;
        this.lastMousePoint = e.getPoint();
        if( !e.isControlDown() )
        {
            boolean pressedSelectedSprite = false;
            for (int i = this.selectedSprites.size()-1; i >= 0; i--)
            {
                SceneSprite sprite = this.selectedSprites.get(i);
                if( sprite.contains(e.getPoint()) )
                {
                    pressedSelectedSprite = true;
                }
                else if( sprite.resizerContains(e.getPoint()) )
                {
                    pressedSelectedSprite = true;
                    this.mousePressedOnResizer = true;
                }
            }
            
            if( !pressedSelectedSprite )
            {
                for (SceneSprite sprite : this.selectedSprites)
                {
                    sprite.setSelected(false);
                }
                this.selectedSprites.clear();
            }
        }
        
        SceneSprite pressedSprite = null;
        boolean pressedOnResizer = false;
        for(int i =0;i<this.scene.getLayers().size();i++)
        {
            for (int j = this.scene.getLayers().get(i).getSprites().size()-1; j >= 0; j--)
            {
                SceneSprite sprite = this.scene.getLayers().get(i).getSprites().get(j);
                if( sprite.contains(e.getPoint()) )
                {
                    pressedSprite = sprite;
                    pressedOnResizer = false;
                    break;
                }
                if( sprite.resizerContains(e.getPoint()) )
                {
                    pressedSprite = sprite;
                    pressedOnResizer = true;
                    break;
                }
            }
        }
        
        
        if( pressedSprite != null )
        {
            this.mousePressedOnCanvas = false;
            pressedSprite.setSelected(true);
            if(!this.selectedSprites.contains(pressedSprite))
            {
                this.selectedSprites.add(pressedSprite);
            }
            
            if( pressedOnResizer )
            {
                this.mousePressedOnResizer = true;
                this.lastMousePoint = e.getPoint();
                this.whichResizer = pressedSprite.whichResizerIsPressed(e.getPoint());
            }
            else
            {
                this.mousePressedOnResizer = false;
            }
            
        }
        else if( this.mousePressedOnCanvas )
        {
            this.selectionRectangleStart = e.getPoint();
        }
        
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if( this.mousePressedOnCanvas && this.selectionRectangleStart != null && this.selectionRectangleEnd != null )
        {
            Rectangle rectangle = this.getFixedRectangle();
            for(int i =0;i<this.scene.getLayers().size();i++)
            {
                for (SceneSprite sprite : this.scene.getLayers().get(i).getSprites())
                {
                    if( sprite.intersects(rectangle) )
                    {
                        if( !this.selectedSprites.contains(sprite) )
                        {
                            sprite.setSelected(true);
                            this.selectedSprites.add(sprite);
                        }
                    }
                }
            }
            
        }
        
        this.lastMousePoint = null;
        this.selectionRectangleStart = null;
        this.selectionRectangleEnd = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        boolean repaintNeeded = false;
        
        if( this.lastMousePoint != null && !this.mousePressedOnCanvas && !this.mousePressedOnResizer )
        {
            int dx = (int)(e.getX() - this.lastMousePoint.getX());
            int dy = (int)(e.getY() - this.lastMousePoint.getY());
            for (SceneSprite sprite : this.selectedSprites)
            {
                sprite.setX( sprite.getX() + dx );
                sprite.setY( sprite.getY() + dy );
            }
            repaintNeeded = true;
        }
        
        if( this.mousePressedOnResizer )
        {
            int dx = (int)(e.getPoint().getX() - this.lastMousePoint.getX());
            int dy = (int)(e.getPoint().getY() - this.lastMousePoint.getY());
            
            for (SceneSprite sprite : this.selectedSprites)
            {
                sprite.updateBounds(this.whichResizer, dx, dy);
                repaintNeeded = true;
            }
            
            this.lastMousePoint = e.getPoint();
        }
        
        if( this.mousePressedOnCanvas )
        {
            if( this.selectionRectangleStart == null )
            {
                this.selectionRectangleStart = e.getPoint();
            }
            else
            {
                this.selectionRectangleEnd = e.getPoint();
                repaintNeeded = true;
                       
            }
        }
        
        this.lastMousePoint = e.getPoint();
        
        if( repaintNeeded )
        {
            repaint();
        }
        
        
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        Cursor cursor = null;
        for(int i=0;i<this.scene.getLayers().size();i++)
        {
            for (SceneSprite sprite : this.scene.getLayers().get(i).getSprites())
            {
                Cursor spriteCursor = sprite.getCursor(e.getPoint());
                if( spriteCursor != null )
                {
                    cursor = spriteCursor;
                }
            }
        }
        
        
        
        if(cursor == null)
        {
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        }
        this.setCursor(cursor);
    }
    
    
    
    
    private Rectangle getFixedRectangle()
    {
        int x1 = (int)(this.selectionRectangleStart.getX());
        int y1 = (int)(this.selectionRectangleStart.getY());
        int x2 = (int)(this.selectionRectangleEnd.getX());
        int y2 = (int)(this.selectionRectangleEnd.getY());
        
        int x = x1 < x2 ? x1 : x2;
        int y = y1 < y2 ? y1 : y2;
        int w = Math.abs(x2-x1);
        int h = Math.abs(y2-y1);
        
        return new Rectangle(x, y, w, h);
        
        
    }

    public Scene getScene() 
    {
        return scene;
    }
    
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv2d.u1;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Pablo Rojas
 */
class SceneSprite
{
    private static int RESIZER_SIZE = 8;
    
    private BufferedImage image;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean selected;

    public SceneSprite(BufferedImage image, int x, int y, int width, int height)
    {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.selected = false;
    }

    public SceneSprite(BufferedImage image, int x, int y)
    {
        this(image, x, y, image.getWidth(), image.getHeight());
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    
    public boolean contains( Point point )
    {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        return (rectangle.contains(point) );
    }
    
    public boolean resizerContains( Point point )
    {
        if(this.selected)
        {
            if( this.topLeftResizer().contains(point) )
            {
                return true;
            }

            if( this.topRightResizer().contains(point) )
            {
                return true;
            }

            if( this.bottomLeftResizer().contains(point) )
            {
                return true;
            }

            if( this.bottomRightResizer().contains(point) )
            {
                return true;
            }
        }
        
        return false;
    }
    

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.drawImage(this.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
        if( this.selected )
        {
            g2d.setColor(Color.BLUE);
            g2d.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            g2d.setColor(Color.BLACK);
            
            g2d.fill(this.topLeftResizer());
            g2d.fill(this.bottomRightResizer() );
            g2d.fill(this.topRightResizer());
            g2d.fill(this.bottomLeftResizer());
        }
    }

    private Rectangle toRectangle()
    {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
    
    private Rectangle topLeftResizer()
    {
        return new Rectangle( this.getX() - SceneSprite.RESIZER_SIZE, this.getY() - SceneSprite.RESIZER_SIZE, SceneSprite.RESIZER_SIZE, SceneSprite.RESIZER_SIZE );
    }
    
    private Rectangle bottomRightResizer()
    {
        return new Rectangle( this.getX() + this.getWidth(), this.getY() + this.getHeight(), SceneSprite.RESIZER_SIZE, SceneSprite.RESIZER_SIZE );
    }
    
    private Rectangle topRightResizer()
    {
        return new Rectangle( this.getX() + this.getWidth(), this.getY() - SceneSprite.RESIZER_SIZE, SceneSprite.RESIZER_SIZE, SceneSprite.RESIZER_SIZE );
    }
    
    private Rectangle bottomLeftResizer()
    {
        return new Rectangle( this.getX() - SceneSprite.RESIZER_SIZE, this.getY() + this.getHeight(), SceneSprite.RESIZER_SIZE, SceneSprite.RESIZER_SIZE );
    }
    
    public Cursor getCursor(Point cursorLocation)
    {
        if( toRectangle().contains(cursorLocation) )
        {
            return new Cursor(Cursor.MOVE_CURSOR);
        }
        
        if(this.selected)
        {
            if( this.topLeftResizer().contains(cursorLocation) )
            {
                return new Cursor(Cursor.NW_RESIZE_CURSOR);
            }
            
            if( this.topRightResizer().contains(cursorLocation) )
            {
                return new Cursor(Cursor.NE_RESIZE_CURSOR);
            }
            
            if( this.bottomLeftResizer().contains(cursorLocation) )
            {
                return new Cursor(Cursor.SW_RESIZE_CURSOR);
            }
            
            if( this.bottomRightResizer().contains(cursorLocation) )
            {
                return new Cursor(Cursor.SE_RESIZE_CURSOR);
            }
        }
        
        return null;
    }

    public boolean intersects(Rectangle rectangle)
    {
        return rectangle.intersects(this.toRectangle());
    }

    public Resizer whichResizerIsPressed(Point cursorLocation)
    {
        if(this.selected)
        {
            if( this.topLeftResizer().contains(cursorLocation) )
            {
                return Resizer.TOP_LEFT;
            }
            
            if( this.topRightResizer().contains(cursorLocation) )
            {
                return Resizer.TOP_RIGHT;
            }
            
            if( this.bottomLeftResizer().contains(cursorLocation) )
            {
                return Resizer.BOTTOM_LEFT;
            }
            
            if( this.bottomRightResizer().contains(cursorLocation) )
            {
                return Resizer.BOTTOM_RIGHT;
            }
        }
        return Resizer.NONE;
    }

    void updateBounds(Resizer whichResizer, int dx, int dy)
    {
        if(whichResizer == Resizer.NONE)
        {
            return;
        }
        
        switch(whichResizer)
        {
            case TOP_LEFT:
                this.x += dx;
                this.width -= dx;
                this.y += dy;
                this.height -= dy;
                break;
            case TOP_RIGHT:
                this.width += dx;
                this.y += dy;
                this.height -= dy;
                break;
            case BOTTOM_LEFT:
                this.x += dx;
                this.width -= dx;
                this.height += dy;
                break;
            case BOTTOM_RIGHT:
                this.width += dx;
                this.height += dy;
                break;
        }
    }
    
    public enum Resizer
    {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, NONE;
    }
}

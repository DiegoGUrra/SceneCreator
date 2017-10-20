package pv2d.u1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class JMainWindow extends JFrame implements ActionListener
{

    private JMenuItem importarImagen;
    private final JFileChooser chooser;
    private JMenuItem addScene;
    private JScenePanel scene;
    private JCanvasPanel canvasPanel;
    
    //private JScrollBar hbar;
    //private JScrollBar vbar;

    public JMainWindow()
    {
        
        super("JSceneCreator - hola alpha");
        super.setSize(1366, 768);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.canvasPanel = new JCanvasPanel();
        this.canvasPanel.setPreferredSize(new Dimension(200,100));
        JMenuBar menuBar = new JMenuBar();
        JMenu menu,menu2,submenu;

        menu = new JMenu("Archivo");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);
        
        menu2 = new JMenu("Agregar");
        menu2.setMnemonic(KeyEvent.VK_I);
        menu2.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu2);

        addScene = new JMenuItem("Agregar Escena");
        addScene.setMnemonic(KeyEvent.VK_S);
        addScene.addActionListener(this);
        menu2.add(addScene);
        
        importarImagen = new JMenuItem("Importar imagen");
        importarImagen.setMnemonic(KeyEvent.VK_B);
        importarImagen.addActionListener(this);
        menu.add(importarImagen);

        super.setJMenuBar(menuBar);

        this.chooser = new JFileChooser();
        this.chooser.addChoosableFileFilter(new ImageFilter());
        
        this.scene = new JScenePanel();
        super.setBackground(Color.GRAY);
        //super.getContentPane().add(this.scene);
        JScrollPane scrollPane = new JScrollPane(this.scene);
        //scrollPane.setPreferredSize(new Dimension(1000,1));
        //scrollPane.setViewportView(this.scene);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        super.getContentPane().add(scrollPane,BorderLayout.CENTER);
        super.getContentPane().add(canvasPanel,BorderLayout.EAST);
        //this.hbar=new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0,this.scene.getScene().getWidth());
        //this.vbar=new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0,this.scene.getScene().getHeight());
        //super.getContentPane().add(hbar, BorderLayout.SOUTH);
        //super.getContentPane().add(vbar, BorderLayout.EAST);
        //super.setContentPane(this.scene);
        this.canvasPanel.agregarBotonActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.importarImagen)
        {
            int returnVal = this.chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = this.chooser.getSelectedFile();
                try
                {
                    BufferedImage image = ImageIO.read(file);
                    this.scene.add(image);
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(this, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                

            }
        }
        if(e.getSource() == this.addScene)
        {
            display();
        }
        if(e.getSource() == this.canvasPanel.getAgregar())
        {   
            scene.getScene().add(new Layer());
            this.canvasPanel.agregarLayer(this);
            
            
        }
        for(int i = 0;i<this.canvasPanel.getBotonesEliminar().size();i++)
        {
            if( e.getSource() == this.canvasPanel.getBotonesEliminar().get(i))
            {
                this.scene.getScene().remove(i);
                this.canvasPanel.eliminarLayer(i);
            }
        }
        for(int i =0;i<this.canvasPanel.getIsVisibleButtons().size();i++)
        {
            if(e.getSource()==this.canvasPanel.getIsVisibleButtons().get(i))
            {
                if(this.canvasPanel.getIsVisibleButtons().get(i).isSelected())
                {
                    this.scene.getScene().getLayers().get(i).setVisible(true);
                }
                else
                {
                    this.scene.getScene().getLayers().get(i).setVisible(false);
            
                }
            }
        }
        repaint();
        validate();
    }

    /* ImageFilter.java is used by FileChooserDemo2.java. */
    private class ImageFilter extends FileFilter
    {
        
        public final static String jpeg = "jpeg";
        public final static String jpg = "jpg";
        public final static String gif = "gif";
        public final static String tiff = "tiff";
        public final static String tif = "tif";
        public final static String png = "png";

        //Accept all directories and all gif, jpg, tiff, or png files.
        public boolean accept(File f)
        {
            if (f.isDirectory())
            {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null)
            {
                if (extension.equals(ImageFilter.tiff)
                        || extension.equals(ImageFilter.tif)
                        || extension.equals(ImageFilter.gif)
                        || extension.equals(ImageFilter.jpeg)
                        || extension.equals(ImageFilter.jpg)
                        || extension.equals(ImageFilter.png))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        public String getDescription()
        {
            return "Todas las imágenes";
        }
        
        ImageIcon createImageIcon(String path)
        {
            java.net.URL imgURL = ImageFilter.class.getResource(path);
            if (imgURL != null)
            {
                return new ImageIcon(imgURL);
            }
            else
            {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
        
        public String getExtension(File f)
        {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1)
            {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
    private void display() 
    {
        
        JTextField field1 = new JTextField(" ");
        JTextField field2 = new JTextField(" ");
        JPanel panel = new JPanel(new GridLayout(0, 1));
       
        panel.add(new JLabel("Height: "));
        panel.add(field1);
        panel.add(new JLabel("Width: "));
        panel.add(field2);
        int result = JOptionPane.showConfirmDialog(null, panel, "Crear Escena",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) 
        {
            try
            {
                String height = field1.getText();
                height = height.replaceAll("\\s+","");
                String width = field2.getText();
                width = width.replaceAll("\\s+","");
                System.out.println(field1.getText()+field2.getText());
                int h = Integer.parseInt(height);
                int w = Integer.parseInt(width);
                scene.add(new Scene(w,h));
                //this.hbar.setMaximum(w);
                //this.vbar.setMaximum(h);
                
            }
            catch(NumberFormatException e)
            {
                System.out.println("Error");
            }
            
        } else {
            System.out.println("Cancelled");
        }
    }
}

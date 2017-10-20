/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv2d.u1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author diego
 */
public class JCanvasPanel extends JPanel 
{
    JPanel panelSuperior;
    JPanel panelInferior;
    private JButton agregar;
    private ArrayList<JCheckBox> isVisibleButtons;
    private ArrayList<JButton> botonesEliminar;
    private ArrayList<JComboBox> comboBoxes;
    private Vector<Integer> opciones;
    JCanvasPanel()
    {
        this.opciones = new Vector<Integer>(0);
        this.panelInferior = new JPanel();
        GridLayout a = new GridLayout(0,3);
        a.setVgap(5);
        a.setHgap(10);
        this.panelSuperior = new JPanel(a);
        
        this.comboBoxes = new ArrayList<JComboBox>();
        this.isVisibleButtons = new ArrayList<JCheckBox>();
        this.botonesEliminar = new ArrayList<JButton>();
        
        //super.setLayout(new GridLayout());
        super.add(panelSuperior,BorderLayout.NORTH);
        super.add(panelInferior,BorderLayout.SOUTH);
        
        this.agregar = new JButton("Agregar capa");
        
        this.panelInferior.setPreferredSize(new Dimension(250,50));
        this.agregar.setPreferredSize(new Dimension(250,50));
        
        this.panelInferior.add(agregar,BorderLayout.SOUTH);
        this.panelSuperior.add(new JLabel("Visible"));
        this.panelSuperior.add(new JLabel("Posición"));
        this.panelSuperior.add(new JLabel("Eliminar"));
    }
    
    public JComboBox crearComboBox()
    {
        return new JComboBox();
    }
    
    public JCheckBox crearCheckBox()
    {
        return new JCheckBox();
    }
    
    public JButton crearButton(String nombre)
    {
        return new JButton(nombre);
    }
    public void agregarLayer(ActionListener a)
    {   
        JCheckBox visible = new JCheckBox();
        visible.setSelected(true);
        JComboBox comboBox = new JComboBox();
        JButton eliminar = new JButton("X");
        visible.addActionListener(a);
        comboBox.addActionListener(a);
        eliminar.addActionListener(a);
        
        this.botonesEliminar.add(eliminar);
        this.comboBoxes.add(comboBox);
        this.isVisibleButtons.add(visible);
        
        this.panelSuperior.add(visible);
        this.panelSuperior.add(comboBox);
        this.panelSuperior.add(eliminar);   
        //super.add();
    }
    public void eliminarLayer(int i)
    {
        this.panelSuperior.remove(this.botonesEliminar.remove(i));
        this.panelSuperior.remove(this.comboBoxes.remove(i));
        this.panelSuperior.remove(this.isVisibleButtons.remove(i));
        
    }
    
    
    
    public JButton getAgregar() 
    {
        return agregar;
    }

    public void agregarBotonActionListener(ActionListener a) 
    {
        this.agregar.addActionListener(a);
    }

    public ArrayList<JCheckBox> getIsVisibleButtons() 
    {
        return isVisibleButtons;
    }

    public void setIsVisibleButtons(ArrayList<JCheckBox> isVisibleButtons) 
    {
        this.isVisibleButtons = isVisibleButtons;
    }
    
    public ArrayList<JButton> getBotonesEliminar()
    {
        return this.botonesEliminar;
    }
}

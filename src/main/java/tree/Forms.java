package tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Forms 
{
    private JPanel panel; // Panel principal para los formularios
    private Database querys;


    public Forms() 
    {
        this.querys = new Database(); // Inicializar instancia de Database

        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Cambiar a GridBagLayout para mejor diseño
        panel.setBackground(new Color(220, 220, 220)); // Color gris claro por defecto
    }

    public JPanel getPanel() {
        return panel;
    }

    public void resetPanel() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public void enlaceForm(List<String> informationActivosToShow, Integer nodoArbolId) {
        resetPanel();
    
        // Obtener el título dinámico
        String titulo = "Sin Nombre";
        if (nodoArbolId != null) 
        {
            try 
            {
                String nombre = querys.getNameFromNodoArbol(nodoArbolId);
                titulo = (nombre != null && !nombre.isEmpty()) ? nombre : "Sin Nombre";
            } 
            catch (NumberFormatException e) 
            {
                System.err.println("Error en el formato del ID del nodo: " + e.getMessage());
            }
        }
    
        // Cambiar el diseño del panel principal
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario Nodo de Enlace"));
    
        // Configurar y agregar el título
        JLabel tituloLabel = new JLabel(titulo, JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande y en negrita
        tituloLabel.setForeground(Color.BLACK);
        panel.add(tituloLabel, BorderLayout.NORTH);
    
        // Crear un panel secundario para los campos del formulario
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(new Color(184, 211, 173));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Configurar los campos del formulario
        JLabel lblTipoNodo = new JLabel("Tipo de Nodo:");
        JComboBox<String> comboTipoNodo = new JComboBox<>(new String[]{"Activos", "Variables", "Sentencias"});
        JLabel lblTipoExpresion = new JLabel("Tipo de Expresión:");
        JComboBox<String> comboTipoExpresion = new JComboBox<>(new String[]{"Boolean", "Aritmética"});
        JLabel lblOpcionTF = new JLabel("Opción (T/F):");
        JComboBox<String> comboOpcionTF = new JComboBox<>(new String[]{"T", "F"});
        JButton btnGuardar = new JButton("Guardar");
    
        // Añadir componentes al formularioPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioPanel.add(lblTipoNodo, gbc);
    
        gbc.gridx = 1;
        formularioPanel.add(comboTipoNodo, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        formularioPanel.add(lblTipoExpresion, gbc);
    
        gbc.gridx = 1;
        formularioPanel.add(comboTipoExpresion, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        formularioPanel.add(lblOpcionTF, gbc);
    
        gbc.gridx = 1;
        formularioPanel.add(comboOpcionTF, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formularioPanel.add(btnGuardar, gbc);
    
        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            System.out.println("Guardando Nodo de Enlace:");
            System.out.println("Tipo de Nodo: " + comboTipoNodo.getSelectedItem());
            System.out.println("Tipo de Expresión: " + comboTipoExpresion.getSelectedItem());
            System.out.println("Opción (T/F): " + comboOpcionTF.getSelectedItem());
        });
    
        // Agregar el formulario al centro del panel principal
        panel.add(formularioPanel, BorderLayout.CENTER);
    
        // Refrescar el panel
        panel.revalidate();
        panel.repaint();
    }
    
    

    public void activoForm(List<String> informationActivosToShow, Integer nodoArbolId) {
        resetPanel();
    
        // Obtener el título
        String titulo = "";
        if (nodoArbolId != null) {
            try 
            {
                String nombre = querys.getNameFromNodoArbol(nodoArbolId);
                titulo = (nombre != null && !nombre.isEmpty()) ? nombre : "Sin Nombre";
            } 
            catch (NumberFormatException e) 
            {
                System.err.println("Error en el formato del ID del nodo: " + e.getMessage());
            }
        }
    
        // Cambiar el diseño a BorderLayout para separar el título del formulario
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario Nodo de Activos"));
        panel.setBackground(new Color(184, 211, 173));
    
        // Configurar y agregar el título
        JLabel tituloLabel = new JLabel(titulo, JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande y en negrita
        tituloLabel.setForeground(Color.black);
        panel.add(tituloLabel, BorderLayout.NORTH);
    
        // Crear otro panel para los campos del formulario
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(new Color(184, 211, 173));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Campos del formulario
        JLabel lblTipo = new JLabel("Tipo:");
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Hardware", "Software", "Infraestructura", "Red", "Otro"});
    
        JLabel lblEstado = new JLabel("Estado:");
        JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Operativo", "Inactivo", "En Reparación", "Retirado", "Desconocido"});
    
        JLabel lblMonitor = new JLabel("Monitor (T/F):");
        JComboBox<String> comboMonitor = new JComboBox<>(new String[]{"T", "F"});
    
        JButton btnGuardar = new JButton("Guardar");
    
        // Añadir componentes al formularioPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioPanel.add(lblTipo, gbc);
        gbc.gridx = 1;
        formularioPanel.add(comboTipo, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        formularioPanel.add(lblEstado, gbc);
        gbc.gridx = 1;
        formularioPanel.add(comboEstado, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        formularioPanel.add(lblMonitor, gbc);
        gbc.gridx = 1;
        formularioPanel.add(comboMonitor, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formularioPanel.add(btnGuardar, gbc);
    
        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            System.out.println("Guardando Nodo de Activos:");
            System.out.println("Tipo: " + comboTipo.getSelectedItem());
            System.out.println("Estado: " + comboEstado.getSelectedItem());
            System.out.println("Monitor: " + comboMonitor.getSelectedItem());
        });
    
        // Agregar el formulario al centro del panel principal
        panel.add(formularioPanel, BorderLayout.CENTER);
    
        // Refrescar el panel
        panel.revalidate();
        panel.repaint();
    }
    

    public void VariablesContextoForm(List<String> informationVariablesToShow, Integer nodoArbolId) {
        resetPanel();
    
        // Obtener el título dinámico
        String titulo = "Sin Nombre";
        if (nodoArbolId != null) 
        {
            try 
            {
                String nombre = querys.getNameFromNodoArbol(nodoArbolId);
                titulo = (nombre != null && !nombre.isEmpty()) ? nombre : "Sin Nombre";
            } 
            catch (NumberFormatException e) 
            {
                System.err.println("Error en el formato del ID del nodo: " + e.getMessage());
            }
        }
    
        // Cambiar el diseño del panel principal
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario Variables Contexto"));
        panel.setBackground(new Color(184, 211, 173));

    
        // Configurar y agregar el título
        JLabel tituloLabel = new JLabel(titulo, JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande y en negrita
        tituloLabel.setForeground(Color.BLACK);
        panel.add(tituloLabel, BorderLayout.NORTH);
    
        // Crear un panel secundario para los campos del formulario
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(new Color(184, 211, 173));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Configurar los campos del formulario
        JLabel lblTipoVC = new JLabel("Tipo:");
        JTextField txtTipoVC = new JTextField();
        JLabel lblActivoId = new JLabel("ActivoId:");
        JComboBox<String> comboActivoId = new JComboBox<>(new String[]{"ActivoId 1", "ActivoId 2", "ActivoId 3"});
        JButton btnGuardar = new JButton("Guardar");
    
        // Añadir componentes al formularioPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioPanel.add(lblTipoVC, gbc);
    
        gbc.gridx = 1;
        formularioPanel.add(txtTipoVC, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        formularioPanel.add(lblActivoId, gbc);
    
        gbc.gridx = 1;
        formularioPanel.add(comboActivoId, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Hacer que el botón esté centrado
        gbc.anchor = GridBagConstraints.CENTER;
        formularioPanel.add(btnGuardar, gbc);
    
        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            System.out.println("Guardando Variables Contexto:");
            System.out.println("Tipo: " + txtTipoVC.getText());
            System.out.println("ActivoId: " + comboActivoId.getSelectedItem());
        });
    
        // Agregar el formulario al centro del panel principal
        panel.add(formularioPanel, BorderLayout.CENTER);
    
        // Refrescar el panel
        panel.revalidate();
        panel.repaint();
    }
    

    public void SentenciasForm(List<String> informationSentenciasToShow, Integer nodoArbolId) {
        resetPanel();
    
        // Obtener el título dinámico
        String titulo = "";
        if (nodoArbolId != null) {
            try {
                String nombre = querys.getNameFromNodoArbol(nodoArbolId);
                titulo = (nombre != null && !nombre.isEmpty()) ? nombre : "Sin Nombre";
            } catch (NumberFormatException e) {
                System.err.println("Error en el formato del ID del nodo: " + e.getMessage());
            }
        }
    
        // Cambiar el diseño del panel principal
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario Sentencias"));
        panel.setBackground(new Color(184, 211, 173));

    
        // Configurar y agregar el título
        JLabel tituloLabel = new JLabel(titulo, JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande y en negrita
        tituloLabel.setForeground(Color.BLACK);
        panel.add(tituloLabel, BorderLayout.NORTH);
    
        // Crear un panel secundario para los campos del formulario
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(new Color(184, 211, 173));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Configurar los campos del formulario
        JLabel lblEstado = new JLabel("Estado:");
        JTextField txtEstado = new JTextField();
        txtEstado.setPreferredSize(new Dimension(300, 30)); // Aumentar tamaño del campo de texto
        JButton btnGuardar = new JButton("Guardar");
    
        // Añadir componentes al formularioPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioPanel.add(lblEstado, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Hacer que el campo de texto ocupe todo el ancho
        formularioPanel.add(txtEstado, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Hacer que el botón esté centrado
        gbc.anchor = GridBagConstraints.CENTER;
        formularioPanel.add(btnGuardar, gbc);
    
        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {
            System.out.println("Guardando Sentencias:");
        });
    
        // Agregar el formulario al centro del panel principal
        panel.add(formularioPanel, BorderLayout.CENTER);
    
        // Refrescar el panel
        panel.revalidate();
        panel.repaint();
    }
    
    
    
}

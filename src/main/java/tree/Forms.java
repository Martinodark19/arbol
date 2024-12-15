package tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;

import tree.getters.ActivoFormSimplificadoGetter;
import tree.getters.ConfigurationGetter;
import tree.getters.SentenciasFormSimplificadoGetter;
import tree.getters.VariablesContextoSimplificadoGetter;

public class Forms 
{
    private JPanel panel; // Panel principal para los formularios
    private Database querys;
    private ActivoFormSimplificadoGetter activoFormSimplificadoGetter;
    private VariablesContextoSimplificadoGetter variablesContextoSimplificadoGetter;
    private SentenciasFormSimplificadoGetter sentenciasFormSimplificadoGetter;
    private ConfigurationGetter configurationGetter;
    private  Interfaz interfazPrincipal;


    public Forms(Interfaz interfazPrincipal,ActivoFormSimplificadoGetter activoFormSimplificadoGetter, VariablesContextoSimplificadoGetter variablesContextoSimplificadoGetter,SentenciasFormSimplificadoGetter sentenciasFormSimplificadoGetter,ConfigurationGetter configurationGetter) 
    {
        this.activoFormSimplificadoGetter = activoFormSimplificadoGetter;
        this.variablesContextoSimplificadoGetter = variablesContextoSimplificadoGetter;
        this.sentenciasFormSimplificadoGetter = sentenciasFormSimplificadoGetter;
        this.configurationGetter = configurationGetter;
        this.interfazPrincipal = interfazPrincipal;

        this.querys = new Database(); // Inicializar instancia de Database

        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Cambiar a GridBagLayout para mejor diseño
        panel.setBackground(new Color(220, 220, 220)); // Color gris claro por defecto
    }

    public JPanel getPanel() 
    {
        return panel;
    }

    public void resetPanel() 
    {
        panel.setBorder(null); // Eliminar el borde y el título
        panel.setLayout(null); // Restablecer el diseño
        panel.setBackground(null); // Restablecer el color de fondo
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public void enlaceForm(List<String> informationActivosToShow, Integer nodoArbolId) 
    {
        resetPanel();
        
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario Nodo de Enlace"));
    
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
    

    public void activoForm(List<String> informationActivosToShow, Integer nodoArbolId) 
    {

        resetPanel();
        // Cambiar el diseño a BorderLayout para separar el título del formulario
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario Nodo de Activos"));
        panel.setBackground(new Color(184, 211, 173));
    
        // Crear otro panel para los campos del formulario
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(new Color(184, 211, 173));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(20); // El número 20 es el ancho inicial del campo


        // Campos del formulario
        JLabel lblTipo = new JLabel("Tipo:");
        JComboBox<String> comboTipo = new JComboBox<>();
    
        JLabel lblEstado = new JLabel("Estado:");
        JComboBox<String> comboEstado = new JComboBox<>();
    
        JLabel lblMonitor = new JLabel("Monitor (T/F):");
        JComboBox<String> comboMonitor = new JComboBox<>();
            
        // Obtener los valores actuales de la base de datos
        //Map<Integer, String> activoDetails = querys.getActivoDetails(nodoArbolId);
        
        if (!informationActivosToShow.isEmpty()) 
        {
            comboTipo.addItem(informationActivosToShow.get(0));
            comboEstado.addItem(informationActivosToShow.get(1));

            for (int i = 1; i < 4; i++) 
            {
                comboTipo.addItem("Tipo " + i);
                comboEstado.addItem("Estado " + i);
            }

            comboMonitor.addItem("T");
            comboMonitor.addItem("F");

            // Establecer el valor seleccionado por defecto
            comboTipo.setSelectedItem(informationActivosToShow.get(0));
            comboEstado.setSelectedItem(informationActivosToShow.get(1));
            comboMonitor.setSelectedItem(informationActivosToShow.get(2));

            txtNombre.setText(informationActivosToShow.get(3));

        }

        JButton btnEliminar = new JButton("Eliminar Nodo");
        btnEliminar.setBackground(Color.decode("#e84a28"));


        btnEliminar.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar un cuadro de confirmación
                int opcion = JOptionPane.showConfirmDialog(
                        panel,
                        "¿Estás seguro de que deseas eliminar el nodo?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // Verificar la opción seleccionada
                if (opcion == JOptionPane.YES_OPTION) 
                {

                    Boolean eliminarNodo = querys.eliminarNodoArbol(nodoArbolId);

                    if (eliminarNodo) 
                    {
                        JOptionPane.showMessageDialog(
                            panel, // Panel o componente padre
                            "Nodo eliminado con exito.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                        ); 

                        // Reiniciar el panel y actualizar el árbol
                        interfazPrincipal.reloadTreePanel();
                        interfazPrincipal.limpiarFormPanel();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(
                            panel,
                            "Se produjo un error al intentar eliminar el nodo",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                    

                    // Si el usuario confirma, mostrar un mensaje en la consola
                    interfazPrincipal.reloadTreePanel();
                } 
                else 
                {
                    // Si el usuario cancela, puedes manejarlo aquí (opcional)
                    System.out.println("Cancelado");
                }
            }
        });




        JButton btnGuardar = new JButton("Guardar");

        //logica para mostrar el boton guardar dependiendo de los permisos del usuario
        if (configurationGetter.getPermisosUsuario()) 
        {
            btnGuardar.setVisible(true);
        }
        else
        {
            btnGuardar.setVisible(false);
    
        }


        if (configurationGetter.getPermisosUsuario()) 
        {
            btnEliminar.setVisible(true);

        }
        else
        {
            btnEliminar.setVisible(false);
        }

            // Añadir componentes al formularioPanel en el orden deseado
        // Primero el campo "Nombre"
        gbc.gridx = 0;
        gbc.gridy = 0;
        formularioPanel.add(lblNombre, gbc);
        gbc.gridx = 1;
        formularioPanel.add(txtNombre, gbc);

        // Luego "Tipo"
        gbc.gridx = 0;
        gbc.gridy = 1;
        formularioPanel.add(lblTipo, gbc);
        gbc.gridx = 1;
        formularioPanel.add(comboTipo, gbc);

        // Luego "Estado"
        gbc.gridx = 0;
        gbc.gridy = 2;
        formularioPanel.add(lblEstado, gbc);
        gbc.gridx = 1;
        formularioPanel.add(comboEstado, gbc);

        // Luego "Monitor"
        gbc.gridx = 0;
        gbc.gridy = 3;
        formularioPanel.add(lblMonitor, gbc);
        gbc.gridx = 1;
        formularioPanel.add(comboMonitor, gbc);

        // Botón Eliminar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formularioPanel.add(btnEliminar, gbc);

        // Botón Guardar
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formularioPanel.add(btnGuardar, gbc);

        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> {

            // Obtener valores actuales del formulario
            String tipo = (String) comboTipo.getSelectedItem();
            String estado = (String) comboEstado.getSelectedItem();
            String monitor = (String) comboMonitor.getSelectedItem();
        

            // Actualizar en la base de datos
            boolean isUpdated = querys.updateActivoDetails(nodoArbolId, tipo, estado, monitor, txtNombre.getText());
        
            if (isUpdated) 
            {
                JOptionPane.showMessageDialog(panel, "Datos actualizados correctamente.");
        
                // Recargar los valores desde la base de datos
                Map<Integer, String> updatedDetails = querys.getActivoDetails(nodoArbolId);
                if (!updatedDetails.isEmpty()) 
                {
                    comboTipo.setSelectedItem(updatedDetails.get(1));
                    comboEstado.setSelectedItem(updatedDetails.get(2));
                    comboMonitor.setSelectedItem(updatedDetails.get(3));
                }
                //// Refrescar el panel
                //panel.revalidate();
                //panel.repaint();

                interfazPrincipal.reloadTreePanel();
                interfazPrincipal.limpiarFormPanel();            
            } 
            else 
            {
                JOptionPane.showMessageDialog(panel, "No se pudo actualizar la información. Verifica los datos.");
            }
        });
    
        // Agregar el formulario al centro del panel principal
        panel.add(formularioPanel, BorderLayout.CENTER);
    
        // Refrescar el panel
        panel.revalidate();
        panel.repaint();
    }


    public void VariablesContextoForm(List<String> informationVariablesToShow, Integer nodoArbolId) 
{

    resetPanel();
    // Cambiar el diseño del panel principal
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Formulario Variables Contexto"));
    panel.setBackground(new Color(184, 211, 173));

    // Crear un panel secundario para los campos del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;


    JLabel lblNombre = new JLabel("Nombre:");
    JTextField txtNombre = new JTextField(20); // El número 20 es el ancho inicial del campo

    txtNombre.setText(informationVariablesToShow.get(2));


    // Configurar los campos del formulario
    JLabel lblTipoVC = new JLabel("Tipo:");
    JTextField txtTipoVC = new JTextField();
    txtTipoVC.setPreferredSize(new Dimension(300, 30)); // Aumentar tamaño del campo de texto
    txtTipoVC.setText(informationVariablesToShow.get(0));

    txtTipoVC.addKeyListener(new KeyAdapter() 
    {
        @Override
        public void keyTyped(KeyEvent e) 
        {
            if (txtTipoVC.getText().length() >= 30) { // Máximo 30 caracteres
                e.consume();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    });

    // Crear label y combo box para 'ActivoId'
    JLabel lblActivoId = new JLabel("ActivoId:");
    JComboBox<Integer> comboActivoId = new JComboBox<>();

    // Obtener el mapa de activos (ID y nombre)
    Map<Integer, String> getActivos = querys.obtenerSoloActivoId();

    // Obtener el conjunto de IDs de activos
    Set<Integer> activos = getActivos.keySet();

    // Agregar los IDs de activos al JComboBox
    for (Integer activo : activos) 
    {
        comboActivoId.addItem(activo);
    }

    try 
    {
        // Establecer el valor seleccionado por defecto
        comboActivoId.setSelectedItem(Integer.parseInt(informationVariablesToShow.get(1)));
    } 
    catch (NumberFormatException e) 
    {
        System.err.println("El valor obtenido no es un número válido: " + informationVariablesToShow.get(1));
    }

    // Crear los botones "Guardar" y "Eliminar Nodo"
    JButton btnGuardar = new JButton("Guardar");

    JButton btnEliminar = new JButton("Eliminar Nodo");
    btnEliminar.setBackground(Color.decode("#e84a28"));


            //logica para mostrar el boton guardar dependiendo de los permisos del usuario
            if (configurationGetter.getPermisosUsuario()) 
            {
                btnGuardar.setVisible(true);
            }
            else
            {
                btnGuardar.setVisible(false);
        
            }
    
    
            if (configurationGetter.getPermisosUsuario()) 
            {
                btnEliminar.setVisible(true);
            }
            else
            {
                btnEliminar.setVisible(false);
            }

    // Crear un panel para los botones con FlowLayout
    JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    botonesPanel.setBackground(new Color(184, 211, 173)); // Fondo coherente

    // Añadir los botones al panel de botones
    botonesPanel.add(btnGuardar);
    botonesPanel.add(btnEliminar);

    gbc.gridx = 0;
    gbc.gridy = 0;
    formularioPanel.add(lblNombre, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    formularioPanel.add(txtNombre, gbc);

    // Luego Tipo
    gbc.gridx = 0;
    gbc.gridy = 1;
    formularioPanel.add(lblTipoVC, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    formularioPanel.add(txtTipoVC, gbc);

    // Luego ActivoId
    gbc.gridx = 0;
    gbc.gridy = 2;
    formularioPanel.add(lblActivoId, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    formularioPanel.add(comboActivoId, gbc);

    // Finalmente el panel de botones
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2; 
    gbc.anchor = GridBagConstraints.CENTER;
    formularioPanel.add(botonesPanel, gbc);

    // Acción del botón Guardar
    btnGuardar.addActionListener(e -> {
        System.out.println("Guardando Variables Contexto:");
        String tipo = txtTipoVC.getText();
        Integer activoId = (Integer) comboActivoId.getSelectedItem();

        boolean isUpdated = querys.updateVariableDetails(nodoArbolId, tipo, activoId,txtNombre.getText());
        if (isUpdated) 
        {
            JOptionPane.showMessageDialog(panel, "Datos actualizados correctamente.");

            // Recargar los valores actualizados desde la base de datos
            Map<Integer, String> updatedDetails = querys.getVariableDetails(nodoArbolId);
            if (!updatedDetails.isEmpty()) 
            {
                txtTipoVC.setText(updatedDetails.get(1));
                comboActivoId.setSelectedItem(Integer.parseInt(updatedDetails.get(2)));
            }
            // Refrescar el panel
            //panel.revalidate();
            //panel.repaint();
            interfazPrincipal.reloadTreePanel();
            interfazPrincipal.limpiarFormPanel();   
        } 
        else 
        {
            JOptionPane.showMessageDialog(panel, "No se pudo actualizar la información. Verifica los datos.");
        }
    });

    // Acción del botón Eliminar Nodo
    btnEliminar.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Mostrar un cuadro de confirmación
            int opcion = JOptionPane.showConfirmDialog(
                    panel,
                    "¿Estás seguro de que deseas eliminar el nodo?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Verificar la opción seleccionada
            if (opcion == JOptionPane.YES_OPTION) 
            {
                Boolean eliminarNodo = querys.eliminarNodoArbol(nodoArbolId);

                if (eliminarNodo) 
                {
                    JOptionPane.showMessageDialog(
                        panel, // Panel o componente padre
                        "Nodo eliminado con exito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    ); 

                    // Reiniciar el panel y actualizar el árbol
                    interfazPrincipal.reloadTreePanel();
                    interfazPrincipal.limpiarFormPanel();
                }
                else
                {
                    JOptionPane.showMessageDialog(
                        panel,
                        "Se produjo un error al intentar eliminar el nodo",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

                // Recargar el panel del árbol
                interfazPrincipal.reloadTreePanel();
            } 
            else 
            {
                // Si el usuario cancela, manejarlo aquí (opcional)
                System.out.println("Cancelado");
            }
        }
    });

    // Agregar el formulario al centro del panel principal
    panel.add(formularioPanel, BorderLayout.CENTER);

    // Refrescar el panel
    panel.revalidate();
    panel.repaint();
}

    
public void SentenciasForm(List<String> informationSentenciasToShow, Integer nodoArbolId) 
{
    resetPanel();

    // Cambiar el diseño del panel principal
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Formulario Sentencias"));
    panel.setBackground(new Color(184, 211, 173));

    // Crear un panel secundario para los campos del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;


    JLabel lblNombre = new JLabel("Nombre:");
    JTextField txtNombre = new JTextField(20); // El número 20 es el ancho inicial del campo
    txtNombre.setText(informationSentenciasToShow.get(2));

    // Configurar los campos del formulario
    JLabel lblQuery = new JLabel("Query:");
    JTextField txtQuery = new JTextField();
    System.out.println(informationSentenciasToShow.get(1));
    txtQuery.setText(informationSentenciasToShow.get(1));
    txtQuery.setPreferredSize(new Dimension(300, 30)); // Aumentar tamaño del campo de texto

    txtQuery.addKeyListener(new KeyAdapter() 
    {
        @Override
        public void keyTyped(KeyEvent e) 
        {
            if (txtQuery.getText().length() >= 255) { // Máximo 255 caracteres (ajusta según necesidad)
                e.consume();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    });

    JLabel lblEstado = new JLabel("Estado:");
    JComboBox<String> comboEstado = new JComboBox<>(new String[]{"ACTIVA", "NO ACTIVA"});
    comboEstado.setPreferredSize(new Dimension(300, 30)); // Aumentar tamaño del campo

    // Establecer el elemento seleccionado basado en el valor obtenido
    comboEstado.setSelectedItem(informationSentenciasToShow.get(0));

    JButton btnGuardar = new JButton("Guardar");

    // Lógica para mostrar el botón guardar dependiendo de los permisos del usuario
    if (configurationGetter.getPermisosUsuario()) 
    {
        btnGuardar.setVisible(true);
    }
    else
    {
        btnGuardar.setVisible(false);
    }


    

    // Crear el botón "Eliminar Nodo"
    JButton btnEliminar = new JButton("Eliminar Nodo");
    btnEliminar.setBackground(Color.decode("#e84a28"));


    if (configurationGetter.getPermisosUsuario()) 
    {
        btnEliminar.setVisible(true);
    }
    else
    {
        btnEliminar.setVisible(false);
    }


    // Crear un panel para los botones con FlowLayout
    JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    botonesPanel.setBackground(new Color(184, 211, 173)); // Fondo coherente con el formulario

    // Añadir los botones al panel de botones
    botonesPanel.add(btnGuardar);
    botonesPanel.add(btnEliminar);

    // Añadir componentes al formularioPanel en el orden requerido
    // Primero el Nombre
    gbc.gridx = 0;
    gbc.gridy = 0;
    formularioPanel.add(lblNombre, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    formularioPanel.add(txtNombre, gbc);

    // Luego Query
    gbc.gridx = 0;
    gbc.gridy = 1;
    formularioPanel.add(lblQuery, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    formularioPanel.add(txtQuery, gbc);

    // Luego Estado
    gbc.gridx = 0;
    gbc.gridy = 2;
    formularioPanel.add(lblEstado, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    formularioPanel.add(comboEstado, gbc);

    // Panel de botones
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2; // Ocupa dos columnas
    gbc.anchor = GridBagConstraints.CENTER;
    formularioPanel.add(botonesPanel, gbc);

    // Acción del botón Guardar
    btnGuardar.addActionListener(e -> {
        System.out.println("Guardando Sentencias:");

        // Obtener los valores del formulario
        String estado = (String) comboEstado.getSelectedItem();
        String query = txtQuery.getText();

        // Validar campos obligatorios (opcional)
        if (query.trim().isEmpty()) 
        {
            JOptionPane.showMessageDialog(panel, "El campo 'Query' no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }


        // Actualizar el valor en la base de datos
        boolean isUpdated = querys.updateSentenciaEstado(nodoArbolId, estado, query, txtNombre.getText());

        if (isUpdated) 
        {
            JOptionPane.showMessageDialog(panel, "Datos actualizados correctamente.");
            // Recargar los valores actualizados desde la base de datos
            Map<Integer, String> updatedDetails = querys.getSentenciaDetails(nodoArbolId);
            if (!updatedDetails.isEmpty()) 
            {
                comboEstado.setSelectedItem(updatedDetails.get(1)); // Actualizar la selección
                txtQuery.setText(updatedDetails.get(2)); // Actualizar el campo Query
            }

            // Refrescar el panel
            //panel.revalidate();
            //panel.repaint();

            // Reiniciar el panel y actualizar el árbol
            interfazPrincipal.reloadTreePanel();
            interfazPrincipal.limpiarFormPanel();
        } 
        else 
        {
            JOptionPane.showMessageDialog(panel, "No se pudo actualizar la información. Verifica los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }        
    });

    // Acción del botón Eliminar Nodo
    btnEliminar.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Mostrar un cuadro de confirmación
            int opcion = JOptionPane.showConfirmDialog(
                    panel,
                    "¿Estás seguro de que deseas eliminar el nodo?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Verificar la opción seleccionada
            if (opcion == JOptionPane.YES_OPTION) 
            {
                Boolean eliminarNodo = querys.eliminarNodoArbol(nodoArbolId);

                if (eliminarNodo) 
                {
                    JOptionPane.showMessageDialog(
                        panel, // Panel o componente padre
                        "Nodo eliminado con éxito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    ); 

                    // Reiniciar el panel y actualizar el árbol
                    interfazPrincipal.reloadTreePanel();
                    interfazPrincipal.limpiarFormPanel();
                }
                else
                {
                    JOptionPane.showMessageDialog(
                        panel,
                        "Se produjo un error al intentar eliminar el nodo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

                // Recargar el panel del árbol
                interfazPrincipal.reloadTreePanel();
            } 
            else 
            {
                // Si el usuario cancela, manejarlo aquí (opcional)
                System.out.println("Eliminación cancelada.");
            }
        }
    });

    // Agregar el formulario al centro del panel principal
    panel.add(formularioPanel, BorderLayout.CENTER);

    // Refrescar el panel
    panel.revalidate();
    panel.repaint();
}



 // Formularios para agregar nuevos nodos
public void activoFormSimplificado(JPanel panel) 
{
    // Reiniciar el panel recibido
    resetPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Formulario Nodo de Activos"));
    panel.setBackground(new Color(184, 211, 173));

    // Crear un panel secundario para los campos del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Campos del formulario
    JLabel lblTipo = new JLabel("Tipo:");
    JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Har", "Software", "Infra", "Red", "Otro"});
    JLabel lblEstado = new JLabel("Estado:");
    JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Opera", "Inac", "En Rep", "Reti", "Desc"});
    JLabel lblMonitor = new JLabel("Monitor (T/F):");
    JComboBox<String> comboMonitor = new JComboBox<>(new String[]{"T", "F"});

    // Antes de agregar los ActionListeners
    activoFormSimplificadoGetter.setTipo((String) comboTipo.getSelectedItem());
    activoFormSimplificadoGetter.setEstado((String) comboEstado.getSelectedItem());
    activoFormSimplificadoGetter.setMonitor((String) comboMonitor.getSelectedItem());


    // Registrar ActionListeners para detectar cambios en los JComboBox
    comboTipo.addActionListener(e -> {
        String selectedTipo = (String) comboTipo.getSelectedItem();
        activoFormSimplificadoGetter.setTipo(selectedTipo);
    });

    comboEstado.addActionListener(e -> {
        String selectedEstado = (String) comboEstado.getSelectedItem();
        activoFormSimplificadoGetter.setEstado(selectedEstado);
    });

    comboMonitor.addActionListener(e -> {
        String selectedMonitor = (String) comboMonitor.getSelectedItem();
        activoFormSimplificadoGetter.setMonitor(selectedMonitor);
    });

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

    // Agregar el formulario al centro del panel recibido
    panel.add(formularioPanel, BorderLayout.CENTER);

    // Refrescar el panel
    panel.revalidate();
    panel.repaint();
}

    public void VariablesContextoFormSimplificado(JPanel panel) 
{
    // Reiniciar el panel
    resetPanel();

    // Cambiar el diseño del panel principal
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Formulario Variables Contexto"));
    panel.setBackground(new Color(184, 211, 173));

    // Crear un panel secundario para los campos del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Campos del formulario
    JLabel lblTipoVC = new JLabel("Tipo:");
    JTextField txtTipoVC = new JTextField(20);

    // Listener para actualizar el getter al escribir en txtTipoVC
    txtTipoVC.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            variablesContextoSimplificadoGetter.setTipoVC(txtTipoVC.getText());
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (txtTipoVC.getText().length() >= 30) { // Máximo 30 caracteres
                e.consume();
                Toolkit.getDefaultToolkit().beep();

            }
        }
    });

    Map<Integer, String> obtenerActivosIds = querys.obtenerSoloActivoId();

    JLabel lblActivoId = new JLabel("ActivoId:");
    JComboBox<Integer> comboActivoId = new JComboBox<>();

    // Verificar si el Map está vacío
    if (obtenerActivosIds.isEmpty()) 
    {
        
        // Mostrar un cuadro de diálogo informativo
        JOptionPane.showMessageDialog(
            panel, // Panel o componente padre
            "No hay nodos activos disponibles. Por favor, agregue nodos activos antes de continuar.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );
    
        // Mostrar un mensaje predeterminado en el JComboBox
        comboActivoId.addItem(-1); // Valor simbólico
        comboActivoId.setSelectedItem(-1);
        comboActivoId.setEnabled(false); // Deshabilitar para evitar interacciones
        // Deshabilitar el campo de texto
        txtTipoVC.setEnabled(false);

        variablesContextoSimplificadoGetter.setDisponibilidadForm(false);
    }
    else 
    {
        variablesContextoSimplificadoGetter.setDisponibilidadForm(true);
        // Llenar el JComboBox con los valores del Map
        for (Integer key : obtenerActivosIds.keySet()) 
        {
            comboActivoId.addItem(key);
        }
    }

    // Listener para actualizar el getter al cambiar la selección en comboActivoId
// Seleccionar manualmente el valor actual para disparar el ActionListener
    comboActivoId.addActionListener(e -> {
        variablesContextoSimplificadoGetter.setActivoId((Integer) comboActivoId.getSelectedItem());
    });

    // Forzar la ejecución del ActionListener al inicio
    if (comboActivoId.getSelectedItem() != null) 
    {
        comboActivoId.setSelectedItem(comboActivoId.getSelectedItem()); // Simula la interacción
    }


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

    // Agregar el formulario al centro del panel principal
    panel.add(formularioPanel, BorderLayout.CENTER);
                

    // Refrescar el panel
    panel.revalidate();
    panel.repaint();
}

    
    public void SentenciasFormSimplificado(JPanel panel) 
{
    // Reiniciar el panel
    resetPanel();

    // Configurar el diseño y apariencia del panel principal
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Formulario Sentencias"));
    panel.setBackground(new Color(184, 211, 173));

    // Crear un panel secundario para los campos del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.BOTH; 
    gbc.anchor = GridBagConstraints.NORTHWEST; 

    // Campo Estado
    JLabel lblEstado = new JLabel("Estado:");
    JComboBox<String> comboEstado = new JComboBox<>(new String[]{"ACTIVA", "NO ACTIVA"});
    sentenciasFormSimplificadoGetter.setEstado((String) comboEstado.getSelectedItem());

    // Listener para actualizar el getter al seleccionar un estado en el comboEstado
    comboEstado.addActionListener(e -> {
        String selectedEstado = (String) comboEstado.getSelectedItem();
        sentenciasFormSimplificadoGetter.setEstado(selectedEstado);
    });

    // Añadir lblEstado al formularioPanel
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    formularioPanel.add(lblEstado, gbc);

    // Añadir comboEstado al formularioPanel
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 0;
    formularioPanel.add(comboEstado, gbc);

    // Configurar el campo de Query como JTextField en lugar de JTextArea
    JLabel lblQuery = new JLabel("Query:");
    JTextField txtQueryVC = new JTextField(20); // Ajustar el ancho del campo según sea necesario
    sentenciasFormSimplificadoGetter.setQuery(txtQueryVC.getText());

    // Listener opcional para actualizar el getter al cambiar el texto
    txtQueryVC.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            sentenciasFormSimplificadoGetter.setQuery(txtQueryVC.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            sentenciasFormSimplificadoGetter.setQuery(txtQueryVC.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            sentenciasFormSimplificadoGetter.setQuery(txtQueryVC.getText());
        }
    });

    // Añadir lblQuery al formularioPanel
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.weighty = 0;
    formularioPanel.add(lblQuery, gbc);

    // Añadir txtQueryVC al formularioPanel
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    formularioPanel.add(txtQueryVC, gbc);

    // Añadir formularioPanel al centro del panel principal
    panel.add(formularioPanel, BorderLayout.CENTER);

    // Refrescar el panel
    panel.revalidate();
    panel.repaint();
}



public void configuracionNodoRaiz(JPanel panel) 
{
    // Reiniciar el panel
    resetPanel();

    // Configurar el diseño del panel
    panel.setLayout(new BorderLayout());
    //panel.setBorder(BorderFactory.createTitledBorder("Configuración Nodo Raíz"));
    panel.setBackground(new Color(184, 211, 173));

    // Crear el panel del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;


    String nombreRaiz = querys.obtenerNombreRaiz();


    // Campos del formulario
    JLabel lblNombreRaiz = new JLabel("Nombre Nodo Raíz:");
    JTextField txtNombreRaiz = new JTextField(20);
    txtNombreRaiz.setText(nombreRaiz);




    // Limitar la longitud del texto
    txtNombreRaiz.addKeyListener(new KeyAdapter() 
    {
        @Override
        public void keyTyped(KeyEvent e) {
            if (txtNombreRaiz.getText().length() >= 30) 
            {
                e.consume();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    });

    // Crear el botón Guardar
    JButton btnGuardar = new JButton("Guardar");
    btnGuardar.addActionListener(e -> {
        String nuevoNombre = txtNombreRaiz.getText().trim();
        if (!nuevoNombre.isEmpty()) 
        {
            //configurationGetter.setNodoNombreRaiz(nuevoNombre);
            Boolean guardarNodoRaiz = querys.actualizarNombreNodoRaiz(nuevoNombre);

            if(guardarNodoRaiz)
            {
                // Llamar al método en la interfaz principal para actualizar el árbol
                JOptionPane.showMessageDialog(
                    panel,
                    "Nodo Raíz Guardado Con Éxito.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
                interfazPrincipal.actualizarNombreNodoRaiz();
                // Reiniciar el panel y actualizar el árbol
                interfazPrincipal.actualizarNombreNodoRaiz();
                interfazPrincipal.limpiarFormPanel();

            }
            else
            {
                JOptionPane.showMessageDialog(
                    panel,
                    "Ha ocurrido un error al intentar guardar el nodo raiz.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }


        } 
        else 
        {
            JOptionPane.showMessageDialog(
                panel,
                "El nombre del nodo raíz no puede estar vacío.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    });

    // Añadir componentes al formulario
    gbc.gridx = 0;
    gbc.gridy = 0;
    formularioPanel.add(lblNombreRaiz, gbc);

    gbc.gridx = 1;
    formularioPanel.add(txtNombreRaiz, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    formularioPanel.add(btnGuardar, gbc);

    // Añadir el formulario al panel principal
    panel.add(formularioPanel, BorderLayout.CENTER);

    // Refrescar el panel
    panel.revalidate();
    panel.repaint();
}





public void crearRelacionesNodoForm(JPanel panel) 
{
    // 1. Definir variables locales y encapsular las que se modificarán en lambdas
    final String[] nodoPadreEliminar = {null};
    final List<String> nodosHijosDisponibles = new ArrayList<>();
    final JComboBox<String> hijosComboBox = new JComboBox<>();
    
    final Integer[] nodoArbolIdPadre = {null};
    final Integer[] nodoArbolIdHijo = {null};
    
    final JButton btnGuardarLocal = new JButton("Guardar");
    
    // 2. Reiniciar el panel
    resetPanel();

    // 3. Configurar el diseño del panel
    panel.setLayout(new BorderLayout());
    panel.setBackground(new Color(184, 211, 173));

    // 4. Crear el panel del formulario
    JPanel formularioPanel = new JPanel(new GridBagLayout());
    formularioPanel.setBackground(new Color(184, 211, 173));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // 5. Componentes del formulario
    JLabel nodosPadreLabel = new JLabel("Seleccione el nodo padre:");
    JComboBox<String> nodosComboBox = new JComboBox<>();

    // 6. Obtener los nombres de nodos desde la base de datos
    Map<Integer, String> obtenerNodos = querys.obtenerNombresNodos();
    nodosComboBox.addItem("Seleccione una opción..."); // Ítem vacío

    // 7. Verificar si hay nodos disponibles
    if (obtenerNodos.isEmpty()) 
    {
        JOptionPane.showMessageDialog(
            panel,
            "No existen nodos disponibles en este momento. Por favor inserte nuevos nodos para poder crear las relaciones",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    else
    {
        // 8. Llenar el JComboBox de nodos padre
        for (String nombre : obtenerNodos.values()) 
        {
            nodosComboBox.addItem(nombre);
        }
        // Opción por defecto
        nodosComboBox.setSelectedIndex(0);

        nodoPadreEliminar[0] = nodosComboBox.getSelectedItem().toString();

        // 9. Deshabilitar inicialmente el JComboBox hijos y el botón guardar
        hijosComboBox.setEnabled(false);
        btnGuardarLocal.setEnabled(false);

        // 10. Registrar ActionListener para el JComboBox de nodos padre
        nodosComboBox.addActionListener(e -> {
            String seleccionadoPadre = (String) nodosComboBox.getSelectedItem();

            if (seleccionadoPadre == null || seleccionadoPadre.equals("Seleccione una opción...")) 
            {
                hijosComboBox.setEnabled(false);
                btnGuardarLocal.setEnabled(false);
                hijosComboBox.removeAllItems();
                hijosComboBox.addItem("Seleccione una opción...");
                nodoPadreEliminar[0] = null; // Resetear
                return;
            }
            else
            {
                hijosComboBox.setEnabled(true);
                btnGuardarLocal.setEnabled(true);
            }

            nodoPadreEliminar[0] = seleccionadoPadre;

            hijosComboBox.removeAllItems();
            nodosHijosDisponibles.clear();

            // Obtener nodos sin familia
            List<String> nodosSinfamilia = querys.obtenerNodosSinRelacion();
            if (!nodosSinfamilia.isEmpty()) 
            {
                for (String nodo : nodosSinfamilia) 
                {
                    nodosHijosDisponibles.add(nodo);
                }
            }
            else
            {
                hijosComboBox.addItem("No existen nodos disponibles");
                hijosComboBox.setSelectedItem("No existen nodos disponibles");
                hijosComboBox.setEnabled(false);
                btnGuardarLocal.setEnabled(false);
                return;
            }

            nodosHijosDisponibles.remove(nodoPadreEliminar[0]);

            // Obtener el nodo seleccionado en el árbol
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) interfazPrincipal.treeModel.getRoot();
            DefaultMutableTreeNode nodoEncontrado = interfazPrincipal.buscarNodoPorNombre(rootNode, seleccionadoPadre);
            List<String> buscarHijosNodos = interfazPrincipal.obtenerNodosConHijos(rootNode);

            if (!buscarHijosNodos.isEmpty()) 
            {
                for(String buscarHijo : buscarHijosNodos)
                {
                    nodosHijosDisponibles.remove(buscarHijo);
                }
            }

            // Validación para evitar ciclos
            if (nodoEncontrado != null) 
            {
                List<String> ancestros = interfazPrincipal.obtenerAncestrosDesdeNodo(nodoEncontrado);
                for (String ancestro : ancestros) 
                {
                    nodosHijosDisponibles.remove(ancestro);
                }
            } 
            else 
            {
                System.out.println("No se encontró un nodo con el nombre: " + seleccionadoPadre);
            }

            // Añadir nodos hijos disponibles al JComboBox
            for (String hijo : nodosHijosDisponibles) 
            {
                hijosComboBox.addItem(hijo);
            }

            // Si no hay nodos disponibles después de las eliminaciones
            if (nodosHijosDisponibles.isEmpty()) 
            {
                hijosComboBox.addItem("No existen nodos disponibles");
                hijosComboBox.setSelectedItem("No existen nodos disponibles");
                hijosComboBox.setEnabled(false);
                btnGuardarLocal.setEnabled(false);
            }
        });
    }

    // 11. Lógica para mostrar los nodos hijos disponibles
    JLabel nodoHijoLabel = new JLabel("Seleccione el nodo hijo:");
    // JComboBox<String> hijosComboBox = new JComboBox<>(); // Ya definido arriba

    JLabel opcionTFLabel = new JLabel("Seleccione opcionTF:");
    JComboBox<String> opcionTFComboBox = new JComboBox<>(new String[]{"T","F"});
    opcionTFComboBox.setSelectedItem(null); // No selecciona ningún elemento por defecto

    // 12. Registrar ActionListener para el botón Guardar
    btnGuardarLocal.addActionListener(e -> {

        
        // Obtener el nodoArbolId del map obtenerNodos
        for (Map.Entry<Integer, String> idNodoArbol : obtenerNodos.entrySet()) 
        {
            if (idNodoArbol.getValue().equals(nodosComboBox.getSelectedItem())) 
            {
                nodoArbolIdPadre[0] = idNodoArbol.getKey();
            }

            if (idNodoArbol.getValue().equals(hijosComboBox.getSelectedItem())) 
            {
                nodoArbolIdHijo[0] = idNodoArbol.getKey();
            }
        }

        // Verificar si existe un solo nodo para bloquear el JComboBox hijos
        int cantidadElementosHijos = hijosComboBox.getItemCount();

        if (cantidadElementosHijos == 1)  
        {
            hijosComboBox.addItem("No existen nodos disponibles");
            hijosComboBox.setSelectedItem("No existen nodos disponibles");
            hijosComboBox.setEnabled(false);
            btnGuardarLocal.setEnabled(false);
        }

        // Enviar a la base de datos
        String opcionTFSeleccionada = (String) opcionTFComboBox.getSelectedItem();
        Boolean enviarRelacionNodoArbol = querys.InsertRelacionesNodos(nodoArbolIdPadre[0], nodoArbolIdHijo[0], opcionTFSeleccionada);
        
        if (enviarRelacionNodoArbol) 
        {
            JOptionPane.showMessageDialog(
                panel,
                "Nodo Relacionado Con Éxito.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Reiniciar el panel y actualizar el árbol
            interfazPrincipal.reloadTreePanel();
            interfazPrincipal.limpiarFormPanel();
        }
        else
        {
            JOptionPane.showMessageDialog(
                panel,
                "No se puede asignar un nuevo padre al nodo seleccionado.",
                "Error", // Título de la ventana
                JOptionPane.ERROR_MESSAGE  // Tipo de mensaje
            );
        }
    });

    // 13. Añadir componentes al formulario
    gbc.gridx = 0;
    gbc.gridy = 0;
    formularioPanel.add(nodosPadreLabel, gbc);
    gbc.gridx = 1;
    formularioPanel.add(nodosComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    formularioPanel.add(nodoHijoLabel, gbc);
    gbc.gridx = 1;
    formularioPanel.add(hijosComboBox, gbc);

    // Fila 2: opcionTF y opcionTFJComboBox
    gbc.gridx = 0;
    gbc.gridy = 2;
    formularioPanel.add(opcionTFLabel, gbc);

    gbc.gridx = 1; // Cambiar a 1 para mantener la consistencia
    formularioPanel.add(opcionTFComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    formularioPanel.add(btnGuardarLocal, gbc);

    // 14. Añadir el formulario al panel principal
    panel.add(formularioPanel, BorderLayout.CENTER);

    // 15. Refrescar el panel
    panel.revalidate();
    panel.repaint();
}


}

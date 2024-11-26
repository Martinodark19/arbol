package tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import tree.getters.ActivoFormSimplificadoGetter;
import tree.getters.SentenciasFormSimplificadoGetter;
import tree.getters.VariablesContextoSimplificadoGetter;

public class Interfaz extends JFrame 
{
    protected JPanel formPanel; // Panel derecho para el formulario dinámico

    // Color verde para el fondo
    private static final Color BACKGROUND_COLOR = new Color(184, 211, 173);
    private Database querys;
    private Map<Integer, String> nodosPadresMap = new HashMap<>();
    private Map<Integer, String> nodosHijosMap = new HashMap<>();
    private Map<Integer, String> nombresNodosMap = new HashMap<>();

    //lista para almacenar datos de formularios de creacion de nodos
    private List<String> listaDatosTipoNodo = new ArrayList<>();
    
    private DefaultMutableTreeNode root; // Nodo raíz del árbol
    private JTree tree; // Componente JTree

    // Clase que contiene los formularios respecto al tipo de nodo
    private Forms forms;
    private ActivoFormSimplificadoGetter activoFormSimplificadoGetter;
    private VariablesContextoSimplificadoGetter variablesContextoSimplificadoGetter;
    private SentenciasFormSimplificadoGetter sentenciasFormSimplificadoGetter;

    public Interfaz() 
{
    // Inicializar ActivoFormSimplificadoGetter antes de pasarlo a Forms
    this.activoFormSimplificadoGetter = new ActivoFormSimplificadoGetter();
    this.variablesContextoSimplificadoGetter = new VariablesContextoSimplificadoGetter();
    this.sentenciasFormSimplificadoGetter = new SentenciasFormSimplificadoGetter();
    // Inicializar base de datos y formularios
    this.querys = new Database();
    this.forms = new Forms(activoFormSimplificadoGetter,variablesContextoSimplificadoGetter,sentenciasFormSimplificadoGetter);

    // Configuración inicial de la ventana
    setTitle("Árbol N-ario");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    // Crear el modelo del árbol
    root = new DefaultMutableTreeNode();
    tree = new JTree(root);

    // Configurar el JTree
    tree.setBackground(BACKGROUND_COLOR);
    tree.setFont(new Font("Arial", Font.PLAIN, 30));
    tree.setShowsRootHandles(true);

    // Crear un panel para el árbol y el botón
    JPanel treePanel = new JPanel();
    treePanel.setLayout(new BorderLayout());
    treePanel.setBackground(new Color(184, 211, 173));

    // Añadir el árbol al panel
    JScrollPane treeScrollPane = new JScrollPane(tree);
    treePanel.add(treeScrollPane, BorderLayout.CENTER);

    // Crear el botón "Añadir Nodo"
    JButton btnNuevoNodo = new JButton("Añadir Nodo");
    btnNuevoNodo.setFont(new Font("Arial", Font.BOLD, 16));
    btnNuevoNodo.setBackground(new Color(135, 206, 250)); // Color azul claro
    btnNuevoNodo.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            formPanel.removeAll();
            
            // Cambiar el diseño del panel principal
            formPanel.setLayout(new BorderLayout());
            //formPanel.setBorder(BorderFactory.createTitledBorder("Formulario Nombre y Tipo de Nodo"));
            formPanel.setBackground(new Color(184, 211, 173));

// Crear un panel secundario para los campos generales
JPanel formularioPanel = new JPanel(new GridBagLayout());
formularioPanel.setBackground(new Color(184, 211, 173));
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(10, 10, 10, 10);
gbc.fill = GridBagConstraints.HORIZONTAL;

// Panel dinámico para los formularios específicos
JPanel dynamicFormPanel = new JPanel(new GridBagLayout());
dynamicFormPanel.setBackground(new Color(184, 211, 173));

// Configurar los campos del formulario principal
JLabel lblNombre = new JLabel("Nombre:");
JTextField txtNombre = new JTextField(20);

txtNombre.addKeyListener(new KeyAdapter() 
{
    @Override
    public void keyTyped(KeyEvent e) {
        if (txtNombre.getText().length() >= 50) { // Máximo 10 caracteres
            e.consume();
            Toolkit.getDefaultToolkit().beep();
        }
    }
});

JLabel lblTipoNodo = new JLabel("Tipo de Nodo:");
JComboBox<String> comboTipoNodo = new JComboBox<>(new String[]{"Activos", "Variables", "Sentencias"});

    // Crear el mapa en memoria para almacenar la última opción seleccionada
    Map<Integer, String> mapaOpcionesMemoria = new HashMap<>();
    Map<Integer, String> mapaOpcionesTipoNodo = new HashMap<>();

    // Obtener la opción por defecto
    String selectedOption = (String) comboTipoNodo.getSelectedItem();

    // Almacenar la opción por defecto en el mapa
    mapaOpcionesMemoria.put(1, selectedOption);

    

    // Lógica para mostrar el formulario inicial basado en la opción preseleccionada
    List<String> resultadosTipoNodo = mostrarFormularioSeleccionado(selectedOption, dynamicFormPanel);
    
//en el map debemos insertar con el siguiente orden 1=tipo,2=estado,3=monitor

// Añadir un ActionListener al JComboBox para manejar la selección
comboTipoNodo.addActionListener(event -> {

    // Obtener la nueva opción seleccionada
    String nuevaOpcion = (String) comboTipoNodo.getSelectedItem();
    String opcionAnterior = mapaOpcionesMemoria.get(1);

    // Verificar si la opción ha cambiado
    if (!nuevaOpcion.equals(opcionAnterior)) 
    {
        // Actualizar la memoria con la nueva opción
        mapaOpcionesMemoria.put(1, nuevaOpcion);
        // Mostrar el formulario correspondiente a la nueva opción
        mostrarFormularioSeleccionado(nuevaOpcion, dynamicFormPanel);
    }
    });

    JButton btnGuardar = new JButton("Guardar");
    btnGuardar.addActionListener(event -> {


        // Obtener los valores del formularios
        String nombre = txtNombre.getText();
        String tipoNodo = (String) comboTipoNodo.getSelectedItem();

        //ahora viene la logica de insercion a la base de datoss

        switch (tipoNodo) 
        {
            case "Enlace":
                System.out.println("Opción seleccionada: Enlace.");
                // Aquí puedes agregar un formulario para "Enlace"
                break;

            case "Activos":                                                         
                Map<Integer,String> opcionesNodoActivosMap = new HashMap<>();

                opcionesNodoActivosMap.put(1, activoFormSimplificadoGetter.getTipo());
                opcionesNodoActivosMap.put(2, activoFormSimplificadoGetter.getEstado());
                opcionesNodoActivosMap.put(3, activoFormSimplificadoGetter.getMonitor());

                //tipo,estado,monitor

                Boolean insertOpcionesNodoActivos = querys.insertarNodoArbol(tipoNodo,nombre,opcionesNodoActivosMap);
                if (insertOpcionesNodoActivos) 
                {
                    System.out.println("se inserto correctamente en activos");
                    reloadTreePanel();

                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Guardado exitosamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    //debemos dar aviso de que el nodo se ingreso correctamente y realizar una accion 
                }
                else
                {
                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Ocurrio un error inesperado.",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    System.out.println("No se inserto correctamente en activos");

                    // debemos informar que ocurrio un problema al insertar el nodo a la base de datos
                }
                //forms.activoFormSimplificado(dynamicFormPanel);
                break;

            case "Variables":

                    //logica para permitir abrir y cerrar el formulario
                if(variablesContextoSimplificadoGetter.isDisponibilidadForm() == false)
                {
                            // Mostrar un cuadro de diálogo informativo
                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "No hay nodos activos disponibles. Por favor, agregue nodos activos antes de continuar.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    break; // Detén el flujo aquí si no está disponible

                }
                else
                {
                    Map<Integer,String> opcionesNodoVariablesMap = new HashMap<>();
                    opcionesNodoVariablesMap.put(1,variablesContextoSimplificadoGetter.getTipoVC());
                    opcionesNodoVariablesMap.put(2, variablesContextoSimplificadoGetter.getActivoId().toString());

                    Boolean insertOpcionesNodoVariables = querys.insertarNodoArbol(tipoNodo,nombre,opcionesNodoVariablesMap);
                    if (insertOpcionesNodoVariables) 
                    {
                        //debemos dar aviso de que el nodo se ingreso correctamente y realizar una accion
                        System.out.println("se inserto correctamente en variables");
                        reloadTreePanel();

                        JOptionPane.showMessageDialog(
                            formPanel, // Panel o componente padre
                            "Guardado exitosamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                        );

     
                    }
                    else
                    {

                        // debemos informar que ocurrio un problema al insertar el nodo a la base de datos
                        System.out.println("Ocurrio un problema al insertar el nodo de Variables");
                        JOptionPane.showMessageDialog(
                            formPanel, // Panel o componente padre
                            "Ocurrio un error inesperado.",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE
                        );
    
                    }
    
                    System.out.println("Opción seleccionada: Variables.");
                    //forms.VariablesContextoFormSimplificado(dynamicFormPanel); // Agregar el formulario de "Variables"
                }

                break;

            case "Sentencias":

                Map<Integer,String> opcionesNodoSentenciasMap = new HashMap<>();
                opcionesNodoSentenciasMap.put(1, sentenciasFormSimplificadoGetter.getEstado());
                //opcionesNodoSentenciasMap.put(1, variablesContextoSimplificadoGetter.getActivoId());

                Boolean insertOpcionesNodoSentencias = querys.insertarNodoArbol(tipoNodo,nombre,opcionesNodoSentenciasMap);
                if (insertOpcionesNodoSentencias) 
                {
                    //debemos dar aviso de que el nodo se ingreso3 correctamente y realizar una accion
                    System.out.println("Se inserto correctamente en Sentencias");
                    reloadTreePanel();
                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Guardado exitosamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );                
                }
                else
                {
                    // debemos informar que ocurrio un problema al insertar el nodo a la base de datos
                    System.out.println("Ocurrio un error al insertar en Sentencias");
                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Ocurrio un error inesperado.",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                System.out.println("Opción seleccionada: Sentencias.");
                //forms.SentenciasFormSimplificado(dynamicFormPanel); // Agregar el formulario de "Sentencias"
                break;

            default:
                System.out.println("Opción no reconocida: " + selectedOption);
                break;
        }
        // Mostrar un mensaje en consola con los datos
        System.out.println("Guardando datos...");
    });

    // Añadir componentes al formularioPanel
    gbc.gridx = 0;
    gbc.gridy = 0;
    formularioPanel.add(lblNombre, gbc);

    gbc.gridx = 1;
    formularioPanel.add(txtNombre, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    formularioPanel.add(lblTipoNodo, gbc);

    gbc.gridx = 1;
    formularioPanel.add(comboTipoNodo, gbc);

    // Añadir el dynamicFormPanel al formulario principal
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2; // Hacer que el dynamicFormPanel ocupe todo el ancho
    formularioPanel.add(dynamicFormPanel, gbc);

    // Añadir el botón Guardar al final
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2; // Hacer que el botón esté centrado
    gbc.anchor = GridBagConstraints.CENTER;
    formularioPanel.add(btnGuardar, gbc);

    // Agregar el formulario principal al centro del formPanel
    formPanel.add(formularioPanel, BorderLayout.CENTER);

    // Refrescar el formPanel
    formPanel.revalidate();
    formPanel.repaint();

        }
    }); // Acción al hacer clic
    treePanel.add(btnNuevoNodo, BorderLayout.SOUTH); // Añadir el botón en la parte inferior

    // Configurar el panel derecho para formularios
    formPanel = new JPanel();
    formPanel.setLayout(new BorderLayout()); // Usar BorderLayout para ajustar componentes
    formPanel.setBackground(new Color(184, 211, 173));

    // Dividir la pantalla
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, formPanel);
    splitPane.setDividerLocation(300); // Tamaño inicial del panel izquierdo
    splitPane.setResizeWeight(0.3); // Prioridad al redimensionar
    add(splitPane);

    // Probar la conexión a la base de datos
    try 
    {
        querys.testDatabaseConnection();
    } 
    catch (Exception e) 
    {
        e.printStackTrace();
    }

    // Cargar datos del árbol
    createTreeModel();
    setLocationRelativeTo(null);
}

    private void createTreeModel() 
    {
        List<String> queryGetParents = querys.findNodesParents();

        if (queryGetParents != null && !queryGetParents.isEmpty()) 
        {
            Map<Integer, DefaultMutableTreeNode> mapaNodos = new HashMap<>();

            for (String parentId : queryGetParents) 
            {
                String getNamesFromNodos = querys.getNameFromNodoArbol(Integer.parseInt(parentId));

                nombresNodosMap.put(Integer.parseInt(parentId), getNamesFromNodos);

                //ahora preguntaremos en el map nombresNodosMap por el parentId para obtener el nombre del nodo

                if (!mapaNodos.containsKey(Integer.parseInt(parentId))) 
                {
                        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(nombresNodosMap.get(Integer.parseInt(parentId)));
                        mapaNodos.put(Integer.parseInt(parentId), nodoPadre);
                        root.add(nodoPadre);
                        construirHijos(parentId, nodoPadre, mapaNodos);
                }
            }

            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.reload(root);
        } 
        else 
        {
            System.out.println("No se encontraron nodos padres en la base de datos.");
        }

        tree.addTreeSelectionListener(e -> {
            // Refrescar el formPanel
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            Integer findNodoArbolIdFromName = querys.getNodoArbolIdByName(selectedNode);
            if (selectedNode != null && selectedNode.getUserObject() != null) 
            {

                String tipoNodo = querys.findTypeOfNodo(findNodoArbolIdFromName.toString());
                Integer nodoArbolId = Integer.parseInt(findNodoArbolIdFromName.toString());

                List<String> causeId = querys.getDataForNodoArbol(nodoArbolId);
                
                // Limpiar el panel derecho antes de agregar un nuevo formulario
                formPanel.removeAll();

                if ("Activos".equals(tipoNodo) || "Nodo de activos".equals(tipoNodo)) 
                {
                    forms.activoForm(causeId,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("Sentencias".equals(tipoNodo) || "Nodo de sentencias".equals(tipoNodo)) 
                {
                    forms.SentenciasForm(causeId,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("Variables".equals(tipoNodo) || "Nodo de variables".equals(tipoNodo)) 
                {
                    forms.VariablesContextoForm(causeId,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("enlace".equals(tipoNodo) || "Nodo de enlace".equals(tipoNodo)) 
                {
                    forms.enlaceForm(causeId,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else 
                {
                    System.out.println("Tipo de nodo no coincide con ningún formulario.");
                }

                // Refrescar el panel derecho
                formPanel.revalidate();
                formPanel.repaint();
            }
        });
    }

    private void reloadTreePanel() 
    {
        // Limpiar la raíz del árbol
        root.removeAllChildren();
        
        // Actualizar el modelo del árbol
        createTreeModel();
    
        // Refrescar el árbol en el panel izquierdo
        ((DefaultTreeModel) tree.getModel()).reload();
    
        // Refrescar el componente gráfico
        tree.revalidate();
        tree.repaint();
    }
    

    private void construirHijos(String idNodoPadre, DefaultMutableTreeNode nodoPadre, Map<Integer, DefaultMutableTreeNode> mapaNodos) {
        List<String> hijos = querys.findNodesChildren(idNodoPadre);

        for (String hijoId : hijos) 
        {
            if (!mapaNodos.containsKey(Integer.parseInt(hijoId))) 
            {
                DefaultMutableTreeNode nodoHijo = new DefaultMutableTreeNode(querys.getNameFromNodoArbol(Integer.parseInt(hijoId)));
                nodoPadre.add(nodoHijo);
                mapaNodos.put(Integer.parseInt(hijoId), nodoHijo);

                construirHijos(hijoId, nodoHijo, mapaNodos);
            }
        }
    }

    List<String> resultados;

    public List<String> mostrarFormularioSeleccionado(String selectedOption, JPanel dynamicFormPanel)
    {                                               
        // Limpiar el dynamicFormPanel únicamente
        dynamicFormPanel.removeAll();

        switch (selectedOption) 
        {
            case "Enlace":
                // Aquí puedes agregar un formulario para "Enlace"
                break;

            case "Activos":
                forms.activoFormSimplificado(dynamicFormPanel);
                break;
                    
            case "Variables":
                forms.VariablesContextoFormSimplificado(dynamicFormPanel); // Agregar el formulario de "Variables"
                break;

            case "Sentencias":
                forms.SentenciasFormSimplificado(dynamicFormPanel); // Agregar el formulario de "Sentencias"
                break;

            default:
                System.out.println("Opción no reconocida: " + selectedOption);
                break;
        }

        // Refrescar el dynamicFormPanel
        dynamicFormPanel.revalidate();
        dynamicFormPanel.repaint();

        return resultados;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz treeExample = new Interfaz();
            treeExample.setVisible(true);
        });
    }
}

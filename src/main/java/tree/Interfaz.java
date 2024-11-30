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
import tree.getters.ConfigurationGetter;
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
    private String selectedOption;

    private String nodoNombreRaiz = "Raíz";


    //lista para almacenar datos de formularios de creacion de nodos
    private List<String> listaDatosTipoNodo = new ArrayList<>();
    private DefaultMutableTreeNode root; // Nodo raíz del árbol
    private JTree tree; // Componente JTree
    private DefaultTreeModel treeModel;


    // Clase que contiene los formularios respecto al tipo de nodo
    private Forms forms;
    private ActivoFormSimplificadoGetter activoFormSimplificadoGetter;
    private VariablesContextoSimplificadoGetter variablesContextoSimplificadoGetter;
    private SentenciasFormSimplificadoGetter sentenciasFormSimplificadoGetter;
    private ConfigurationGetter configurationGetter;


    public Interfaz() 
{
    // Inicializar ActivoFormSimplificadoGetter antes de pasarlo a Forms
    this.activoFormSimplificadoGetter = new ActivoFormSimplificadoGetter();
    this.variablesContextoSimplificadoGetter = new VariablesContextoSimplificadoGetter();
    this.sentenciasFormSimplificadoGetter = new SentenciasFormSimplificadoGetter();
    this.configurationGetter = new ConfigurationGetter();
    // Inicializar base de datos y formularios
    this.querys = new Database();
    this.forms = new Forms(this,activoFormSimplificadoGetter,variablesContextoSimplificadoGetter,sentenciasFormSimplificadoGetter,configurationGetter);

    // Configuración inicial de la ventana
    setTitle("Árbol N-ario");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    // Crear el modelo del árbol
    //root = new DefaultMutableTreeNode();
    //tree = new JTree(root);

    // Crear el modelo del árbol
    root = new DefaultMutableTreeNode(nodoNombreRaiz); // Inicializa root con un nombre
    treeModel = new DefaultTreeModel(root); // Inicializa treeModel con root
    tree = new JTree(treeModel); // Crea el JTree utilizando treeModel

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

    // Crear un panel para organizar los botones en vertical
    JPanel botonesPanel = new JPanel();
    botonesPanel.setLayout(new GridBagLayout()); // Diseño flexible para organizar botones
    botonesPanel.setBackground(new Color(184, 211, 173)); // Fondo consistente

    // Configurar GridBagConstraints para el diseño
    GridBagConstraints gbcBotones = new GridBagConstraints();
    gbcBotones.insets = new Insets(5, 5, 5, 5); // Margen entre botones
    gbcBotones.fill = GridBagConstraints.HORIZONTAL; // Ocupa todo el ancho disponible
    
    JButton btnCambiarNombreRaiz = new JButton("Cambiar nombre nodo raiz");
    btnCambiarNombreRaiz.setFont(new Font("Arial", Font.BOLD, 16));
    btnCambiarNombreRaiz.setBackground(new Color(135, 206, 250)); // Color azul claro

    // Añadir el botón "Cambiar nombre nodo raíz" al panel
    gbcBotones.gridx = 0; // Primera columna
    gbcBotones.gridy = 0; // Primera fila
    botonesPanel.add(btnCambiarNombreRaiz, gbcBotones);

    btnCambiarNombreRaiz.addActionListener(new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            selectedOption = "Nodo raiz configuracion";
            
            // Llamar al método loloForms
            mostrarFormularioSeleccionado(selectedOption, formPanel);
            reloadTreePanel();
        }
    });


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


// Crear un panel secundario para añadir nodo raiz
JPanel formularioPanel1 = new JPanel(new GridBagLayout());
formularioPanel1.setBackground(new Color(184, 211, 173));
GridBagConstraints gbc1 = new GridBagConstraints();
gbc1.insets = new Insets(10, 10, 10, 10);
gbc1.fill = GridBagConstraints.HORIZONTAL;


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
    selectedOption = ((String) comboTipoNodo.getSelectedItem()).toLowerCase();

    // Almacenar la opción por defecto en el mapa
    mapaOpcionesMemoria.put(1, selectedOption);

    // Lógica para mostrar el formulario inicial basado en la opción preseleccionada
    List<String> resultadosTipoNodo = mostrarFormularioSeleccionado(selectedOption, dynamicFormPanel);
    
//en el map debemos insertar con el siguiente orden 1=tipo,2=estado,3=monitor

// Añadir un ActionListener al JComboBox para manejar la selección
comboTipoNodo.addActionListener(event -> {

    // Obtener la nueva opción seleccionada
    String nuevaOpcion = ((String) comboTipoNodo.getSelectedItem()).toLowerCase();
    String opcionAnterior = mapaOpcionesMemoria.get(1).toLowerCase();    

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
        String tipoNodo = ((String) comboTipoNodo.getSelectedItem()).toLowerCase();

        //ahora viene la logica de insercion a la base de datoss

        switch (tipoNodo) 
        {
            case "enlace":
                System.out.println("Opción seleccionada: Enlace.");
                // Aquí puedes agregar un formulario para "Enlace"
                break;

            case "activos":                                                         
                Map<Integer,String> opcionesNodoActivosMap = new HashMap<>();

                opcionesNodoActivosMap.put(1, activoFormSimplificadoGetter.getTipo());
                opcionesNodoActivosMap.put(2, activoFormSimplificadoGetter.getEstado());
                opcionesNodoActivosMap.put(3, activoFormSimplificadoGetter.getMonitor());

                //tipo,estado,monitor

                Boolean insertOpcionesNodoActivos = querys.insertarNodoArbol(tipoNodo,nombre,opcionesNodoActivosMap);
                if (insertOpcionesNodoActivos) 
                {
                    reloadTreePanel();

                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Guardado exitosamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    //debemos dar aviso de que el nodo se ingreso correctamente y realizar una accion
                        // Remover el formulario del formPanel
                    formPanel.removeAll();
                    formPanel.revalidate();
                    formPanel.repaint();

                }
                else
                {
                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Ocurrio un error inesperado.",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                }
                //forms.activoFormSimplificado(dynamicFormPanel);
                break;

            case "variables":

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
                        reloadTreePanel();

                        JOptionPane.showMessageDialog(
                            formPanel, // Panel o componente padre
                            "Guardado exitosamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        formPanel.removeAll();
                        formPanel.revalidate();
                        formPanel.repaint();
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

            case "sentencias":

                Map<Integer,String> opcionesNodoSentenciasMap = new HashMap<>();
                opcionesNodoSentenciasMap.put(1, sentenciasFormSimplificadoGetter.getEstado());
                //opcionesNodoSentenciasMap.put(1, variablesContextoSimplificadoGetter.getActivoId());

                Boolean insertOpcionesNodoSentencias = querys.insertarNodoArbol(tipoNodo,nombre,opcionesNodoSentenciasMap);
                if (insertOpcionesNodoSentencias) 
                {
                    //debemos dar aviso de que el nodo se ingreso3 correctamente y realizar una accion
                    reloadTreePanel();
                    JOptionPane.showMessageDialog(
                        formPanel, // Panel o componente padre
                        "Guardado exitosamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    ); 
                    
                    formPanel.removeAll();
                    formPanel.revalidate();
                    formPanel.repaint();
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
    }); 

    gbcBotones.gridy = 1; // Segunda fila
    botonesPanel.add(btnNuevoNodo, gbcBotones);

    // Añadir el panel de botones al panel del árbol
    treePanel.add(botonesPanel, BorderLayout.SOUTH);
    

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

public void actualizarNombreNodoRaiz() 
{
    String nuevoNombre = configurationGetter.getNodoNombreRaiz();
    if (nuevoNombre == null || nuevoNombre.isEmpty()) 
    {
        nuevoNombre = "Raíz"; // Valor predeterminado si el nombre es nulo o vacío
    }
    root.setUserObject(nuevoNombre);
    treeModel.nodeChanged(root);
    tree.repaint();
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

                // Verificar si el nodo seleccionado es el nodo raíz

                if (selectedNode == null) 
                {
                    // No se ha seleccionado ningún nodo
                    return;
                }
            
                // Verificar si el nodo seleccionado es el nodo raíz
                if (selectedNode == root) 
                {
                    // Si es el nodo raíz, deseleccionar el nodo
                    tree.clearSelection();
                    System.out.println("La selección del nodo raíz no está permitida.");
                    return; // Salir del método
                }
            
                // Ahora, selectedNode no es null y no es root
                if (selectedNode.getUserObject() == null) 
                {
                    System.out.println("El nodo seleccionado no tiene un objeto de usuario.");
                    return;
                }

            Integer findNodoArbolIdFromName = querys.getNodoArbolIdByName(selectedNode);
            if (selectedNode != null && selectedNode.getUserObject() != null) 
            {

                String tipoNodo = querys.findTypeOfNodo(findNodoArbolIdFromName.toString());
                Integer nodoArbolId = findNodoArbolIdFromName;


                List<String> dataFromNodo = querys.getDataForNodoArbol(nodoArbolId);
                
                // Limpiar el panel derecho antes de agregar un nuevo formulario
                formPanel.removeAll();

                if ("activos".equals(tipoNodo) || "nodo de activos".equals(tipoNodo)) 
                {

                    if (dataFromNodo.isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(
                            formPanel, // Panel o componente padre
                            "No se encontraron datos en la tabla de activos asociada al nodo seleccionado.\n"
                            + "Por favor, verifique la información del nodo e ingrese los datos correspondientes.",
                            "Advertencia",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    forms.activoForm(dataFromNodo,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("sentencias".equals(tipoNodo) || "nodo de sentencias".equals(tipoNodo)) 
                {
                    if (dataFromNodo.isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(
                            formPanel, // Panel o componente padre
                            "No se encontraron datos en la tabla de sentencias asociada al nodo seleccionado.\n"
                            + "Por favor, verifique la información del nodo e ingrese los datos correspondientes.",
                            "Advertencia",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        return;

                    }

                    forms.SentenciasForm(dataFromNodo,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("variables".equals(tipoNodo) || "nodo de variables".equals(tipoNodo)) 
                {

                    if (dataFromNodo.isEmpty()) 
                    {
                        JOptionPane.showMessageDialog(
                            formPanel, // Panel o componente padre
                            "No se encontraron datos en la tabla de variablescontexto asociada al nodo seleccionado.\n"
                            + "Por favor, verifique la información del nodo e ingrese los datos correspondientes.",
                            "Advertencia",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                        return;

                    }
                    forms.VariablesContextoForm(dataFromNodo,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("enlace".equals(tipoNodo) || "nodo de enlace".equals(tipoNodo)) 
                {
                    forms.enlaceForm(dataFromNodo,nodoArbolId);
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

    public void reloadTreePanel() 
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
        dynamicFormPanel.removeAll(); // Elimina todos los componentes anteriores
        dynamicFormPanel.setBorder(null); // Elimina cualquier borde o título anterior

        switch (selectedOption) 
        {
            case "enlace":
                // Aquí puedes agregar un formulario para "Enlace"
                break;

            case "activos":
                forms.activoFormSimplificado(dynamicFormPanel);
                break;
                    
            case "variables":
                forms.VariablesContextoFormSimplificado(dynamicFormPanel); // Agregar el formulario de "Variables"
                break;

            case "sentencias":
                forms.SentenciasFormSimplificado(dynamicFormPanel); // Agregar el formulario de "Sentencias"
                break;

            case "Nodo raiz configuracion":
                forms.configuracionNodoRaiz(dynamicFormPanel);
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

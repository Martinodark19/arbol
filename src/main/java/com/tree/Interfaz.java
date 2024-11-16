package com.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.tree.CapaNegocio.Database;
import com.tree.CapaNegocio.Forms;

public class Interfaz extends JFrame 
{

    // Color verde para el fondo
    private static final Color BACKGROUND_COLOR = new Color(184, 211, 173);
    private Database querys;
    // map para almacenar los id de los nodos padres
    private Map<Integer,String> nodosPadresMap = new HashMap<>();

    // map para almacenar los id de los nodos hijos
    private Map<Integer,String> nodosHijosMap = new HashMap<>();

    private DefaultMutableTreeNode root; // Nodo raíz del árbol
    private JTree tree; // Componente JTree

    //Inicializar nodo padre
    private DefaultMutableTreeNode nodosPadres;
    
    private DefaultMutableTreeNode nodosHijos;

    //clase que contiene los formularios respecto al tipo de nodo
    private Forms forms;

    public Interfaz(Database querys) 
    {   
        // Llama a ObtenerNodosPadres para cargar los datos en el árbol
        this.querys = querys;
        this.forms = new Forms(); // Inicializar instancia de Forms


        // Cambiar el fondo del JFrame
        getContentPane().setBackground(new Color(184, 211, 173));
        setTitle("Árbol N-ario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        // Crear el modelo del árbol
        root = new DefaultMutableTreeNode();

        // Crear el componente JTree
        tree = new JTree(root);

        // Cambiar el fondo del JTree
        tree.setBackground(BACKGROUND_COLOR);
        tree.setFont(new Font("Arial", Font.PLAIN, 30)); // Cambiar tamaño a 16


        // Configuración estética (opcional)
        tree.setShowsRootHandles(true); // Mostrar íconos de expandir/colapsar
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);


        add(scrollPane);
        setLocationRelativeTo(null);

        // Probar la conexión a la base de datos
        try 
        {
            querys.testDatabaseConnection();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        ObtenerNodosPadres();
        createTreeModel();
    }

    private void ObtenerNodosPadres() 
    {   
            // llamar a la base de datos
            List<String> queryGetParents = querys.findNodesParents();
        
            if(!queryGetParents.isEmpty() && queryGetParents != null)
            {
                for(String parent: queryGetParents)
                {

                    for (int i = 0; i < 10; i++) 
                    {

                        if (!nodosPadresMap.containsValue(parent)) 
                        {
                            nodosPadresMap.put(i, parent);
                        }
                    }
                 
                }   
            }
            else
            {
             System.out.println(" No existieron nodos padres en la base de datos");
            }
    }

    private void createTreeModel() 
    {
        // Obtener nodos padres
        List<String> queryGetParents = querys.findNodesParents();
    
        if (queryGetParents != null && !queryGetParents.isEmpty()) 
        {
            Map<Integer, DefaultMutableTreeNode> mapaNodos = new HashMap<>();
    
            // Procesar nodos padres
            for (String parentId : queryGetParents) 
            {
                if (!mapaNodos.containsKey(Integer.parseInt(parentId))) {
                    DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(parentId);
                    mapaNodos.put(Integer.parseInt(parentId), nodoPadre);
                    root.add(nodoPadre);
    
                    // Obtener nodos hijos para este padre
                    construirHijos(parentId, nodoPadre, mapaNodos);
                }
            }
    
            // Notificar cambios al modelo del árbol
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.reload(root);
        } 
        else 
        {
            System.out.println("No se encontraron nodos padres en la base de datos.");
        }


        // En el árbol (JTree), agregar un listener de mouse o selección
        tree.addTreeSelectionListener(e -> {
            // Obtener el nodo seleccionado
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (selectedNode != null) 
            {

                // Cerrar formularios existentes antes de abrir uno nuevo
                if (forms.isVisible()) 
                {
                    forms.dispose();
                }

                String tipoNodo = querys.findTypeOfNodo(selectedNode.getUserObject().toString());
        
        // Crear una nueva instancia del formulario adecuado según el tipo de nodo
        if ("Activos".equals(tipoNodo) || "Nodo de activos".equals(tipoNodo)) 
        {
            Forms activoForm = new Forms();
            activoForm.activoForm(); // Configurar el formulario
            activoForm.setVisible(true); // Mostrar el formulario
        } 
        else if ("sentencias".equals(tipoNodo) || "Nodo de sentencias".equals(tipoNodo)) 
        {
            Forms sentenciasForm = new Forms();
            sentenciasForm.SentenciasForm(); // Configurar el formulario
            sentenciasForm.setVisible(true); // Mostrar el formulario
        } 
        else if ("variables".equals(tipoNodo) || "Nodo de variables".equals(tipoNodo)) 
        {
            Forms variablesForm = new Forms();
            variablesForm.VariablesContextoForm(); // Configurar el formulario
            variablesForm.setVisible(true); // Mostrar el formulario
        } 
        else 
        {
            System.out.println("Tipo de nodo no coincide con ningún formulario.");
        }

            }
        });

    }
    
    private void construirHijos(String idNodoPadre, DefaultMutableTreeNode nodoPadre, Map<Integer, DefaultMutableTreeNode> mapaNodos) 
    {
        // Obtener los hijos del nodo actual
        List<String> hijos = querys.findNodesChildren(idNodoPadre);
    
        for (String hijoId : hijos) {
            if (!mapaNodos.containsKey(Integer.parseInt(hijoId))) 
            {
                DefaultMutableTreeNode nodoHijo = new DefaultMutableTreeNode(hijoId);
                nodoPadre.add(nodoHijo);
                mapaNodos.put(Integer.parseInt(hijoId), nodoHijo);
    
                // Verificar si este hijo tiene más hijos
                construirHijos(hijoId, nodoHijo, mapaNodos);
            }
        }
    }

    public static void main(String[] args) 
    {
        
        // Crear una instancia de Querys
        Database querys = new Database(); 
        SwingUtilities.invokeLater(() -> {
            Interfaz treeExample = new Interfaz(querys);
            treeExample.setVisible(true);
        });
    }
}

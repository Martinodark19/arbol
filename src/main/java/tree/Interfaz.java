package tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Interfaz extends JFrame 
{
    private JPanel formPanel; // Panel derecho para el formulario dinámico

    // Color verde para el fondo
    private static final Color BACKGROUND_COLOR = new Color(184, 211, 173);
    private Database querys;
    private Map<Integer, String> nodosPadresMap = new HashMap<>();
    private Map<Integer, String> nodosHijosMap = new HashMap<>();

    private DefaultMutableTreeNode root; // Nodo raíz del árbol
    private JTree tree; // Componente JTree

    // Clase que contiene los formularios respecto al tipo de nodo
    private Forms forms;

    public Interfaz() 
    {
        // Inicializar base de datos y formularios
        this.querys = new Database();
        this.forms = new Forms();

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

        // Configurar panel derecho para formularios
        formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout()); // Usar BorderLayout para ajustar componentes
        formPanel.setBackground(new Color(184, 211, 173));

        // Dividir la pantalla
        JScrollPane treeScrollPane = new JScrollPane(tree);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, formPanel);
        splitPane.setDividerLocation(300); // Tamaño inicial del panel izquierdo
        splitPane.setResizeWeight(0.3); // Prioridad al redimensionar
        add(splitPane);

        // Probar la conexión a la base de datos
        try {
            querys.testDatabaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cargar datos del árbol
        ObtenerNodosPadres();
        createTreeModel();

        setLocationRelativeTo(null);
    }

    private void ObtenerNodosPadres() {
        List<String> queryGetParents = querys.findNodesParents();

        if (queryGetParents != null && !queryGetParents.isEmpty()) {
            for (String parent : queryGetParents) {
                if (!nodosPadresMap.containsValue(parent)) {
                    nodosPadresMap.put(nodosPadresMap.size(), parent);
                }
            }
        } else {
            System.out.println("No existieron nodos padres en la base de datos");
        }
    }

    private void createTreeModel() {
        List<String> queryGetParents = querys.findNodesParents();

        if (queryGetParents != null && !queryGetParents.isEmpty()) {
            Map<Integer, DefaultMutableTreeNode> mapaNodos = new HashMap<>();

            for (String parentId : queryGetParents) {
                if (!mapaNodos.containsKey(Integer.parseInt(parentId))) {
                    DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(parentId);
                    mapaNodos.put(Integer.parseInt(parentId), nodoPadre);
                    root.add(nodoPadre);

                    construirHijos(parentId, nodoPadre, mapaNodos);
                }
            }

            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.reload(root);
        } else {
            System.out.println("No se encontraron nodos padres en la base de datos.");
        }

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (selectedNode != null && selectedNode.getUserObject() != null) {
                String tipoNodo = querys.findTypeOfNodo(selectedNode.getUserObject().toString());
                Integer nodoArbolId = Integer.parseInt(selectedNode.getUserObject().toString());

                List<String> causeId = querys.getDataForNodoArbol(nodoArbolId);
                
                // Limpiar el panel derecho antes de agregar un nuevo formulario
                formPanel.removeAll();

                if ("activos".equals(tipoNodo) || "Nodo de activos".equals(tipoNodo)) 
                {
                    forms.activoForm(causeId,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("sentencias".equals(tipoNodo) || "Nodo de sentencias".equals(tipoNodo)) 
                {
                    forms.SentenciasForm(causeId,nodoArbolId);
                    formPanel.add(forms.getPanel(), BorderLayout.CENTER);
                } 
                else if ("variables".equals(tipoNodo) || "Nodo de variables".equals(tipoNodo)) 
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

    private void construirHijos(String idNodoPadre, DefaultMutableTreeNode nodoPadre, Map<Integer, DefaultMutableTreeNode> mapaNodos) {
        List<String> hijos = querys.findNodesChildren(idNodoPadre);

        for (String hijoId : hijos) {
            if (!mapaNodos.containsKey(Integer.parseInt(hijoId))) {
                DefaultMutableTreeNode nodoHijo = new DefaultMutableTreeNode(hijoId);
                nodoPadre.add(nodoHijo);
                mapaNodos.put(Integer.parseInt(hijoId), nodoHijo);

                construirHijos(hijoId, nodoHijo, mapaNodos);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz treeExample = new Interfaz();
            treeExample.setVisible(true);
        });
    }
}

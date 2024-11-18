package com.tree.CapaNegocio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Forms extends JFrame 
{

    //Variable enlace form
    private JComboBox<String> comboTipoNodo;        // Selección del tipo de nodo
    private JComboBox<String> comboTipoExpresion;  // Selección del tipo de expresión
    private JComboBox<String> comboOpcionTF;       // Selección de opción T/F
    private JButton btnGuardar;                    // Botón de guardar


    // Variables formulario Activo
    private JComboBox<String> comboTipo;      // Lista desplegable para tipo
    private JComboBox<String> comboEstado;   // Lista desplegable para estado
    private JComboBox<String> comboMonitor;  // Lista desplegable para monitor (T/F)
    private JButton btnGuardarA;              // Botón de guardar


    // Variables formulario VariablesContexto
    private JTextField txtTipoVC;
    private JComboBox<String> txtActivoId;
    private JButton btnGuardarVC;

    // Variables formulario Sentencias
    private JTextField txtEstadoS;
    private JButton btnGuardarS;


    public void enlaceForm(List<String> informationActivosToShow) 
    {
        if (getContentPane().getComponentCount() > 0) {
            // Si ya hay componentes en el formulario, no volver a añadirlos
            return;
        }

        setTitle("Formulario Nodo de Enlace");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(4, 2, 5, 5)); // Formato de filas y columnas

        // Inicializar componentes
        comboTipoNodo = new JComboBox<>(new String[]{"Activos", "Variables", "Sentencias"});
        comboTipoExpresion = new JComboBox<>(new String[]{"Boolean", "Aritmética"});
        comboOpcionTF = new JComboBox<>(new String[]{"T", "F"});
        btnGuardar = new JButton("Guardar");

        // Añadir etiquetas y campos al formulario
        add(new JLabel("Tipo de Nodo:"));
        add(comboTipoNodo);

        add(new JLabel("Tipo de Expresión:"));
        add(comboTipoExpresion);

        add(new JLabel("Opción (T/F):"));
        add(comboOpcionTF);

        add(btnGuardar);

        // Acción al hacer clic en el botón Guardar
        btnGuardar.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                saveDataNodoEnlace();
            }
        });

        setLocationRelativeTo(null);
    }

    private void saveDataNodoEnlace() 
    {
        // Obtener los valores seleccionados de los combos
        String tipoNodo = (String) comboTipoNodo.getSelectedItem();
        String tipoExpresion = (String) comboTipoExpresion.getSelectedItem();
        String opcionTF = (String) comboOpcionTF.getSelectedItem();

        // Mostrar los datos por consola (puedes adaptarlo para guardarlos en la base de datos)
        System.out.println("Datos del Nodo de Enlace:");
        System.out.println("Tipo de Nodo: " + tipoNodo);
        System.out.println("Tipo de Expresión: " + tipoExpresion);
        System.out.println("Opción (T/F): " + opcionTF);

        dispose();
    }

    public void activoForm(List<String> informationActivosToShow) 
    {
        if (getContentPane().getComponentCount() > 0) {
            // Si ya hay componentes en el formulario, no volver a añadirlos
            return;
        }

        setTitle("Formulario Nodo de Activos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridLayout(7, 2, 5, 5)); // Formato de filas y columnas

        // Inicializar listas predefinidas
        String[] tipos = {"Hardware", "Software", "Infraestructura", "Red", "Otro"};
        String[] estados = {"Operativo", "Inactivo", "En Reparación", "Retirado", "Desconocido"};
        String[] monitorValores = {"T", "F"};

        // Inicializar componentes
        comboTipo = new JComboBox<>(tipos);
        comboEstado = new JComboBox<>(estados);
        comboMonitor = new JComboBox<>(monitorValores);

        
        btnGuardarA = new JButton("Guardar");

        // Añadir etiquetas y campos al formulario
        add(new JLabel("Tipo:"));
        add(comboTipo);

        add(new JLabel("Estado:"));
        add(comboEstado);

        add(new JLabel("Monitor (T/F):"));
        add(comboMonitor);

        add(btnGuardarA);

        // Acción al hacer clic en el botón Guardar
        btnGuardarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                saveDataNodoActivos();
            }
        });

        setLocationRelativeTo(null);
    }
    

    private void saveDataNodoActivos() 
    {
        // Obtener los valores seleccionados de las listas desplegables
        String tipo = (String) comboTipo.getSelectedItem();
        String estado = (String) comboEstado.getSelectedItem();
        String monitor = (String) comboMonitor.getSelectedItem();


        dispose();
    }

    // Métodos similares para `VariablesContextoForm` y `SentenciasForm`
    public void VariablesContextoForm(List<String> informationVariablesToShow) 
    {

        if (getContentPane().getComponentCount() > 0) {
            // Si ya hay componentes en el formulario, no volver a añadirlos
            return;
        }
        setTitle("Formulario Variables Contexto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new GridLayout(18, 2, 5, 5)); // 18 filas, 2 columnas, espacio entre componentes

        String[] tiposActivoId = {"ActivoId 1", "ActivoId 2", "ActivoId 3", "ActivoId 4", "ActivoId 5"};

        txtTipoVC = new JTextField();
        txtActivoId = new JComboBox<>(tiposActivoId);

        

        btnGuardarVC = new JButton("Guardar");


        if (informationVariablesToShow.isEmpty()) 
        {
            System.out.println("Array vacío, se configuran valores predeterminados.");

            txtTipoVC.setText(""); // Tipo predeterminado
        } 
        else 
        {
            System.out.println("Array lleno, cargando valores desde el array.");

            txtTipoVC.setText(informationVariablesToShow.get(3)); 

        }
        
        // Añadir etiquetas y campos al formulario


        add(new JLabel("Tipo:"));
        add(txtTipoVC);

        add(new JLabel("ActivoId:"));
        add(txtActivoId);

        add(btnGuardarVC);

        // Acción al hacer clic en el botón Guardar
        btnGuardarVC.addActionListener(e -> saveDataVariablesContexto());

        setLocationRelativeTo(null); // Centrar en pantalla
        //setVisible(true); // Hacer visible la ventana
    }

    private void saveDataVariablesContexto() 
    {
        // Recoger los datos ingresados en el formulario

        String tipo = txtTipoVC.getText();
        String activoId = (String) txtActivoId.getSelectedItem();

        // Mostrar los datos por consola (puedes adaptarlo para guardarlos en la base de datos)
        System.out.println("Datos de Variables Contexto:");

        System.out.println("Tipo: " + tipo);
        System.out.println("activoId: " + activoId);

        // Cerrar el formulario
        dispose();
    }

    public void SentenciasForm(List<String> informationSentenciasToShow) 
    {
        System.out.println(informationSentenciasToShow + "llego a sentencias");
        if (getContentPane().getComponentCount() > 0) 
        {
            // Si ya hay componentes en el formulario, no volver a añadirlos
            return;
        }

        setTitle("Formulario Sentencias");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridLayout(7, 2, 5, 5)); // 7 filas, 2 columnas, espacio entre componentes

        // Inicializar componentes

        txtEstadoS = new JTextField();

        btnGuardarS = new JButton("Guardar");

        if (informationSentenciasToShow.isEmpty()) 
        {
            System.out.println("Array vacío, se configuran valores predeterminados.");
            txtEstadoS.setText(""); // Ejemplo de Estado
        } 
        else 
        {
            System.out.println("Array lleno, cargando valores desde el array.");
            txtEstadoS.setText(informationSentenciasToShow.get(3)); 
        }
        // Añadir etiquetas y campos al formulario



        add(new JLabel("Estado:"));
        add(txtEstadoS);

        add(btnGuardarS);

        // Acción al hacer clic en el botón Guardar
        btnGuardarS.addActionListener(e -> saveDataSentencias());

        setLocationRelativeTo(null); // Centrar en pantalla
        //setVisible(true); // Hacer visible la ventana
    }

    private void saveDataSentencias() 
    {
        // Recoger los datos ingresados en el formulario
        String estado = txtEstadoS.getText();

        // Mostrar los datos por consola (puedes adaptarlo para guardarlos en la base de datos)
        System.out.println("Datos de Sentencias:");
        System.out.println("Estado: " + estado);

        dispose();
    }
}

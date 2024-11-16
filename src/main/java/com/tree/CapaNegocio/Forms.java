package com.tree.CapaNegocio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Forms extends JFrame 
{

    // Variables formulario Activo
    private JTextField txtActivoIdA;
    private JTextField txtNombreA;
    private JTextField txtClienteIdA;
    private JTextField txtOperacionIdA;
    private JTextField txtSitioIdA;
    private JTextField txtProcesoIdA;
    private JTextField txtTipoA;
    private JTextField txtEstadoA;
    private JTextField txtMonitorA;
    private JTextField txtDocEstadoA;
    private JTextField txtOperacionIdOperacionesA;
    private JTextField txtSitioIdSitiosA;
    private JButton btnGuardarA;

    // Variables formulario VariablesContexto
    private JTextField txtVariableIdVC;
    private JTextField txtNombreVC;
    private JTextField txtComponenteIdVC;
    private JTextField txtTipoVC;
    private JTextField txtLargoVC;
    private JTextField txtValorEnteroVC;
    private JTextField txtValorRealVC;
    private JTextField txtVectorEnteroVC;
    private JTextField txtVectorStringVC;
    private JTextField txtLocalizacionIdVC;
    private JTextField txtActiviIdVC;
    private JTextField txtLocalizacionIdLocalizacionVC;
    private JTextField txtAsociacionIdVC;
    private JTextField txtValorStringVC;
    private JTextField txtAsociacionIdAsociacionesVC;
    private JTextField txtComponenteIdComponentesActivosVC;
    private JTextField txtExpresionIdExpresionesVC;
    private JButton btnGuardarVC;

    // Variables formulario Sentencias
    private JTextField txtSentenciaIdS;
    private JTextField txtNombreS;
    private JTextField txtQueryS;
    private JTextField txtEstadoS;
    private JTextField txtFechaAprobacionS;
    private JTextField txtAlertaIdAlertasDefinicionS;
    private JButton btnGuardarS;

    public void activoForm() 
    {
        if (getContentPane().getComponentCount() > 0) 
        {
            // Si ya hay componentes en el formulario, no volver a añadirlos
            return;
        }

        setTitle("Formulario Activo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridLayout(13, 2, 5, 5));

        // Inicializar componentes
        txtActivoIdA = new JTextField();
        txtNombreA = new JTextField();
        txtClienteIdA = new JTextField();
        txtOperacionIdA = new JTextField();
        txtSitioIdA = new JTextField();
        txtProcesoIdA = new JTextField();
        txtTipoA = new JTextField();
        txtEstadoA = new JTextField();
        txtMonitorA = new JTextField();
        txtDocEstadoA = new JTextField();
        txtOperacionIdOperacionesA = new JTextField();
        txtSitioIdSitiosA = new JTextField();
        btnGuardarA = new JButton("Guardar");

        // Añadir etiquetas y campos al formulario
        add(new JLabel("Activo ID:"));
        add(txtActivoIdA);

        add(new JLabel("Nombre:"));
        add(txtNombreA);

        add(new JLabel("Cliente ID:"));
        add(txtClienteIdA);

        add(new JLabel("Operacion ID:"));
        add(txtOperacionIdA);

        add(new JLabel("Sitio ID:"));
        add(txtSitioIdA);

        add(new JLabel("Proceso ID:"));
        add(txtProcesoIdA);

        add(new JLabel("Tipo:"));
        add(txtTipoA);

        add(new JLabel("Estado:"));
        add(txtEstadoA);

        add(new JLabel("Monitor (T/F):"));
        add(txtMonitorA);

        add(new JLabel("Doc Estado:"));
        add(txtDocEstadoA);

        add(new JLabel("Operacion ID Operaciones:"));
        add(txtOperacionIdOperacionesA);

        add(new JLabel("Sitio ID Sitios:"));
        add(txtSitioIdSitiosA);

        add(btnGuardarA);

        // Acción al hacer clic en el botón Guardar
        btnGuardarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataActivo();
            }
        });

        setLocationRelativeTo(null);
    }

    private void saveDataActivo() 
    {
        String activoId = txtActivoIdA.getText();
        String nombre = txtNombreA.getText();
        String clienteId = txtClienteIdA.getText();
        String operacionId = txtOperacionIdA.getText();
        String sitioId = txtSitioIdA.getText();
        String procesoId = txtProcesoIdA.getText();
        String tipo = txtTipoA.getText();
        String estado = txtEstadoA.getText();
        String monitor = txtMonitorA.getText();
        String docEstado = txtDocEstadoA.getText();
        String operacionIdOperaciones = txtOperacionIdOperacionesA.getText();
        String sitioIdSitios = txtSitioIdSitiosA.getText();

        System.out.println("Datos del Activo:");
        System.out.println("Activo ID: " + activoId);
        System.out.println("Nombre: " + nombre);
        System.out.println("Cliente ID: " + clienteId);
        System.out.println("Operacion ID: " + operacionId);
        System.out.println("Sitio ID: " + sitioId);
        System.out.println("Proceso ID: " + procesoId);
        System.out.println("Tipo: " + tipo);
        System.out.println("Estado: " + estado);
        System.out.println("Monitor: " + monitor);
        System.out.println("Doc Estado: " + docEstado);
        System.out.println("Operacion ID Operaciones: " + operacionIdOperaciones);
        System.out.println("Sitio ID Sitios: " + sitioIdSitios);

        // Cerrar el formulario
        dispose();
    }

    // Métodos similares para `VariablesContextoForm` y `SentenciasForm`
    public void VariablesContextoForm() {

        if (getContentPane().getComponentCount() > 0) {
            // Si ya hay componentes en el formulario, no volver a añadirlos
            return;
        }
        setTitle("Formulario Variables Contexto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new GridLayout(18, 2, 5, 5)); // 18 filas, 2 columnas, espacio entre componentes

        // Inicializar componentes
        txtVariableIdVC = new JTextField();
        txtNombreVC = new JTextField();
        txtComponenteIdVC = new JTextField();
        txtTipoVC = new JTextField();
        txtLargoVC = new JTextField();
        txtValorEnteroVC = new JTextField();
        txtValorRealVC = new JTextField();
        txtVectorEnteroVC = new JTextField();
        txtVectorStringVC = new JTextField();
        txtLocalizacionIdVC = new JTextField();
        txtActiviIdVC = new JTextField();
        txtLocalizacionIdLocalizacionVC = new JTextField();
        txtAsociacionIdVC = new JTextField();
        txtValorStringVC = new JTextField();
        txtAsociacionIdAsociacionesVC = new JTextField();
        txtComponenteIdComponentesActivosVC = new JTextField();
        txtExpresionIdExpresionesVC = new JTextField();
        btnGuardarVC = new JButton("Guardar");

        // Añadir etiquetas y campos al formulario
        add(new JLabel("Variable ID:"));
        add(txtVariableIdVC);

        add(new JLabel("Nombre:"));
        add(txtNombreVC);

        add(new JLabel("Componente ID:"));
        add(txtComponenteIdVC);

        add(new JLabel("Tipo:"));
        add(txtTipoVC);

        add(new JLabel("Largo:"));
        add(txtLargoVC);

        add(new JLabel("Valor Entero:"));
        add(txtValorEnteroVC);

        add(new JLabel("Valor Real:"));
        add(txtValorRealVC);

        add(new JLabel("Vector Entero:"));
        add(txtVectorEnteroVC);

        add(new JLabel("Vector String:"));
        add(txtVectorStringVC);

        add(new JLabel("Localización ID:"));
        add(txtLocalizacionIdVC);

        add(new JLabel("Activi ID:"));
        add(txtActiviIdVC);

        add(new JLabel("Localización ID Localización:"));
        add(txtLocalizacionIdLocalizacionVC);

        add(new JLabel("Asociación ID:"));
        add(txtAsociacionIdVC);

        add(new JLabel("Valor String:"));
        add(txtValorStringVC);

        add(new JLabel("Asociación ID Asociaciones:"));
        add(txtAsociacionIdAsociacionesVC);

        add(new JLabel("Componente ID Componentes Activos:"));
        add(txtComponenteIdComponentesActivosVC);

        add(new JLabel("Expresión ID Expresiones:"));
        add(txtExpresionIdExpresionesVC);

        add(btnGuardarVC);

        // Acción al hacer clic en el botón Guardar
        btnGuardarVC.addActionListener(e -> saveDataVariablesContexto());

        setLocationRelativeTo(null); // Centrar en pantalla
        //setVisible(true); // Hacer visible la ventana
    }

    private void saveDataVariablesContexto() 
    {
        // Recoger los datos ingresados en el formulario
        String variableId = txtVariableIdVC.getText();
        String nombre = txtNombreVC.getText();
        String componenteId = txtComponenteIdVC.getText();
        String tipo = txtTipoVC.getText();
        String largo = txtLargoVC.getText();
        String valorEntero = txtValorEnteroVC.getText();
        String valorReal = txtValorRealVC.getText();
        String vectorEntero = txtVectorEnteroVC.getText();
        String vectorString = txtVectorStringVC.getText();
        String localizacionId = txtLocalizacionIdVC.getText();
        String activiId = txtActiviIdVC.getText();
        String localizacionIdLocalizacion = txtLocalizacionIdLocalizacionVC.getText();
        String asociacionId = txtAsociacionIdVC.getText();
        String valorString = txtValorStringVC.getText();
        String asociacionIdAsociaciones = txtAsociacionIdAsociacionesVC.getText();
        String componenteIdComponentesActivos = txtComponenteIdComponentesActivosVC.getText();
        String expresionIdExpresiones = txtExpresionIdExpresionesVC.getText();

        // Mostrar los datos por consola (puedes adaptarlo para guardarlos en la base de datos)
        System.out.println("Datos de Variables Contexto:");
        System.out.println("Variable ID: " + variableId);
        System.out.println("Nombre: " + nombre);
        System.out.println("Componente ID: " + componenteId);
        System.out.println("Tipo: " + tipo);
        System.out.println("Largo: " + largo);
        System.out.println("Valor Entero: " + valorEntero);
        System.out.println("Valor Real: " + valorReal);
        System.out.println("Vector Entero: " + vectorEntero);
        System.out.println("Vector String: " + vectorString);
        System.out.println("Localización ID: " + localizacionId);
        System.out.println("Activi ID: " + activiId);
        System.out.println("Localización ID Localización: " + localizacionIdLocalizacion);
        System.out.println("Asociación ID: " + asociacionId);
        System.out.println("Valor String: " + valorString);
        System.out.println("Asociación ID Asociaciones: " + asociacionIdAsociaciones);
        System.out.println("Componente ID Componentes Activos: " + componenteIdComponentesActivos);
        System.out.println("Expresión ID Expresiones: " + expresionIdExpresiones);
        // Cerrar el formulario
        dispose();
    }

    public void SentenciasForm() {

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
        txtSentenciaIdS = new JTextField();
        txtNombreS = new JTextField();
        txtQueryS = new JTextField();
        txtEstadoS = new JTextField();
        txtFechaAprobacionS = new JTextField();
        txtAlertaIdAlertasDefinicionS = new JTextField();
        btnGuardarS = new JButton("Guardar");

        // Añadir etiquetas y campos al formulario
        add(new JLabel("Sentencia ID:"));
        add(txtSentenciaIdS);

        add(new JLabel("Nombre:"));
        add(txtNombreS);

        add(new JLabel("Query:"));
        add(txtQueryS);

        add(new JLabel("Estado:"));
        add(txtEstadoS);

        add(new JLabel("Fecha Aprobación:"));
        add(txtFechaAprobacionS);

        add(new JLabel("Alerta ID Alertas Definición:"));
        add(txtAlertaIdAlertasDefinicionS);

        add(btnGuardarS);

        // Acción al hacer clic en el botón Guardar
        btnGuardarS.addActionListener(e -> saveDataSentencias());

        setLocationRelativeTo(null); // Centrar en pantalla
        //setVisible(true); // Hacer visible la ventana
    }

    private void saveDataSentencias() {
        // Recoger los datos ingresados en el formulario
        String sentenciaId = txtSentenciaIdS.getText();
        String nombre = txtNombreS.getText();
        String query = txtQueryS.getText();
        String estado = txtEstadoS.getText();
        String fechaAprobacion = txtFechaAprobacionS.getText();
        String alertaIdAlertasDefinicion = txtAlertaIdAlertasDefinicionS.getText();

        // Mostrar los datos por consola (puedes adaptarlo para guardarlos en la base de datos)
        System.out.println("Datos de Sentencias:");
        System.out.println("Sentencia ID: " + sentenciaId);
        System.out.println("Nombre: " + nombre);
        System.out.println("Query: " + query);
        System.out.println("Estado: " + estado);
        System.out.println("Fecha Aprobación: " + fechaAprobacion);
        System.out.println("Alerta ID Alertas Definición: " + alertaIdAlertasDefinicion);

        dispose();
    }
}

package com.tree.CapaNegocio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database
{

  //remplazar aqui su base de datos
  String url = "jdbc:sqlserver://localhost:1433;databaseName=arbol_db;integratedSecurity=true;encrypt=false;";


    public void testDatabaseConnection() throws SQLException, ClassNotFoundException 
    {
        // Cargar el controlador JDBC
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Configuración de conexión
        System.out.println("Intentando conectar a la base de datos...");

        // Establecer la conexión
        try (Connection connection = DriverManager.getConnection(url)) 
        {
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }


    
  
    public List<String> findNodesParents() 
    {
        String query = "SELECT nodoArbolId FROM dbo.nodoarbol";
        
        List<String> results = new ArrayList<>();

        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) 
            {
                String alertName = resultSet.getString("nodoArbolId");
                results.add(alertName);
            }
            //System.out.println("Consulta ejecutada exitosamente: " + query);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return results; 
    }

    public List<String> findNodesChildren(String idNodeParent)
    {
        String query = "SELECT * FROM nodosHijos WHERE nodoPadre = ?";
        
        List<String> results = new ArrayList<>();

        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,idNodeParent);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) 
            {
                String alertName = resultSet.getString("nodoHijo");
                results.add(alertName);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return results; 
    }

    public String findTypeOfNodo(String nodoArbolId)
    {
        String alertName = "";

        int id = Integer.parseInt(nodoArbolId);

        try
        {
            String query = "SELECT tipoNodo FROM nodoArbol WHERE nodoArbolId = ?";

            Connection connection = DriverManager.getConnection(url);
    
            PreparedStatement statement = connection.prepareStatement(query);
    
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) 
            {
                // Supongamos que la tabla "alertas" tiene una columna llamada "alertaNombre"
                alertName = resultSet.getString("tipoNodo");
                System.out.println("Se detecto nodo de tipo " + alertName);
            }

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return alertName;


    }

    public List<String> getDataForNodoArbol(int nodoArbolId) 
    {
        String tipoNodo = null;
        Integer causaId = null;
        List<String> arrayNodoInformation = new ArrayList<>();
    
        try {
            // Query para confirmar tipo de nodo
            String query = "SELECT tipoNodo, causaId FROM nodoArbol WHERE nodoArbolId = ?";
            
            // Queries para obtener información específica
            String activosQuery = "SELECT * FROM activos WHERE activoId = ?";
            String variablesQuery = "SELECT * FROM variablesContexto WHERE VariableId = ?";
            String sentenciasQuery = "SELECT * FROM sentencias WHERE sentenciaId = ?";
    
            // Establecer conexión
            Connection connection = DriverManager.getConnection(url);
    
            // Preparar consulta inicial
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, nodoArbolId);
            ResultSet resultSet = statement.executeQuery();
    
            // Obtener tipoNodo y causaId
            if (resultSet.next()) {
                tipoNodo = resultSet.getString("tipoNodo");
                causaId = resultSet.getInt("causaId");
            } else {
                System.out.println("Nodo no encontrado para nodoArbolId: " + nodoArbolId);
                return arrayNodoInformation;
            }
    
            // Preparar consultas específicas
            PreparedStatement detailStatement = null;
    
            switch (tipoNodo) {
                case "activos":
                case "Nodo de activos":
                    detailStatement = connection.prepareStatement(activosQuery);
                    detailStatement.setInt(1, causaId);
                    break;
    
                case "variables":
                case "Nodo de variables":
                    detailStatement = connection.prepareStatement(variablesQuery);
                    detailStatement.setInt(1, causaId);
                    break;
    
                case "sentencias":
                case "Nodo de sentencias":
                    detailStatement = connection.prepareStatement(sentenciasQuery);
                    detailStatement.setInt(1, causaId);
                    break;
    
                default:
                    System.out.println("Tipo de nodo desconocido: " + tipoNodo);
                    return arrayNodoInformation;
            }
    
            // Ejecutar consulta específica y llenar el array
            if (detailStatement != null) {
                resultSet = detailStatement.executeQuery();
                System.out.println(resultSet  + "paso por el resulset");
                while (resultSet.next()) {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = resultSet.getMetaData().getColumnLabel(i);
                        String columnValue = resultSet.getString(i);
                        arrayNodoInformation.add(columnValue);
                    }
                }
                detailStatement.close();
            }
    
            // Cerrar conexiones
            resultSet.close();
            statement.close();
            connection.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return arrayNodoInformation;
    }
    
    

}

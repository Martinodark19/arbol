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
                System.out.println("funciono po " + alertName);
            }

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return alertName;


    }

}

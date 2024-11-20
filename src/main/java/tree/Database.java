package tree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

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
            if (detailStatement != null) 
            {
                resultSet = detailStatement.executeQuery();
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

    // Método para obtener los nombres de la columna "nombre"

    /* */
    public String getNameFromNodoArbol(Integer nodoArbolId) 
    {
        String nombre = "";

        String query = "SELECT nombre FROM nodoarbol WHERE nodoArbolId = ?";

        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query); 
            preparedStatement.setInt(1, nodoArbolId);

            ResultSet resultSet = preparedStatement.executeQuery();



            while (resultSet.next()) 
            {
                nombre = resultSet.getString("nombre");
            }
        }
         catch (Exception e) 
        {
            e.printStackTrace();
        }
        return nombre;
    }


    public Integer getNodoArbolIdByName(DefaultMutableTreeNode selectedNode) 
    {
        Integer nodoArbolId = null; // Variable para almacenar el resultado

        if (selectedNode != null && selectedNode.getUserObject() != null) 
        {
            // Extraer el nombre desde el nodo seleccionado
            String nombre = selectedNode.getUserObject().toString();

            String query = "SELECT nodoArbolId FROM nodoarbol WHERE nombre = ?";

            try
            {
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                // Configurar el parámetro de la consulta
                preparedStatement.setString(1, nombre);

                // Ejecutar la consulta
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) 
                {
                    // Obtener el nodoArbolId de la consulta
                    nodoArbolId = resultSet.getInt("nodoArbolId");
                    System.out.println("NodoArbolId encontrado: " + nodoArbolId);
                } 
                else 
                {
                    System.out.println("No se encontró un nodo con el nombre: " + nombre);
                }
                

            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
        else 
        {
            System.out.println("El nodo seleccionado no es válido.");
        }

        return nodoArbolId;
}


    
// metodos para acceder a los valores de los formularios


    public Map<String, String> getActivoDetails(int activoId) 
    {
        Map<String, String> activoDetails = new HashMap<>();

        String query = "SELECT tipo, estado, monitor FROM activos WHERE activoId = ?";

        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) 
            {

            // Configurar el parámetro activoId
            preparedStatement.setInt(1, activoId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Extraer los valores de las columnas
                    activoDetails.put("tipo", resultSet.getString("tipo"));
                    activoDetails.put("estado", resultSet.getString("estado"));
                    activoDetails.put("monitor", resultSet.getString("monitor"));
                } else {
                    System.out.println("No se encontró ningún registro con el activoId: " + activoId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activoDetails;
    }

    public Map<String, String> getVariableDetails(Integer variableId) {
        Map<String, String> variableDetails = new HashMap<>();

        String query = "SELECT tipo, activoId FROM variablescontexto WHERE VariableId = ?";
        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, variableId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    variableDetails.put("tipo", resultSet.getString("tipo"));
                    variableDetails.put("activoId", resultSet.getString("activoId"));
                } else {
                    System.out.println("No se encontró ningún registro con el VariableId: " + variableId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return variableDetails;
    }

    public Map<String, String> getSentenciaDetails(Integer sentenciaId) 
    {
        Map<String, String> sentenciaDetails = new HashMap<>();
    
        String query = "SELECT estado FROM sentencias WHERE sentenciaId = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, sentenciaId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    sentenciaDetails.put("estado", resultSet.getString("estado"));
                } else {
                    System.out.println("No se encontró ningún registro con el sentenciaId: " + sentenciaId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return sentenciaDetails;
    }


    //metodos para actualizar los datos de los formularios

    public boolean updateActivoDetails(int activoId, String tipo, String estado, String monitor) {
        String query = "UPDATE activos SET tipo = ?, estado = ?, monitor = ? WHERE activoId = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, tipo);
            preparedStatement.setString(2, estado);
            preparedStatement.setString(3, monitor);
            preparedStatement.setInt(4, activoId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println(rowsUpdated + " se actualizzz");
            return rowsUpdated > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateVariableDetails(int variableId, String tipo, String activoId) 
    {
        String query = "UPDATE variablescontexto SET tipo = ?, activoId = ? WHERE VariableId = ?";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, tipo);
            preparedStatement.setInt(2, Integer.parseInt(activoId));
            preparedStatement.setInt(3, variableId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    


    public boolean updateSentenciaEstado(int sentenciaId, String estado) {
        String updateQuery = "UPDATE sentencias SET estado = ? WHERE sentenciaId = ?";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
    
            preparedStatement.setString(1, estado);
            preparedStatement.setInt(2, sentenciaId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    


    
    
    

}

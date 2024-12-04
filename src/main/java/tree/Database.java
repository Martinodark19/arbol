package tree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public class Database
{
  //remplazar aqui su base de datos
  private String url = "jdbc:sqlserver://localhost:1433;databaseName=arbol_db;integratedSecurity=true;encrypt=false;";

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
        } 
        catch (SQLException e) 
        {
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
            System.out.println(e);
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
            System.out.println(e);
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
            System.out.println(e);
        }

        return alertName;
    }

    public List<String> getDataForNodoArbol(int nodoArbolId) 
    {
        String tipoNodo = null;
        Integer modoarbolid = null;
        List<String> arrayNodoInformation = new ArrayList<>();
    
        try 
        {
            // Query para confirmar tipo de nodo
            String query = "SELECT tipoNodo, modoarbolid FROM nodoArbol WHERE nodoArbolId = ?";
            
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
    
            // Obtener tipoNodo y modoarbolid
            if (resultSet.next()) 
            {
                tipoNodo = resultSet.getString("tipoNodo");
                modoarbolid = resultSet.getInt("modoarbolid");
            } 
            else 
            {
                System.out.println("Nodo no encontrado para nodoArbolId: " + nodoArbolId);
                return arrayNodoInformation;
            }
    
            // Preparar consultas específicas
            PreparedStatement detailStatement = null;
    
            switch (tipoNodo) 
            {
                case "activos":
                case "nodo de activos":
                    detailStatement = connection.prepareStatement(activosQuery);
                    detailStatement.setInt(1, modoarbolid);
                    break;
    
                case "variables":
                case "nodo de variables":
                    detailStatement = connection.prepareStatement(variablesQuery);
                    detailStatement.setInt(1, modoarbolid);
                    break;
    
                case "sentencias":
                case "nodo de sentencias":
                    detailStatement = connection.prepareStatement(sentenciasQuery);
                    detailStatement.setInt(1, modoarbolid);
                    break;
        
    
                default:
                    System.out.println("Tipo de nodo desconocido: " + tipoNodo);
                    return arrayNodoInformation;
            }
    
            // Ejecutar consulta específica y llenar el array
            if (detailStatement != null) 
            {
                resultSet = detailStatement.executeQuery();
                while (resultSet.next()) 
                {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) 
                    {
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
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
        }
        return arrayNodoInformation;
    }

    // Método para obtener los nombres de la columna "nombre"
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
            System.out.println(e);
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
                } 
                else 
                {
                    System.out.println("No se encontró un nodo con el nombre: " + nombre);
                }
                

            } 
            catch (SQLException e) 
            {
                System.out.println(e);
            }
        }
        else 
        {
            System.out.println("El nodo seleccionado no es válido.");
        }
        return nodoArbolId;
}


// metodos para acceder a los valores de los formularios
    public Map<Integer, String> getActivoDetails(int activoId) 
    {
        Map<Integer, String> activoDetails = new HashMap<>();
        String query = "SELECT tipo, estado, monitor FROM activos WHERE activoId = ?";

        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Configurar el parámetro activoId
            preparedStatement.setInt(1, activoId);
            ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) 
                {
                    // Extraer los valores de las columnas


                    activoDetails.put(1, resultSet.getString("tipo"));
                    activoDetails.put(2, resultSet.getString("estado"));
                    activoDetails.put(3, resultSet.getString("monitor"));
                } 
                else 
                {
                    System.out.println("No se encontró ningún registro con el activoId: " + activoId);
                }
        } 
        catch (SQLException e) 
        {
            System.out.println("Ocurrió un error al consultar los detalles del activo con ID: " + e);
            
        }

        return activoDetails;
    }

    public Map<Integer, String> getVariableDetails(Integer variableId) 
    {
        Map<Integer, String> variableDetails = new HashMap<>();

        String query = "SELECT tipo, activoId FROM variablescontexto WHERE VariableId = ?";
        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, variableId);

            ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) 
                {
                    variableDetails.put(1, resultSet.getString("tipo"));
                    variableDetails.put(2, resultSet.getString("activoId"));
                } 
                else 
                {
                    System.out.println("No se encontró ningún registro con el VariableId: " + variableId);
                }
            
        } 
        catch (SQLException e) 
        {
            System.out.println("Ocurrió un error al consultar los detalles del VariableId con ID: " + e);
        }

        return variableDetails;
    }

    public Map<Integer, String> getSentenciaDetails(Integer sentenciaId) 
    {
        Map<Integer, String> sentenciaDetails = new HashMap<>();
    
        String query = "SELECT estado FROM sentencias WHERE sentenciaId = ?";
        try 
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sentenciaId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
                if (resultSet.next()) 
                {
                    sentenciaDetails.put(1, resultSet.getString("estado"));
                } 
                else 
                {
                    System.out.println("No se encontró ningún registro con el sentenciaId: " + sentenciaId);
                }
            
        } 
        catch (SQLException e) 
        {
            System.out.println("Ocurrió un error al consultar los detalles del sentenciaId con ID: " + e);

        }
    
        return sentenciaDetails;
    }


    //metodos para actualizar los datos de los formularios
    public boolean updateActivoDetails(int activoId, String tipo, String estado, String monitor) {
        String query = "UPDATE activos SET tipo = ?, estado = ?, monitor = ? WHERE activoId = ?";
        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tipo);
            preparedStatement.setString(2, estado);
            preparedStatement.setString(3, monitor);
            preparedStatement.setInt(4, activoId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Retorna true si se actualizó al menos una fila
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
            return false;
        }
    }


    public boolean updateVariableDetails(int variableId, String tipo, int activoId) 
    {
        String query = "UPDATE variablescontexto SET tipo = ?, activoId = ? WHERE VariableId = ?";
    
        try  
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tipo);
            preparedStatement.setInt(2, activoId);
            preparedStatement.setInt(3, variableId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) 
            {
                return true;
            }

            return false;
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
            return false;
        }
    }
    
    public boolean updateSentenciaEstado(int sentenciaId, String estado) 
    {

        String updateQuery = "UPDATE sentencias SET estado = ? WHERE sentenciaId = ?";
        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, estado);
            preparedStatement.setInt(2, sentenciaId);
    
            int rowsUpdated = preparedStatement.executeUpdate();

            if(rowsUpdated > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
    
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
            return false;
        }
    }


    public boolean saveNuevoNodo(String tipoNodo, Map<String, String> valores) 
    {
        String query = "INSERT INTO nodoarbol (tipoNodo, tipoExpresion, opcionTF, ...) VALUES (?, ?, ?, ...)";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, tipoNodo);
    
            // Rellenar los campos dinámicos
            int index = 2;
            for (String key : valores.keySet()) {
                preparedStatement.setString(index, valores.get(key));
                index++;
            }
    
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
    
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
            return false;
        }
    }


    public Boolean insertarNodoArbol(String tipoNodo, String nombre, Map<Integer,String> mapTipoNodo)                       
    {
        try
        {
            String query = "INSERT INTO nodoarbol (nodoArbolId, tipoNodo, modoarbolid, nombre) VALUES (?, ?, ?, ?)";
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatementQuery = connection.prepareStatement(query);

            //obtener el maximo nodoArbolId
            String maxNodoArbolIdQuery = "SELECT MAX(nodoArbolId) AS maxNodoArbolId FROM nodoarbol";
            Statement statementNodoArbolID = connection.createStatement();
            ResultSet resultSet = statementNodoArbolID.executeQuery(maxNodoArbolIdQuery);

            int nodoArbolId = 1; // Valor inicial si la tabla está vacía
            if (resultSet.next()) 
            {
                int maxCauseId = resultSet.getInt("maxNodoArbolId");
                nodoArbolId = maxCauseId + 1; // Incrementar el último ID
            }

            // Paso 1: obtener el maximo modoarbolid
            String maxCauseIdQuery = "SELECT MAX(modoarbolid) AS maxModoarbolId FROM nodoarbol";
            Statement statementCauseId = connection.createStatement();
            ResultSet resultSetCauseId = statementCauseId.executeQuery(maxCauseIdQuery);

            int modoarbolId = 1; // Valor inicial si la tabla está vacía
            if (resultSetCauseId.next()) 
            {
                int maxModoarbolId = resultSetCauseId.getInt("maxModoarbolId");
                modoarbolId = maxModoarbolId + 1; // Incrementar el último ID
            }

            int resultSetQuery;

            switch (tipoNodo) 
            {
                case "activos":
                    String queryActivo = "INSERT INTO activos (activoId, tipo, estado, monitor) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatementQueryActivo = connection.prepareStatement(queryActivo);

                    //insert para tabla nodoArbol
                    preparedStatementQuery.setInt(1, nodoArbolId);
                    preparedStatementQuery.setString(2, tipoNodo);
                    preparedStatementQuery.setInt(3, modoarbolId);
                    preparedStatementQuery.setString(4, nombre);

                    //insert para Activos                    
                    preparedStatementQueryActivo.setInt(1, modoarbolId);
                    preparedStatementQueryActivo.setString(2, mapTipoNodo.get(1));
                    preparedStatementQueryActivo.setString(3, mapTipoNodo.get(2));
                    preparedStatementQueryActivo.setString(4, mapTipoNodo.get(3));

                    resultSetQuery = preparedStatementQuery.executeUpdate();
                    int  resultSetActivos = preparedStatementQueryActivo.executeUpdate();

                    break;
                case "variables":
                    String queryVariable = "INSERT INTO  variablesContexto (VariableId,tipo, activoId) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatementQueryVariables = connection.prepareStatement(queryVariable);

                    //insert para tabla nodoArbol
                    preparedStatementQuery.setInt(1, nodoArbolId);
                    preparedStatementQuery.setString(2, tipoNodo);
                    preparedStatementQuery.setInt(3, modoarbolId);
                    preparedStatementQuery.setString(4, nombre);

                    //insert para Variables                    
                    preparedStatementQueryVariables.setInt(1, modoarbolId);
                    preparedStatementQueryVariables.setString(2, mapTipoNodo.get(1));
                    preparedStatementQueryVariables.setInt(3, Integer.parseInt(mapTipoNodo.get(2)));

                    resultSetQuery = preparedStatementQuery.executeUpdate();
                    int  resultSetVariables = preparedStatementQueryVariables.executeUpdate();

                    break;
                case "sentencias":

                    String querySentencias = "INSERT INTO sentencias (sentenciaId,estado) VALUES (?, ?)";
                    PreparedStatement preparedStatementQuerySentencias = connection.prepareStatement(querySentencias);


                    //insert para tabla nodoArbol
                    preparedStatementQuery.setInt(1, nodoArbolId);
                    preparedStatementQuery.setString(2, tipoNodo);
                    preparedStatementQuery.setInt(3, modoarbolId);
                    preparedStatementQuery.setString(4, nombre);

                    // esta llegando null a sentencias

                    preparedStatementQuerySentencias.setInt(1, modoarbolId);
                    preparedStatementQuerySentencias.setString(2, mapTipoNodo.get(1));

                    resultSetQuery = preparedStatementQuery.executeUpdate();
                    int resultSetSentencias = preparedStatementQuerySentencias.executeUpdate();
                    break;
                default:
                    System.out.println("Tipo de nodo inválido: " + tipoNodo);
            }
    
        }
      

        catch (SQLException e) 
        {
            System.err.println("Error al insertar en la tabla nodoArbol: " + e.getMessage());
            return false;
        }

        return true;
    }


    public Map<Integer, String> obtenerSoloActivoId() 
    {
        Map<Integer, String> activosMap = new HashMap<>();
    
        String query = "SELECT activoId FROM activos";
    
        try 
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int activoId = resultSet.getInt("activoId");
                activosMap.put(activoId, null); // El valor es null porque solo queremos la clave
            }
    
        } 
        catch (SQLException e) 
        {
            System.err.println("Error al obtener los valores de activoId: " + e.getMessage());
        }
    
        return activosMap;
    }


    public Map<Integer, String> obtenerNombresNodos() 
    {
        // Mapa para almacenar los resultados
        Map<Integer, String> nodoArbolMap = new HashMap<>();

        // Consulta SQL para seleccionar nodoArbolId y nombre
        String selectQuery = "SELECT nodoArbolId, nombre FROM nodoarbol";

        try
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterar sobre los resultados y llenar el mapa
            while (resultSet.next()) 
            {
                int nodoArbolId = resultSet.getInt("nodoArbolId");
                String nombre = resultSet.getString("nombre");
                nodoArbolMap.put(nodoArbolId, nombre);
            }

        } 
        catch (SQLException e) 
        {
            System.out.println(e);
        }

        return nodoArbolMap;
    }


    public Boolean InsertRelacionesNodos(Integer nodoArbolIdPadre, Integer nodoArbolIdHijo, String opcionTf) 
    {
        String guardarRelacionQuery = "INSERT INTO nodosHijos (nodoPadre, nodoHijo, opcionTF) VALUES (? , ? , ?)";
                
        try 
        {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(guardarRelacionQuery);
            preparedStatement.setInt(1, nodoArbolIdPadre);
            preparedStatement.setInt(2, nodoArbolIdHijo);
            preparedStatement.setString(3, opcionTf);


            int resultSet = preparedStatement.executeUpdate();

            if (resultSet > 0) 
            {
                return true;
            }
            else
            {
                return false;
            }

        } 
        catch (SQLException e) 
        {
            // Manejo de excepciones: imprimir la traza y/o registrar el error
            System.err.println("Error al intentar insertar relación en nodosHijos: " + e.getMessage());
            return false;
        }
    }
    
    
    
    
    
    


    
    
    

}

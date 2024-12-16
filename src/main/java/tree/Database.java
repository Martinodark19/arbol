package tree;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
    
        // Uso de try-with-resources para garantizar el cierre de recursos
        try 
        (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        )

        {
            while (resultSet.next()) 
            {
                String alertName = resultSet.getString("nodoArbolId");
                results.add(alertName);
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    
        return results;
    }
    


    
    public List<String> findNodesChildren(String idNodeParent) 
    {
        String query = "SELECT * FROM nodosHijos WHERE nodoPadre = ?";
        List<String> results = new ArrayList<>();
    
        // Uso de try-with-resources para asegurar el cierre de recursos
        try 
        (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            // Establecer el parámetro de la consulta
            statement.setString(1, idNodeParent);
            
            // Ejecutar la consulta y procesar los resultados
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String alertName = resultSet.getString("nodoHijo");
                    results.add(alertName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    
        return results;
    }
    

    public String findTypeOfNodo(String nodoArbolId) {
        String alertName = "";
        
        // Convertir nodoArbolId a int
        int id = Integer.parseInt(nodoArbolId);
    
        // Uso de try-with-resources para manejar los recursos
        String query = "SELECT tipoNodo FROM nodoArbol WHERE nodoArbolId = ?";
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            // Establecer el parámetro de la consulta
            statement.setInt(1, id);
            
            // Ejecutar la consulta y procesar el resultado
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    alertName = resultSet.getString("tipoNodo");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    
        return alertName;
    }
    

    public List<String> getDataForNodoArbol(int nodoArbolId) 
    {
        List<String> arrayNodoInformation = new ArrayList<>();
        String tipoNodo = null;
    
        // Consultas
        String query = "SELECT tipoNodo FROM nodoArbol WHERE nodoArbolId = ?";
        String activosQuery = "SELECT * FROM activos WHERE activoId = ?";
        String variablesQuery = "SELECT * FROM variablesContexto WHERE VariableId = ?";
        String sentenciasQuery = "SELECT * FROM sentencias WHERE sentenciaId = ?";
    
        try 
        (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement initialStatement = connection.prepareStatement(query)
        ) {
            // Establecer parámetro y ejecutar consulta inicial
            initialStatement.setInt(1, nodoArbolId);
            try (ResultSet resultSet = initialStatement.executeQuery()) 
            {
                if (resultSet.next()) {
                    tipoNodo = resultSet.getString("tipoNodo");
                } else {
                    System.out.println("Nodo no encontrado para nodoArbolId: " + nodoArbolId);
                    return arrayNodoInformation;
                }
            }
    
            // Preparar la consulta específica basada en el tipo de nodo
            String detailQuery;
            switch (tipoNodo) 
            {
                case "activos":
                case "nodo de activos":
                    detailQuery = activosQuery;
                    break;
                case "variables":
                case "nodo de variables":
                    detailQuery = variablesQuery;
                    break;
                case "sentencias":
                case "nodo de sentencias":
                    detailQuery = sentenciasQuery;
                    break;
                default:
                    System.out.println("Tipo de nodo desconocido: " + tipoNodo);
                    return arrayNodoInformation;
            }
    
            // Ejecutar la consulta específica
            try 
            (
                PreparedStatement detailStatement = connection.prepareStatement(detailQuery)
            ) 
            {
                detailStatement.setInt(1, nodoArbolId);
                try (ResultSet detailResultSet = detailStatement.executeQuery()) 
                {

                    while (detailResultSet.next()) 
                    {

                        switch (tipoNodo) 
                        {
                            case "activos":
                            case "nodo de activos":
                            
                            String tipo = detailResultSet.getString("tipo"); 
                            String estado = detailResultSet.getString("estado"); 
                            String monitor = detailResultSet.getString("monitor");
                            String nombreActivo = detailResultSet.getString("nombre");

                            arrayNodoInformation.add(tipo);
                            arrayNodoInformation.add(estado);
                            arrayNodoInformation.add(monitor);
                            arrayNodoInformation.add(nombreActivo);

                            break;

                            case "variables":
                            case "nodo de variables":
                            String tipoVariable = detailResultSet.getString("tipo"); 
                            int activoId = detailResultSet.getInt("activoId");
                            String nombreVariable = detailResultSet.getString("nombre");
                            String valorString = detailResultSet.getString("valorString");

                            arrayNodoInformation.add(tipoVariable);
                            arrayNodoInformation.add(activoId + "");
                            arrayNodoInformation.add(nombreVariable);
                            arrayNodoInformation.add(valorString);
                            break;
                            
                            case "sentencias":
                            case "nodo de sentencias":
                            String estadoSentencias = detailResultSet.getString("estado"); 
                            String querySentencias = detailResultSet.getString("query");
                            String nombreSentencias = detailResultSet.getString("nombre");

                            arrayNodoInformation.add(estadoSentencias);
                            arrayNodoInformation.add(querySentencias);
                            arrayNodoInformation.add(nombreSentencias);
                                break;
                            default:
                                System.out.println("Tipo de nodo no coincide para retornar informacion " + tipoNodo);
                                return arrayNodoInformation;
                        }

                    }
            
                
                }
            }
        } catch (SQLException e) 
        {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
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

    public Integer getNodoArbolIdByName(DefaultMutableTreeNode selectedNode) {
        Integer nodoArbolId = null; // Variable para almacenar el resultado
    
        if (selectedNode != null && selectedNode.getUserObject() != null) {
            // Extraer el nombre desde el nodo seleccionado
            String nombre = selectedNode.getUserObject().toString();
            String query = "SELECT nodoArbolId FROM nodoarbol WHERE nombre = ?";
    
            // Uso de try-with-resources para garantizar el cierre de recursos
            try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
            ) {
                // Configurar el parámetro de la consulta
                preparedStatement.setString(1, nombre);
    
                // Ejecutar la consulta y procesar los resultados
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Obtener el nodoArbolId de la consulta
                        nodoArbolId = resultSet.getInt("nodoArbolId");
                    } else {
                        System.out.println("No se encontró un nodo con el nombre: " + nombre);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al ejecutar la consulta: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El nodo seleccionado no es válido.");
        }
    
        return nodoArbolId;
    }
    


    public Map<Integer, String> getActivoDetails(int activoId) 
    {
        Map<Integer, String> activoDetails = new HashMap<>();
        String query = "SELECT tipo, estado, monitor FROM activos WHERE activoId = ?";
    
        // Uso de try-with-resources para cerrar automáticamente los recursos
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) 
        {
            // Configurar el parámetro activoId
            preparedStatement.setInt(1, activoId);
    
            // Ejecutar la consulta y procesar los resultados
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Extraer los valores de las columnas
                    activoDetails.put(1, resultSet.getString("tipo"));
                    activoDetails.put(2, resultSet.getString("estado"));
                    activoDetails.put(3, resultSet.getString("monitor"));
                } else {
                    System.out.println("No se encontró ningún registro con el activoId: " + activoId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrió un error al consultar los detalles del activo con ID " + activoId + ": " + e.getMessage());
            e.printStackTrace();
        }
    
        return activoDetails;
    }
    

    public Map<Integer, String> getVariableDetails(Integer variableId) {
        Map<Integer, String> variableDetails = new HashMap<>();
        String query = "SELECT tipo, activoId FROM variablescontexto WHERE VariableId = ?";
    
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, variableId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    variableDetails.put(1, resultSet.getString("tipo"));
                    variableDetails.put(2, resultSet.getString("activoId"));
                } else {
                    System.out.println("No se encontró ningún registro con el VariableId: " + variableId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrió un error al consultar los detalles del VariableId con ID " + variableId + ": " + e.getMessage());
            e.printStackTrace();
        }
    
        return variableDetails;
    }

    
    public Map<Integer, String> getSentenciaDetails(Integer sentenciaId) {
        Map<Integer, String> sentenciaDetails = new HashMap<>();
        String query = "SELECT estado FROM sentencias WHERE sentenciaId = ?";
    
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, sentenciaId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    sentenciaDetails.put(1, resultSet.getString("estado"));
                } else {
                    System.out.println("No se encontró ningún registro con el sentenciaId: " + sentenciaId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocurrió un error al consultar los detalles del sentenciaId con ID " + sentenciaId + ": " + e.getMessage());
            e.printStackTrace();
        }
    
        return sentenciaDetails;
    }

    
    public boolean updateActivoDetails(int activoId, String tipo, String estado, String monitor, String nombre) {
        String query = "UPDATE activos SET tipo = ?, estado = ?, monitor = ?, nombre = ? WHERE activoId = ?";
        String queryUpdateNombreNodoArbol = "UPDATE nodoarbol SET nombre = ? WHERE nodoArbolId = ?";
    
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            PreparedStatement preparedStatementQueryNodoArbol = connection.prepareStatement(queryUpdateNombreNodoArbol)
        ) {
            // Iniciar transacción
            connection.setAutoCommit(false);
    
            // Actualizar tabla 'activos'
            preparedStatement.setString(1, tipo);
            preparedStatement.setString(2, estado);
            preparedStatement.setString(3, monitor);
            preparedStatement.setString(4, nombre);
            preparedStatement.setInt(5, activoId);
            int rowsUpdated = preparedStatement.executeUpdate();
    
            if (rowsUpdated > 0) {
                // Actualizar tabla 'nodoarbol'
                preparedStatementQueryNodoArbol.setString(1, nombre);
                preparedStatementQueryNodoArbol.setInt(2, activoId);
                int executeQueryNodoArbol = preparedStatementQueryNodoArbol.executeUpdate();
    
                if (executeQueryNodoArbol > 0) {
                    // Ambas actualizaciones exitosas, confirmar transacción
                    connection.commit();
                    return true;
                } else {
                    // La segunda actualización falló, deshacer cambios
                    connection.rollback();
                    return false;
                }
            } else {
                // La primera actualización falló, deshacer transacción
                connection.rollback();
                return false;
            }
    
        } catch (SQLException e) {
            System.err.println("Ocurrió un error al actualizar los detalles del activo con ID " + activoId + ": " + e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    

    public boolean updateVariableDetails(int variableId, String tipo, int activoId, String nombre, String valorString) 
    {
        String query = "UPDATE variablescontexto SET nombre = ?, tipo = ?, activoId = ?, valorString = ? WHERE VariableId = ?";
        String queryUpdateNombreNodoArbol = "UPDATE nodoarbol SET nombre = ? WHERE nodoArbolId = ?";
    
        try 
        (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            PreparedStatement preparedStatementQueryNodoArbol = connection.prepareStatement(queryUpdateNombreNodoArbol)
        ) 
        {
            // Desactivar autocommit para iniciar la transacción
            connection.setAutoCommit(false);
    
            // Actualizar la tabla variablescontexto
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, tipo);
            preparedStatement.setInt(3, activoId);
            preparedStatement.setString(4, valorString);
            preparedStatement.setInt(5, variableId);


            int rowsUpdated = preparedStatement.executeUpdate();
    
            if (rowsUpdated > 0) 
            {
                // Actualizar la tabla nodoarbol
                preparedStatementQueryNodoArbol.setString(1, nombre);
                preparedStatementQueryNodoArbol.setInt(2, variableId);
                
                int executeNodoArbol = preparedStatementQueryNodoArbol.executeUpdate();
                if (executeNodoArbol > 0) 
                {
                    // Ambas actualizaciones fueron exitosas, confirmar la transacción
                    connection.commit();
                    return true;
                } 
                else 
                {
                    // Falla la segunda actualización, deshacer cambios
                    connection.rollback();
                    return false;
                }
            } 
            else 
            {
                // Falla la primera actualización, deshacer cambios
                connection.rollback();
                return false;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error al actualizar los detalles de la variable con ID " + variableId + ": " + e.getMessage());
            e.printStackTrace();
            // En caso de excepción, se retorna false. La conexión se cierra automáticamente.
            return false;
        }
    }
    

    
    public boolean updateSentenciaEstado(int sentenciaId, String estado, String query, String nombre) 
    {
        String updateQuery = "UPDATE sentencias SET nombre = ?, estado = ?, query = ? WHERE sentenciaId = ?";
        String queryUpdateNombreNodoArbol = "UPDATE nodoarbol SET nombre = ? WHERE nodoArbolId = ?";

    
        try 
        (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            PreparedStatement preparedStatementQueryNodoArbol = connection.prepareStatement(queryUpdateNombreNodoArbol)

        ) 

        {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, estado);
            preparedStatement.setString(3, query);
            preparedStatement.setInt(4, sentenciaId);
    
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) 
            {
                preparedStatementQueryNodoArbol.setString(1, nombre);
                preparedStatementQueryNodoArbol.setInt(2, sentenciaId);
                int executeNodoArbol = preparedStatementQueryNodoArbol.executeUpdate();

                if (executeNodoArbol > 0) 
                {
                    connection.commit();

                    return true;
                }
                else
                {
                    connection.rollback();
                    return false;

                }
            }
            else
            {

                connection.rollback();
                return false;
            }

        } 
        catch (SQLException e) 
        {
            System.err.println("Error al actualizar el estado de la sentencia con ID " + sentenciaId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    



    public boolean saveNuevoNodo(String tipoNodo, Map<String, String> valores) {
        String query = "INSERT INTO nodoarbol (tipoNodo, tipoExpresion, opcionTF, ...) VALUES (?, ?, ?, ...)";
        
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {
            preparedStatement.setString(1, tipoNodo);
            
            // Rellenar los campos dinámicos
            int index = 2;
            for (String key : valores.keySet()) 
            {
                preparedStatement.setString(index, valores.get(key));
                index++;
            }
    
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } 
        catch (SQLException e) 
        {
            System.err.println("Error al guardar el nuevo nodo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Boolean insertarNodoArbol(String tipoNodo, String nombre, Map<Integer, String> mapTipoNodo) {
        String queryNodoArbol = "INSERT INTO nodoarbol (nodoArbolId, tipoNodo, nombre) VALUES (?, ?, ?)";
        String maxNodoArbolIdQuery = "SELECT MAX(nodoArbolId) AS maxNodoArbolId FROM nodoarbol";
    
        try (
            Connection connection = DriverManager.getConnection(url);
            Statement statementNodoArbolID = connection.createStatement()
        ) {
            connection.setAutoCommit(false); // Iniciar transacción
    
            // Obtener el próximo nodoArbolId
            int nodoArbolId = 1; // Valor inicial si la tabla está vacía
            try (ResultSet resultSet = statementNodoArbolID.executeQuery(maxNodoArbolIdQuery)) {
                if (resultSet.next()) {
                    int maxNodoArbolId = resultSet.getInt("maxNodoArbolId");
                    nodoArbolId = maxNodoArbolId + 1;
                }
            }
    
            // Validar las claves requeridas en mapTipoNodo
            switch (tipoNodo) {
                case "activos":
                    if (!mapTipoNodo.containsKey(1) || !mapTipoNodo.containsKey(2) || !mapTipoNodo.containsKey(3)) 
                    {
                        connection.rollback();
                        return false;
                    }
                    break;
                case "variables":
                    if (!mapTipoNodo.containsKey(1) || !mapTipoNodo.containsKey(2)) 
                    {
                        connection.rollback();
                        return false;
                    }
                    break;
                case "sentencias":
                    if (!mapTipoNodo.containsKey(1)) 
                    {
                        connection.rollback();
                        return false;
                    }
                    break;
                default:
                    System.out.println("Tipo de nodo inválido: " + tipoNodo);
                    return false;
            }
    
            // Insertar en nodoarbol
            try (PreparedStatement preparedStatementQuery = connection.prepareStatement(queryNodoArbol)) {
                preparedStatementQuery.setInt(1, nodoArbolId);
                preparedStatementQuery.setString(2, tipoNodo);
                preparedStatementQuery.setString(3, nombre);
                preparedStatementQuery.executeUpdate();
            }
    
            // Insertar en la tabla específica según tipoNodo
            switch (tipoNodo) {
                case "activos":
                    String queryActivo = "INSERT INTO activos (activoId, nombre,  tipo, estado, monitor) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementQueryActivo = connection.prepareStatement(queryActivo)) {
                        preparedStatementQueryActivo.setInt(1, nodoArbolId);
                        preparedStatementQueryActivo.setString(2, nombre);
                        preparedStatementQueryActivo.setString(3, mapTipoNodo.get(1));
                        preparedStatementQueryActivo.setString(4, mapTipoNodo.get(2));
                        preparedStatementQueryActivo.setString(5, mapTipoNodo.get(3));
                        preparedStatementQueryActivo.executeUpdate();
                    }
                    break;
                case "variables":
                    String queryVariable = "INSERT INTO variablesContexto (VariableId, nombre,  tipo, activoId, valorString) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementQueryVariables = connection.prepareStatement(queryVariable)) 
                    {
                        preparedStatementQueryVariables.setInt(1, nodoArbolId);
                        preparedStatementQueryVariables.setString(2, nombre);
                        preparedStatementQueryVariables.setString(3, mapTipoNodo.get(1));
                        preparedStatementQueryVariables.setInt(4, Integer.parseInt(mapTipoNodo.get(2)));
                        preparedStatementQueryVariables.setString(5, mapTipoNodo.get(3));
                        preparedStatementQueryVariables.executeUpdate();
                    }
                    break;
                case "sentencias":
                    String querySentencias = "INSERT INTO sentencias (sentenciaId, nombre,  estado, query) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementQuerySentencias = connection.prepareStatement(querySentencias)) 
                    {
                        preparedStatementQuerySentencias.setInt(1, nodoArbolId);
                        preparedStatementQuerySentencias.setString(2, nombre);
                        preparedStatementQuerySentencias.setString(3, mapTipoNodo.get(1));
                        preparedStatementQuerySentencias.setString(4, mapTipoNodo.get(2));
                        preparedStatementQuerySentencias.executeUpdate();
                    }
                    break;
            }
    
            connection.commit(); // Confirmar transacción
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar en la tabla nodoArbol: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    




    public Map<Integer, String> obtenerSoloActivoId() {
        Map<Integer, String> activosMap = new HashMap<>();
        String query = "SELECT activoId FROM activos";
    
        // Uso de try-with-resources para cerrar automáticamente los recursos
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                int activoId = resultSet.getInt("activoId");
                activosMap.put(activoId, null); // El valor es null porque solo queremos la clave
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los valores de activoId: " + e.getMessage());
            e.printStackTrace();
        }
    
        return activosMap;
    }

    
    public Map<Integer, String> obtenerNombresNodos() {
        Map<Integer, String> nodoArbolMap = new HashMap<>();
        String selectQuery = "SELECT nodoArbolId, nombre FROM nodoarbol";
    
        // Uso de try-with-resources para cerrar automáticamente los recursos
        try (
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                int nodoArbolId = resultSet.getInt("nodoArbolId");
                String nombre = resultSet.getString("nombre");
                nodoArbolMap.put(nodoArbolId, nombre);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los nombres de los nodos: " + e.getMessage());
            e.printStackTrace();
        }
    
        return nodoArbolMap;
    }
    

    public Boolean InsertRelacionesNodos(Integer nodoArbolIdPadre, Integer nodoArbolIdHijo, String opcionTf) {
        // Query para verificar si ya existe la relación
        String verificarRelacionQuery = "SELECT COUNT(*) FROM nodosHijos WHERE nodoPadre = ? AND nodoHijo = ?";
    
        // Query para eliminar la relación existente
        String eliminarRelacionQuery = "DELETE FROM nodosHijos WHERE nodoPadre = ? AND nodoHijo = ?";
    
        // Query para insertar la nueva relación
        String guardarRelacionQuery = "INSERT INTO nodosHijos (nodoPadre, nodoHijo, opcionTF) VALUES (?, ?, ?)";
    
        try (
            Connection connection = DriverManager.getConnection(url)
        ) {
            connection.setAutoCommit(false); // Iniciar transacción
    
            // Verificar si la relación ya existe
            try (PreparedStatement verificarStatement = connection.prepareStatement(verificarRelacionQuery)) {
                verificarStatement.setInt(1, nodoArbolIdPadre);
                verificarStatement.setInt(2, nodoArbolIdHijo);
                try (ResultSet resultSet = verificarStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        // Si existe la relación, eliminarla
                        try (PreparedStatement eliminarStatement = connection.prepareStatement(eliminarRelacionQuery)) {
                            eliminarStatement.setInt(1, nodoArbolIdPadre);
                            eliminarStatement.setInt(2, nodoArbolIdHijo);
                            eliminarStatement.executeUpdate();
                        }
                    }
                }
            }
    
            // Insertar la nueva relación
            try (PreparedStatement insertarStatement = connection.prepareStatement(guardarRelacionQuery)) {
                insertarStatement.setInt(1, nodoArbolIdPadre);
                insertarStatement.setInt(2, nodoArbolIdHijo);
                insertarStatement.setString(3, opcionTf);
    
                int rowsInserted = insertarStatement.executeUpdate();
                if (rowsInserted > 0) {
                    connection.commit();
                    return true; // Éxito
                } else {
                    connection.rollback();
                    return false; // Fallo al insertar
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar insertar relación en nodosHijos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    

    public List<String> obtenerNodosSinRelacion() 
    {
        String query = "SELECT nodoArbolId, nombre FROM nodoarbol WHERE nodoArbolId NOT IN (SELECT nodoHijo FROM nodosHijos)";
        List<String> nodosDisponibles = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) 
        {
    
            while (resultSet.next()) 
            {
                String nodoNombre = resultSet.getString("nombre");
                nodosDisponibles.add(nodoNombre);
            }
    
        } catch (SQLException e) {
            System.err.println("Error al obtener nodos sin relación: " + e.getMessage());
        }
    
        return nodosDisponibles;
    }


    public boolean eliminarNodoArbol(int nodoArbolID) 
    {
        // Sentencias SQL
        String verificarExistenciaNodo = "SELECT tipoNodo FROM nodoarbol WHERE nodoArbolId = ?";
        String obtenerHijos = "SELECT nodoHijo FROM nodosHijos WHERE nodoPadre = ?";
        String eliminarRelaciones = "DELETE FROM nodosHijos WHERE nodoPadre = ? OR nodoHijo = ?";
        String eliminarNodo = "DELETE FROM nodoarbol WHERE nodoArbolId = ?";
        String eliminarActivoId = "DELETE FROM activos WHERE activoId = ?";
        String eliminarVariableId = "DELETE FROM variablesContexto WHERE VariableId = ?";
        String eliminarSentenciaId = "DELETE FROM sentencias WHERE sentenciaId = ?";

        Connection connection = null;
        PreparedStatement pstmtVerificar = null;
        PreparedStatement pstmtObtenerHijos = null;
        PreparedStatement pstmtEliminarRelaciones = null;
        PreparedStatement pstmtEliminarNodo = null;
        PreparedStatement pstmtEliminarSpecific = null;
        ResultSet rsVerificar = null;
        ResultSet rsHijos = null;

        try 
        {
            // Establecer conexión
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false); // Iniciar transacción

            // 1. Verificar si el nodo existe y obtener su tipo
            pstmtVerificar = connection.prepareStatement(verificarExistenciaNodo);
            pstmtVerificar.setInt(1, nodoArbolID);
            rsVerificar = pstmtVerificar.executeQuery();

            String tipoNodo = null;
            if (rsVerificar.next()) 
            {
                tipoNodo = rsVerificar.getString("tipoNodo").toLowerCase();
            } 
            else 
            {
                System.err.println("Nodo con ID " + nodoArbolID + " no existe.");
                return false; // Nodo no existe
            }
            rsVerificar.close();
            pstmtVerificar.close();

            // 2. Inicializar estructuras de datos
            List<Integer> nodosAEliminar = new ArrayList<>();
            nodosAEliminar.add(nodoArbolID); // Agregar el nodo principal

            Queue<Integer> cola = new LinkedList<>();
            cola.add(nodoArbolID);

            // 3. Identificar todos los descendientes de manera iterativa
            while (!cola.isEmpty()) {
                int nodoActual = cola.poll();

                // Obtener hijos del nodoActual
                pstmtObtenerHijos = connection.prepareStatement(obtenerHijos);
                pstmtObtenerHijos.setInt(1, nodoActual);
                rsHijos = pstmtObtenerHijos.executeQuery();

                while (rsHijos.next()) {
                    int hijo = rsHijos.getInt("nodoHijo");
                    if (!nodosAEliminar.contains(hijo)) {
                        nodosAEliminar.add(hijo);
                        cola.add(hijo); // Agregar para procesar sus hijos
                    }
                }

                rsHijos.close();
                pstmtObtenerHijos.close();
            }

            // 4. Eliminar relaciones padre-hijo para todos los nodos a eliminar
            pstmtEliminarRelaciones = connection.prepareStatement(eliminarRelaciones);
            for (int nodo : nodosAEliminar) {
                pstmtEliminarRelaciones.setInt(1, nodo);
                pstmtEliminarRelaciones.setInt(2, nodo);
                pstmtEliminarRelaciones.executeUpdate();
            }
            pstmtEliminarRelaciones.close();

            // 5. Eliminar entradas en otras tablas según tipoNodo
            for (int nodo : nodosAEliminar) 
            {
                // Obtener el tipoNodo para cada nodo
                pstmtVerificar = connection.prepareStatement(verificarExistenciaNodo);
                pstmtVerificar.setInt(1, nodo);
                rsVerificar = pstmtVerificar.executeQuery();

                String tipoNodoActual = null;
                if (rsVerificar.next()) 
                {
                    tipoNodoActual = rsVerificar.getString("tipoNodo").toLowerCase();
                }
                rsVerificar.close();
                pstmtVerificar.close();

                // Eliminar de tablas específicas según tipoNodo
                if (tipoNodoActual != null) 
                {
                    String deleteQuery = null;
                    switch (tipoNodoActual) 
                    {
                        case "activos":
                            deleteQuery = eliminarActivoId;
                            break;
                        case "variables":
                            deleteQuery = eliminarVariableId;
                            break;
                        case "sentencias":
                            deleteQuery = eliminarSentenciaId;
                            break;
                        default:
                            // Si hay otros tipos de nodos, manejar según corresponda
                            continue; // Saltar si el tipo de nodo no requiere eliminación en otras tablas
                    }

                    if (deleteQuery != null) 
                    {
                        pstmtEliminarSpecific = connection.prepareStatement(deleteQuery);
                        pstmtEliminarSpecific.setInt(1, nodo);
                        int filasEliminadas = pstmtEliminarSpecific.executeUpdate();
                        if (filasEliminadas == 0) 
                        {
                            System.err.println("No se eliminó ningún registro en la tabla específica para nodoArbolId = " + nodo);
                            // Dependiendo de tu lógica, podrías decidir si esto debe fallar la transacción
                        }
                        pstmtEliminarSpecific.close();
                    }
                }
            }

            // 6. Eliminar los nodos de la tabla nodoarbol
            pstmtEliminarNodo = connection.prepareStatement(eliminarNodo);
            for (int nodo : nodosAEliminar) 
            {
                pstmtEliminarNodo.setInt(1, nodo);
                int filasEliminadasNodo = pstmtEliminarNodo.executeUpdate();
                if (filasEliminadasNodo == 0) 
                {
                    System.err.println("No se eliminó el nodo de la tabla nodoarbol para nodoArbolId = " + nodo);
                    // Dependiendo de tu lógica, podrías decidir si esto debe fallar la transacción
                }
            }
            pstmtEliminarNodo.close();

            // 7. Confirmar la transacción
            connection.commit();
            return true; // Eliminación exitosa

        } 
        catch (SQLException e) 
        {
            // Manejar errores y revertir la transacción
            if (connection != null) 
            {
                try 
                {
                    connection.rollback();
                    System.err.println("Transacción revertida debido a un error: " + e.getMessage());
                } 
                catch (SQLException ex) 
                {
                    System.err.println("Error al revertir la transacción: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false; // Fallo en la eliminación
        } 
        finally 
        {
            // Cerrar recursos en caso de que aún estén abiertos
            try 
            {
                if (rsVerificar != null && !rsVerificar.isClosed()) rsVerificar.close();
                if (rsHijos != null && !rsHijos.isClosed()) rsHijos.close();
                if (pstmtVerificar != null && !pstmtVerificar.isClosed()) pstmtVerificar.close();
                if (pstmtObtenerHijos != null && !pstmtObtenerHijos.isClosed()) pstmtObtenerHijos.close();
                if (pstmtEliminarRelaciones != null && !pstmtEliminarRelaciones.isClosed()) pstmtEliminarRelaciones.close();
                if (pstmtEliminarNodo != null && !pstmtEliminarNodo.isClosed()) pstmtEliminarNodo.close();
                if (pstmtEliminarSpecific != null && !pstmtEliminarSpecific.isClosed()) pstmtEliminarSpecific.close();
                if (connection != null && !connection.isClosed()) connection.close();
            } 
            catch (SQLException ex) 
            {
                System.err.println("Error al cerrar recursos: " + ex.getMessage());
            }
        }
    }
    
    
    
    
    
    public boolean actualizarNombreNodoRaiz(String nuevoNombre) 
    {
        // Sentencias SQL
        String sqlCount = "SELECT COUNT(*) AS total FROM nodoNombreRaiz;";
        String sqlInsert = "INSERT INTO nodoNombreRaiz(nombre) VALUES (?);";
        String sqlActualizar = "UPDATE nodoNombreRaiz SET nombre = ?;";

        // Uso de try-with-resources para asegurar el cierre de recursos
        try (Connection conexion = DriverManager.getConnection(url)) 
        {
            
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) 
            {
                // Si el nombre está vacío, verificar si la tabla está vacía
                try (PreparedStatement pstmtCount = conexion.prepareStatement(sqlCount);
                     ResultSet rs = pstmtCount.executeQuery()) 
                {
                    
                    if (rs.next()) 
                    {
                        int count = rs.getInt("total");
                        if (count == 0) 
                        {
                            // La tabla está vacía, insertar "Raiz"
                            try (PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert)) 
                            {
                                pstmtInsert.setString(1, "Raiz");
                                int filasInsertadas = pstmtInsert.executeUpdate();
                                return filasInsertadas > 0;
                            }
                        } 
                        else 
                        {
                            // La tabla no está vacía, no hacer nada
                            System.out.println("La tabla 'nodoNombreRaiz' ya contiene datos.");
                            return false;
                        }
                    }
                }
            } 
            else 
            {
                // Si el nombre no está vacío, proceder a actualizar
                try (PreparedStatement pstmtActualizar = conexion.prepareStatement(sqlActualizar)) 
                {
                    pstmtActualizar.setString(1, nuevoNombre);
                    int filasActualizadas = pstmtActualizar.executeUpdate();
                    return filasActualizadas > 0;
                }
                
            }
        } 
        catch (SQLException e) 
        {
            // Manejo de excepciones
            System.err.println("Error al actualizar o insertar en nodoNombreRaiz: " + e.getMessage());
            return false; // Retornar false en caso de error
        }
        
        return false;
    
    }


    public String obtenerNombreRaiz() 
    {
        String nombreRaiz = "Raiz"; // Valor por defecto si no se encuentra en la BD.
        String query = "SELECT nombre FROM nodoNombreRaiz";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) 
        {
            if (resultSet.next()) {
                nombreRaiz = resultSet.getString("nombre");
            } else {
                // Si no hay registros, se mantiene el valor por defecto "Raiz"
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el nombre raíz: " + e.getMessage());
            // Si hay error, retorna "Raiz" por defecto.
        }
    
        return nombreRaiz;
    }
    
    
    

}

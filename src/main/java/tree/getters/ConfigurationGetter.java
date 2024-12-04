package tree.getters;

public class ConfigurationGetter 
{
    private String nodoNombreRaiz;
    private Boolean permisosUsuario = false; // true para XQOS_Admin, false para XQOS_Readonly

    public String getNodoNombreRaiz() 
    {
        return nodoNombreRaiz;
    }

    public void setNodoNombreRaiz(String nodoNombreRaiz) 
    {
        this.nodoNombreRaiz = nodoNombreRaiz;
    }

    public Boolean getPermisosUsuario()
    {
        return permisosUsuario;
    }
    public void setPermisosUsuario(Boolean permisosUsuario)
    {
        this.permisosUsuario = permisosUsuario;
    }
}

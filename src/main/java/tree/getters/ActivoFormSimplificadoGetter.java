package tree.getters;


public class ActivoFormSimplificadoGetter 
{

    // Campos del formulario como String
    private String tipo;
    private String estado;
    private String monitor;
    private String nombre;

    // Setter para tipo
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Getter para tipo
    public String getTipo() {
        return tipo;
    }

    // Setter para estado
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter para estado
    public String getEstado() {
        return estado;
    }

    // Setter para monitor
    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    // Getter para monitor
    public String getMonitor() {
        return monitor;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setMonitor()
    {
        this.nombre = nombre;
    }
}

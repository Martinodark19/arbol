package tree.getters;

public class SentenciasFormSimplificadoGetter 
{
    private String estado;
    private boolean disponibilidadForm;
    private String query;

    // Getter para estado
    public String getEstado() {
        return estado;
    }

    // Setter para estado
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter para disponibilidadForm
    public boolean isDisponibilidadForm() {
        return disponibilidadForm;
    }

    // Setter para disponibilidadForm
    public void setDisponibilidadForm(boolean disponibilidadForm) {
        this.disponibilidadForm = disponibilidadForm;
    }

    // Método para actualizar estado desde un componente
    public void updateEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("Estado actualizado: " + this.estado);
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String nuevaQuery)
    {
        this.query = nuevaQuery;
    }
}

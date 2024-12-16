package tree.getters;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class VariablesContextoSimplificadoGetter 
{
    // Campos para almacenar los valores

    private String tipoVC;
    private Integer activoId;
    private boolean disponibilidadForm; // Campo para disponibilidad del formulario
    private String valorString;

    // Getter para tipoVC
    public String getTipoVC() {
        return tipoVC;
    }

    // Setter para tipoVC
    public void setTipoVC(String tipoVC) 
    {
        this.tipoVC = tipoVC;
    }

    // Getter para activoId
    public Integer getActivoId() 
    {
        return activoId;
    }

    // Setter para activoId
    public void setActivoId(Integer activoId) {
        this.activoId = activoId;
    }

    // Getter para disponibilidadForm
    public boolean isDisponibilidadForm() 
    {
        return disponibilidadForm;
    }

    // Setter para disponibilidadForm
    public void setDisponibilidadForm(boolean disponibilidadForm) {
        this.disponibilidadForm = disponibilidadForm;
    }

    // Método para actualizar valores directamente desde los componentes del formulario
    public void updateFromForm(JTextField txtTipoVC, JComboBox<Integer> comboActivoId) 
    {
        this.tipoVC = txtTipoVC.getText();
        this.activoId = (Integer) comboActivoId.getSelectedItem();
    }

    public String getValorString()
    {
        return valorString;
    }

    public void setValorString(String valorString)
    {
        this.valorString = valorString;
    }
}

package tree.getters;


import javax.swing.JComboBox;
import javax.swing.JTextField;

public class VariablesContextoSimplificadoGetter 
{

    // Campos para almacenar los valores
    private String tipoVC;
    private String activoId;

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
    public String getActivoId() 
    {
        return activoId;
    }

    // Setter para activoId
    public void setActivoId(String activoId) {
        this.activoId = activoId;
    }

    // Método para actualizar valores directamente desde los componentes del formulario
    public void updateFromForm(JTextField txtTipoVC, JComboBox<String> comboActivoId) {
        this.tipoVC = txtTipoVC.getText();
        this.activoId = (String) comboActivoId.getSelectedItem();
    }
}

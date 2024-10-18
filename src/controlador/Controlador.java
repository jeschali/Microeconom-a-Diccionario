package controlador;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import modelo.micro;
import modelo.microDao;
import vista.Vista;

public class Controlador implements ActionListener {

    microDao dao = new microDao();
    micro p = new micro();
    Vista vista = new Vista();
    DefaultTableModel modelo = new DefaultTableModel();

    public Controlador(Vista v) {
        this.vista = v;
        this.vista.btnListar.addActionListener(this);
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnNuevo.addActionListener(this); 
        listar(vista.Tabla);
        configurarTabla();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnListar) {
            limpiarTabla();
            listar(vista.Tabla);
        }
        if (e.getSource() == vista.btnGuardar) {
            agregar();
            limpiarTabla();
            listar(vista.Tabla);
        }
        if (e.getSource() == vista.btnEditar) {
            int fila = vista.Tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debe Seleccionar Una Fila");
            } else {
                int id = Integer.parseInt((String) vista.Tabla.getValueAt(fila, 0).toString());
                String palabra = (String) vista.Tabla.getValueAt(fila, 1);
                String definicion = (String) vista.Tabla.getValueAt(fila, 2);
                vista.txtId.setText("" + id);
                vista.txtPalabra.setText(palabra);
                vista.txtDefinicion.setText(definicion);
            }
        }
        if (e.getSource() == vista.btnActualizar) {
            Actualizar();
            limpiarTabla();
            listar(vista.Tabla);
        }
        if (e.getSource() == vista.btnEliminar) {
            delete();
            limpiarTabla();
            listar(vista.Tabla);
        }
        if (e.getSource() == vista.btnNuevo) {
            limpiarCampos();
        }
    }

    public void delete(){
        int fila = vista.Tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe Seleccionar Una Palabra");
        } else {
            int id = Integer.parseInt((String) vista.Tabla.getValueAt(fila, 0).toString());
            dao.delete(id);
            JOptionPane.showMessageDialog(vista, "Palabra Eliminada Con Éxito");
        }
    }

    void limpiarTabla() {
        modelo.setRowCount(0); 
    }

    class TextAreaRenderer extends JTextArea implements TableCellRenderer {

        public TextAreaRenderer() {
            setLineWrap(true); 
            setWrapStyleWord(true);
            setMargin(new Insets(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : ""); 
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);

            // Ajustar la altura de la fila según el contenido
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }
            return this;
        }
    }

    public void configurarTabla() {
        vista.Tabla.getColumnModel().getColumn(0).setPreferredWidth(10);  
        vista.Tabla.getColumnModel().getColumn(1).setPreferredWidth(100); 
        vista.Tabla.getColumnModel().getColumn(2).setPreferredWidth(510); 

        vista.Tabla.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); 
        vista.Tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Centrar la columna "PALABRA"
        DefaultTableCellRenderer centerRendererPalabra = new DefaultTableCellRenderer();
        centerRendererPalabra.setHorizontalAlignment(SwingConstants.CENTER); 
        vista.Tabla.getColumnModel().getColumn(1).setCellRenderer(centerRendererPalabra);

        vista.Tabla.setRowHeight(30); 
    }

    public void Actualizar() {
        int id = Integer.parseInt(vista.txtId.getText());
        String palabra = vista.txtPalabra.getText();
        String definicion = vista.txtDefinicion.getText();
        p.setId(id);
        p.setPalabra(palabra);
        p.setDefinicion(definicion);
        int r = dao.Actualizar(p);
        if (r == 1) {
            JOptionPane.showMessageDialog(vista, "Palabra Actualizada Con Éxito");
        } else {
            JOptionPane.showMessageDialog(vista, "Error Al Actualizar La Palabra");
        }
    }

    public void agregar() {
        String palabra = vista.txtPalabra.getText();
        String definicion = vista.txtDefinicion.getText();
        p.setPalabra(palabra);
        p.setDefinicion(definicion);
        int r = dao.agregar(p);
        if (r == 1) {
            JOptionPane.showMessageDialog(vista, "Palabra Agregada Con Éxito");
        } else {
            JOptionPane.showMessageDialog(vista, "Error al agregar la palabra");
        }
    }

    public void listar(JTable tabla) {
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<micro> lista = dao.listar();
        Object[] object = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getPalabra();
            object[2] = lista.get(i).getDefinicion();
            modelo.addRow(object);
        }

        vista.Tabla.setModel(modelo);
    }

    public void limpiarCampos() {
        vista.txtId.setText("");        
        vista.txtPalabra.setText("");
        vista.txtDefinicion.setText("");
    }
}

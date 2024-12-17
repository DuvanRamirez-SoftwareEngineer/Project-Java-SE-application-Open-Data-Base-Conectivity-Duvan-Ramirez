import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FormularioClientes extends JFrame implements ActionListener {
    private JTextField txtCedula, txtNombre, txtDireccion, txtTelefono;
    private JButton btnInsert, btnUpdate, btnDelete, btnSelect;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conexion;

    public FormularioClientes() {
        // Configuración del formulario
        setTitle("CRUD Clientes");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(60, 63, 65)); // Fondo oscuro

        // Configuración de componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Agregar logo (etiqueta)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblLogo = new JLabel("Formulario de Clientes", JLabel.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogo.setForeground(new Color(51, 153, 255));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Espaciado alrededor
        add(lblLogo, gbc);

        // Estilo de texto para las etiquetas
        Color textColor = Color.WHITE;
        Font font = new Font("Arial", Font.BOLD, 14);

        // Cédula Cliente
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(createLabel("Cédula Cliente:", textColor, font), gbc);
        txtCedula = new JTextField();
        addTextField(txtCedula, gbc, 1);

        // Nombre
        gbc.gridy++;
        gbc.gridx = 0;
        add(createLabel("Nombre:", textColor, font), gbc);
        txtNombre = new JTextField();
        addTextField(txtNombre, gbc, 1);

        // Dirección
        gbc.gridy++;
        gbc.gridx = 0;
        add(createLabel("Dirección:", textColor, font), gbc);
        txtDireccion = new JTextField();
        addTextField(txtDireccion, gbc, 1);

        // Teléfono
        gbc.gridy++;
        gbc.gridx = 0;
        add(createLabel("Teléfono:", textColor, font), gbc);
        txtTelefono = new JTextField();
        addTextField(txtTelefono, gbc, 1);

        // Botones
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(60, 63, 65));

        btnInsert = createButton("Insertar", new Color(51, 153, 255));
        buttonPanel.add(btnInsert);

        btnUpdate = createButton("Actualizar", new Color(102, 204, 0));
        buttonPanel.add(btnUpdate);

        btnDelete = createButton("Eliminar", new Color(255, 51, 51));
        buttonPanel.add(btnDelete);

        btnSelect = createButton("Seleccionar", new Color(255, 153, 0));
        buttonPanel.add(btnSelect);

        add(buttonPanel, gbc);

        // Tabla para mostrar los resultados
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;

        String[] columnNames = {"Cédula Cliente", "Nombre", "Dirección", "Teléfono"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setBackground(new Color(43, 43, 43));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(75, 75, 75));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, gbc);

        // Conectar a la base de datos
        conectarBD();

        // Eventos
        btnInsert.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnSelect.addActionListener(this);

        setVisible(true);
    }

    private JLabel createLabel(String text, Color color, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(font);
        return label;
    }

    private void addTextField(JTextField textField, GridBagConstraints gbc, int x) {
        gbc.gridx = x;
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(textField, gbc);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void conectarBD() {
        try {
            String url = "jdbc:mysql://localhost:3306/merca_facil";
            String user = "root";
            String password = "";
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnInsert) {
            insertarCliente();
        } else if (e.getSource() == btnUpdate) {
            actualizarCliente();
        } else if (e.getSource() == btnDelete) {
            eliminarCliente();
        } else if (e.getSource() == btnSelect) {
            seleccionarClientes();
        }
    }

    private void insertarCliente() {
        try {
            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();
            String telefono = txtTelefono.getText();

            String sql = "INSERT INTO clientes (cedula_cliente, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, cedula);
            stmt.setString(2, nombre);
            stmt.setString(3, direccion);
            stmt.setString(4, telefono);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cliente insertado");
            seleccionarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar cliente: " + ex.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();
            String telefono = txtTelefono.getText();

            String sql = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ? WHERE cedula_cliente = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);
            stmt.setString(4, cedula);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cliente actualizado");
            seleccionarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente: " + ex.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
            String cedula = txtCedula.getText();

            String sql = "DELETE FROM clientes WHERE cedula_cliente = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, cedula);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cliente eliminado");
            seleccionarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage());
        }
    }

    private void seleccionarClientes() {
        try {
            String sql = "SELECT * FROM clientes";
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            tableModel.setRowCount(0);
            while (rs.next()) {
                String cedula = rs.getString("cedula_cliente");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");

                tableModel.addRow(new Object[]{cedula, nombre, direccion, telefono});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar clientes: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new FormularioClientes();
    }
}


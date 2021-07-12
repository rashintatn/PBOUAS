package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import model.Rent;
import repository.RentRepository;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RentForm {

	private JFrame frame;
	private JScrollPane scrollPane;
	private JLabel titleLabel;
	private JLabel dateLabel;
	private JLabel dateValueLabel;
	private JLabel timeLabel;
	private JLabel timeValueLabel;
	private JLabel biayaLabel;
	private JLabel rpLabel;
	private JLabel biayaValueLabel;
	private JTextField titleTextField;
	private JButton saveButton;
	private JButton cancelEditButton;
	private JButton returnButton;
	private JButton editButton;
	private JButton deleteButton;
	private JTable table;
	private DefaultTableModel tableModel;
	private String currentEditedRentId = null;
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

	/**
	 * Launch the application.
	 */
	public static void  main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RentForm window = new RentForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RentForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 964, 533);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		titleLabel = new JLabel("Persewaan Buku GWEN");
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		titleLabel.setBounds(368, 11, 238, 25);
		frame.getContentPane().add(titleLabel);
		
		dateLabel = new JLabel("Tanggal : ");
		dateLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		dateLabel.setBounds(10, 44, 72, 25);
		frame.getContentPane().add(dateLabel);
		
		timeLabel = new JLabel("Jam :");
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		timeLabel.setBounds(694, 44, 45, 25);
		frame.getContentPane().add(timeLabel);
		
		dateValueLabel = new JLabel("");
		dateValueLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		dateValueLabel.setBounds(86, 44, 147, 25);
		frame.getContentPane().add(dateValueLabel);
		String currentDate = LocalDate.now().format(dateFormat);
		dateValueLabel.setText(currentDate);
		
		timeValueLabel = new JLabel("");
		timeValueLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		timeValueLabel.setBounds(745, 44, 147, 25);
		frame.getContentPane().add(timeValueLabel);
		String currentTime = LocalTime.now().format(timeFormat);
		timeValueLabel.setText(currentTime);
		
		biayaLabel = new JLabel("Biaya : ");
		biayaLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		biayaLabel.setBounds(570, 104, 72, 21);
		frame.getContentPane().add(biayaLabel);
		
		rpLabel = new JLabel("Rp. ");
		rpLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		rpLabel.setBounds(570, 134, 27, 21);
		frame.getContentPane().add(rpLabel);
		
		biayaValueLabel = new JLabel("");
		biayaValueLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		biayaValueLabel.setBounds(607, 136, 88, 21);
		frame.getContentPane().add(biayaValueLabel);
		
		this.buildForm();
		this.buildButtonAction();
		this.buildRentTable();
	}
	
	public void buildForm() {
		JLabel bookTitleLabel = new JLabel("Judul Buku");
		bookTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bookTitleLabel.setBounds(10, 102, 72, 25);
		frame.getContentPane().add(bookTitleLabel);
		
		titleTextField = new JTextField();
		titleTextField.setBounds(92, 104, 176, 24);
		frame.getContentPane().add(titleTextField);
		titleTextField.setColumns(10);
	}
	
	public void buildButtonAction() {
		saveButton = new JButton("Simpan");
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleSaveButtonClick(e);
			}
		});
		saveButton.setBounds(48, 195, 147, 32);
		frame.getContentPane().add(saveButton);
		
		cancelEditButton = new JButton("Batalkan Edit");
		cancelEditButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleCancelEditButtonClick();
			}
		});
		cancelEditButton.setBounds(205, 195, 115, 32);
		frame.getContentPane().add(cancelEditButton);
		this.cancelEditButton.setVisible(false);
		
		returnButton = new JButton("Kembalikan");
		returnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleReturnButtonClick(e);
			}
		});
		returnButton.setBounds(527, 195, 115, 32);
		frame.getContentPane().add(returnButton);
		
		editButton = new JButton("Edit");
		editButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleEditButtonClick(e);
			}
		});
		editButton.setBounds(652, 195, 115, 32);
		frame.getContentPane().add(editButton);
		
		deleteButton = new JButton("Hapus");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleDeleteButtonClick(e);
			}
		});
		deleteButton.setBounds(777, 195, 115, 32);
		frame.getContentPane().add(deleteButton);
	}
	
	@SuppressWarnings("serial")
	public void buildRentTable() {
		tableModel = new DefaultTableModel(null, new String[] {
			"ID", 
			"Judul Buku", 
			"Tanggal Pinjam", 
			"Tanggal Harus Kembali",
			"Tanggal Kembali",
			"Denda",
			"Biaya Sewa"
		}) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 258, 928, 225);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleRowClicked();
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(tableModel);
		scrollPane.setViewportView(table);
	
		this.fetcTableData();
	}
	public void fetcTableData() {
		ArrayList<Rent> rentList = RentRepository.fetchRents();
		for(Rent rent : rentList) {
			tableModel.addRow(rent.toRow());
		}
	}
	public void handleRowClicked() {
		int row = table.getSelectedRow();
		String biayaSewa = (String) this.tableModel.getValueAt(row, 6);
		this.biayaValueLabel.setText(biayaSewa);
	}
	public void handleSaveButtonClick(MouseEvent e) {
		int saveRent = 0;
		if(this.currentEditedRentId == null) {
			saveRent = RentRepository.addRent(titleTextField.getText());
		} else {
			saveRent = RentRepository.updateBookTitle(this.currentEditedRentId, this.titleTextField.getText());
		}
		if(saveRent == 1) {
			JOptionPane.showMessageDialog(null, "Data Berhasil disimpan");
			System.out.print("Berhasil");
			this.titleTextField.setText("");
			this.cancelEditButton.setVisible(false);
			this.saveButton.setText("Simpan");
			this.currentEditedRentId = null;
			this.tableModel.setRowCount(0);
			this.fetcTableData();
		} else {
			JOptionPane.showMessageDialog(null, "Data Gagal disimpan");
		}
	}
	public void handleDeleteButtonClick(MouseEvent e) {
		int row = this.table.getSelectedRow();
		if(row == -1) {
			JOptionPane.showMessageDialog(null, "Pilih salah satu untuk dihapus");
			return;
		}
		String rentId = this.tableModel.getValueAt(row, 0).toString();
		int removeRent = RentRepository.removeRent(rentId);
		if(removeRent == 1) {
			JOptionPane.showMessageDialog(null, "Data Berhasil dihapus");
			this.tableModel.setRowCount(0);
			this.fetcTableData();
		} else {
			JOptionPane.showMessageDialog(null, "Data Gagal dihapus");
		}
	}
	public void handleReturnButtonClick(MouseEvent e) {
		int row = this.table.getSelectedRow();
		if(row == -1) {
			JOptionPane.showMessageDialog(null, "Pilih salah satu untuk dikembalikan");
			return;
		}
		String tanggalKembali = (String) this.tableModel.getValueAt(row, 4);
		if(tanggalKembali != null) {
			JOptionPane.showMessageDialog(null, "Buku sudah pernah dikembalikan");
			return;
		}
		String rentId = this.tableModel.getValueAt(row, 0).toString();
		LocalDate tanggalHarusKembali = LocalDate.parse(tableModel.getValueAt(row, 3).toString(), this.dateFormat);
		int returnRent = RentRepository.returnRent(rentId, tanggalHarusKembali);
		if(returnRent == 1) {
			JOptionPane.showMessageDialog(null, "Buku Berhasil dikembalikan");
			tableModel.setRowCount(0);
			this.fetcTableData();
		} else {
			JOptionPane.showMessageDialog(null, "Buku gagal dikembalikan");
		}
	}
	public void handleEditButtonClick(MouseEvent e) {
		int row = table.getSelectedRow();
		if(row == -1) {
			JOptionPane.showMessageDialog(null, "Pilih salah satu untuk dikembalikan");
			return;
		}
		String rentId = (String) this.tableModel.getValueAt(row, 0);
		String judul = (String) this.tableModel.getValueAt(row, 1);
		String tanggalPinjam = (String) this.tableModel.getValueAt(row, 2);
		String tanggalHarusKembali = (String) this.tableModel.getValueAt(row, 3);
		String tanggalKembali = (String) this.tableModel.getValueAt(row, 4);
		String denda = (String) this.tableModel.getValueAt(row, 5);
		String biayaSewa = (String) this.tableModel.getValueAt(row, 6);
		if(rentId != null && judul != null && tanggalPinjam != null && tanggalHarusKembali != null && tanggalKembali != null && denda != null && biayaSewa != null) {
			JOptionPane.showMessageDialog(null, "Data sudah lengkap dan tidak bisa diedit");
			return;
		}
		this.titleTextField.setText(judul);
		this.currentEditedRentId = rentId;
		this.saveButton.setText("Simpan Perubahan");
		this.cancelEditButton.setVisible(true);
	}
	public void handleCancelEditButtonClick() {
		this.cancelEditButton.setVisible(false);
		this.currentEditedRentId = null;
		this.saveButton.setText("Simpan");
		this.titleTextField.setText("");
	}
}

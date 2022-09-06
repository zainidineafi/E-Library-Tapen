/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Auth;
import java.net.URL;
import Models.Book;
import Models.Borrowing;
import Models.Member;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class DashboardController extends Controller implements Initializable {

    private Book book;
    private Member member;
    private Borrowing borrowing;

    public DashboardController() {
        super();
        this.book = new Book();
        this.member = new Member();
        this.borrowing = new Borrowing();
    }

    @FXML
    private Label nameBox;

    @FXML
    private Label totalBuku;
    @FXML
    private Label totalAnggota;
    @FXML
    private Label totalTransaksi;

    public void initCounter() {
        try {
            ResultSet books = this.book.getCount();
            if (books.next()) {
                totalBuku.setText(books.getString(1) + " (" + books.getString(2) + ")");
            }

            ResultSet members = this.member.getCount();
            if (members.next()) {
                totalAnggota.setText(members.getString(1));
            }

            ResultSet borrowings = this.borrowing.getCount();
            if (borrowings.next()) {
                totalTransaksi.setText(borrowings.getString(1));
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    @FXML
    private TableView tenMember;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, Integer> totalCol;

    ObservableList<Member> tenMemberList = FXCollections.observableArrayList();

    private void loadData() {
        try {
            ResultSet members = this.member.getTenMember();
            while (members.next()) {
                tenMemberList.add(new Member(
                        members.getString("name"),
                        members.getInt("total")
                ));
                tenMember.setItems(tenMemberList);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void setDataTable() {
        loadData();

        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    @FXML
    private BarChart<String, Number> barChart;

    private void setBarChart() {
        try {
            int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            String[] titleMonths = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Transaksi Bulanan");
            for (int i = 0; i < months.length; i++) {
                series.getData().add(new XYChart.Data<>(titleMonths[i], new Borrowing().getPerMonth(i)));
            }
            barChart.getData().add(series);
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        initCounter();
        setDataTable();
        setBarChart();
    }
}

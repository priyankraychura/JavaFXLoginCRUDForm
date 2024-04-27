package com.example.javafxlogincrudform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CrudForm implements Initializable {

    @FXML
    private Button crud_addBtn;

    @FXML
    private Button crud_clearBtn;

    @FXML
    private TableColumn<StudentData, String> crud_col_course;

    @FXML
    private TableColumn<StudentData, String> crud_col_fullName;

    @FXML
    private TableColumn<StudentData, String> crud_col_gender;

    @FXML
    private TableColumn<StudentData, String> crud_col_studentNumber;

    @FXML
    private TableColumn<StudentData, String> crud_col_year;

    @FXML
    private ComboBox<String> crud_course;

    @FXML
    private Button crud_deleteBtn;

    @FXML
    private TextField crud_fullName;

    @FXML
    private ComboBox<String> crud_gender;

    @FXML
    private TextField crud_studentNumber;

    @FXML
    private TableView<StudentData> crud_tableView;

    @FXML
    private Button crud_updateBtn;

    @FXML
    private ComboBox<String> crud_year;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Alert alert;

    public void studentAddBtn(){

        connect = Database.connect();

        try {
            if (crud_studentNumber.getText().isEmpty() || crud_fullName.getText().isEmpty() ||
                    crud_year.getSelectionModel().getSelectedItem() == null ||
                    crud_course.getSelectionModel().getSelectedItem() == null ||
                    crud_gender.getSelectionModel().getSelectedItem() == null)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT student_number FROM student_info WHERE student_number = " + crud_studentNumber.getText();
                prepare = connect.prepareStatement(checkData);

                result = prepare.executeQuery();

                if (result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Student Number: " + crud_studentNumber.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String inssertData = "INSERT INTO student_info (student_number, full_name, year, course, gender, date)"
                            + "VALUES(?, ?, ?, ?, ?, ?)";
                    System.out.println("Code here");
                    prepare = connect.prepareStatement(inssertData);
                    prepare.setString(1, crud_studentNumber.getText());
                    prepare.setString(2, crud_fullName.getText());
                    prepare.setString(3, (String)crud_year.getSelectionModel().getSelectedItem());
                    prepare.setString(4, (String)crud_course.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String)crud_gender.getSelectionModel().getSelectedItem());

                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(6, String.valueOf(sqlDate));

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added!");
                    alert.showAndWait();

                    prepare.executeUpdate();

                    //TO UPDATE THE TABLE VIEW
                    studentShowData();

                    //TO CLEAT ALL FIELDS
                    studentClearBtn();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void studentUpdateBtn(){
        connect = Database.connect();

        try {
            if (crud_studentNumber.getText().isEmpty() || crud_fullName.getText().isEmpty() ||
                    crud_year.getSelectionModel().getSelectedItem() == null ||
                    crud_course.getSelectionModel().getSelectedItem() == null ||
                    crud_gender.getSelectionModel().getSelectedItem() == null)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure, you want to UPDATE Student Number: " + crud_studentNumber.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    String updateData = "UPDATE student_info SET "
                            + "full_name = '"+ crud_fullName.getText()
                            +"', year = '"+ crud_year.getSelectionModel().getSelectedItem()
                            +"', course = '"+ crud_course.getSelectionModel().getSelectedItem()
                            +"', gender = '"+ crud_gender.getSelectionModel().getSelectedItem()
                            +"' WHERE student_number = " + crud_studentNumber.getText();

                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    //TO UPDATE THE TABLE VIEW
                    studentShowData();

                    //TO CLEAT ALL FIELDS
                    studentClearBtn();
                } else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancllede.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void studentDeleteBtn(){
        connect = Database.connect();

        try {
            if (crud_studentNumber.getText().isEmpty())
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure, you want to DELETE Student Number: " + crud_studentNumber.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    String deleteData = "DELETE FROM student_info WHERE student_number = " + crud_studentNumber.getText();

                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    //TO UPDATE THE TABLE VIEW
                    studentShowData();

                    //TO CLEAT ALL FIELDS
                    studentClearBtn();
                } else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancllede.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void studentClearBtn(){
        crud_studentNumber.setText("");
        crud_fullName.setText("");
        crud_year.getSelectionModel().clearSelection();
        crud_course.getSelectionModel().clearSelection();
        crud_gender.getSelectionModel().clearSelection();
    }

    private String[] yearList = {"1st year", "2nd Year", "3rd Year", "4th year"};
    public void studentYearList(){
        List<String> yList = new ArrayList<>();

        for (String data: yearList){
            yList.add(data);
        }

        ObservableList listData = FXCollections.observableList(yList);
        crud_year.setItems(listData);
    }

    private String[] courseList = {"BSIT", "BSCS", "BSCE"};
    public void studentCourseList(){
        List<String> cList = new ArrayList<>();

        for (String data: courseList){
            cList.add(data);
        }

        ObservableList listData = FXCollections.observableList(cList);
        crud_course.setItems(listData);
    }

    private String[] genderList = {"Male", "Female", "Others"};
    public void studentGenderList(){
        List<String> gList = new ArrayList<>();

        for (String data: genderList){
            gList.add(data);
        }

        ObservableList listData = FXCollections.observableList(gList);
        crud_gender.setItems(listData);
    }

    public ObservableList<StudentData> studentListData(){
        ObservableList<StudentData> listData = FXCollections.observableArrayList();
        String selectDate = "SELECT * FROM student_info";

        connect = Database.connect();

        try {
            prepare = connect.prepareStatement(selectDate);
            result = prepare.executeQuery();

            StudentData sData;

            while (result.next()){
                sData = new StudentData(result.getInt("student_number"),
                                        result.getString("full_name"), result.getString("year"),
                                        result.getString("course"), result.getString("gender"));

                listData.add(sData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listData;
    }

    private ObservableList<StudentData> studentData;
    public void studentShowData(){
        studentData = studentListData();

        crud_col_studentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        crud_col_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        crud_col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        crud_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        crud_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        crud_tableView.setItems(studentData);
    }

    public void studentSelectData(){
        StudentData sData = crud_tableView.getSelectionModel().getSelectedItem();
        int num = crud_tableView.getSelectionModel().getSelectedIndex();

        if(num -1 < -1) return;

        crud_studentNumber.setText(String.valueOf(sData.getStudentNumber()));
        crud_fullName.setText(sData.getFullName());
        crud_year.setValue(sData.getYear());
        crud_course.setValue(sData.getCourse());
        crud_gender.setValue(sData.getGender());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentYearList();
        studentCourseList();
        studentGenderList();
        studentShowData();
    }
}

module com.example.javafxlogincrudform {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.javafxlogincrudform to javafx.fxml;
    exports com.example.javafxlogincrudform;
}
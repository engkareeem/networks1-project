module com.main.networksui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.main.networksui to javafx.fxml;
    exports com.main.networksui;
}
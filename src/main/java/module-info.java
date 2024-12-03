module com.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.game to javafx.fxml;
    exports com.game;
}

module se233.project2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens se233.project2 to javafx.fxml;
    opens se233.project2.controller to javafx.fxml;
    opens se233.project2.assets;

    exports se233.project2;
    exports se233.project2.controller;
}
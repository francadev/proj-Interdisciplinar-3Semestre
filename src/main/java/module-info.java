module projetolp3.floralize {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens projetolp3.floralize to javafx.fxml;
    exports projetolp3.floralize;
    opens projetolp3.floralize.model to javafx.base;

}

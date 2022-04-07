open module oogasalad_app {
    // list all imported class packages since they are dependencies
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    requires java.desktop;
    requires com.google.gson;

    // allow other classes to access listed packages in your project
    exports oogasalad;
}

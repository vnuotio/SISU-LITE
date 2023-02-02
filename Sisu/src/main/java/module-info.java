module fi.tuni.prog3.sisu {
        requires javafx.controls;
        requires javafx.fxml;
        requires com.fasterxml.jackson.databind;
        requires java.net.http;
        requires javafx.web;
        exports fi.tuni.prog3.sisu.models;
        exports fi.tuni.prog3.sisu;
        opens fi.tuni.prog3.sisu.controller to javafx.fxml;
        opens fi.tuni.prog3.sisu.models to com.fasterxml.jackson.databind;
    exports fi.tuni.prog3.sisu.utility;
        exports fi.tuni.prog3.sisu.api;
}



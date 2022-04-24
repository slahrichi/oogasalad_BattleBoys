open module oogasalad_app {
  // list all imported class packages since they are dependencies
  requires javafx.controls;
  requires javafx.graphics;
  requires javafx.base;
  requires javafx.media;
  requires javafx.web;
  requires java.desktop;
  requires gson;
  requires java.sql;
  requires org.apache.logging.log4j;
  requires java.scripting;
  requires jdk.jfr;
  requires stripe.java;
  requires spark.core;
  requires com.google.gson;
  //requires com.google.gson;

// allow other classes to access listed packages in your project
  exports oogasalad.controller;
  exports oogasalad.model.parsing;
  exports oogasalad.model.parsing.parsers;
}

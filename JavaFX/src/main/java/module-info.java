module at.fhv.musicshopfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.desktop;
    requires java.naming;
    requires activemq.all;
    requires beta.jboss.ejb.api_3_2;
    requires sharedrmi;
    //requires org.apache.logging.log4j.core;
    //    requires sharedrmi;

    exports at.fhv.musicshopfx;

    opens at.fhv.musicshopfx to javafx.base, javafx.fxml;
    opens at.fhv.musicshopfx.domain to javafx.base, javafx.fxml;
}
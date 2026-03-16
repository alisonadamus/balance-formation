module com.balanceformation {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires static lombok;
    requires spring.context;
    requires spring.data.jpa;
    requires spring.tx;
    requires spring.jdbc;
    requires spring.orm;
    requires jakarta.validation;
    requires spring.beans;
    requires org.hibernate.orm.core;
    requires jakarta.annotation;
    requires org.apache.poi.ooxml;


    opens com.balanceformation;
    exports com.balanceformation;
    opens com.balanceformation.entity;
    exports com.balanceformation.entity;
    opens com.balanceformation.entity.balance1;
    exports com.balanceformation.entity.balance1;
    opens com.balanceformation.entity.balance1.assets;
    exports com.balanceformation.entity.balance1.assets;
    opens com.balanceformation.entity.balance1.liabilities;
    exports com.balanceformation.entity.balance1.liabilities;
    opens com.balanceformation.entity.balance2;
    exports com.balanceformation.entity.balance2;
    opens com.balanceformation.repository;
    exports com.balanceformation.repository;
    exports com.balanceformation.entity.solvencygroups;
    opens com.balanceformation.entity.solvencygroups;

}
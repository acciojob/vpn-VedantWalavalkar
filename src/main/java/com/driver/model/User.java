package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;

    String username;

    String password;

    String originalIp;

    String maskedIp;

    Boolean connected;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    Country originalCountry;

    @JoinColumn
    @ManyToMany
    List<ServiceProvider> serviceProviderList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Connection> connectionList = new ArrayList<>();

    public User(){}

    public User(int id, String username, String password, String originalIP, String maskedIP) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.originalIp = originalIP;
        this.maskedIp = maskedIP;
    }

    public User(int id, String username, String password, String originalIP, String maskedIP, Boolean connected, Country country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.originalIp = originalIP;
        this.maskedIp = maskedIP;
        this.connected = connected;
        this.originalCountry = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalIp() {
        return originalIp;
    }

    public void setOriginalIp(String originalIp) {
        this.originalIp = originalIp;
    }

    public String getMaskedIp() {
        return maskedIp;
    }

    public void setMaskedIp(String maskedIp) {
        this.maskedIp = maskedIp;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Country getOriginalCountry() {
        return originalCountry;
    }

    public void setOriginalCountry(Country originalCountry) {
        this.originalCountry = originalCountry;
    }

    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }
}

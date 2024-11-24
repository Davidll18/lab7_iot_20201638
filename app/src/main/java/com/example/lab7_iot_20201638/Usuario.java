package com.example.lab7_iot_20201638;

public class Usuario {
    private String name;
    private String lastName;
    private String email;
    private String rol;
    private double saldo;

    public Usuario() {} // Constructor vacío necesario para Firebase

    public Usuario(String name, String lastName, String email, String rol, double saldo) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.rol = rol;
        this.saldo = saldo;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public double getSaldo() {
        return saldo;
    }
}


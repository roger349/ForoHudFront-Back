package com.rer.ForoHubBackEndApp.Models.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Login {
    @NotBlank(message = "El nombre de usuario no debe estar vacío")
    String username;
    @NotBlank(message = "La contraseña no debe estar vacío")
    @Size(min = 6, message = "La contraseña debe tener 6 o mas caracteres")
    String password;
    // Getters y Setters
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}

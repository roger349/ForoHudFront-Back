package com.rer.ForoHubBackEndApp.Errores;

import com.rer.ForoHubBackEndApp.Models.Model.Usuario;

public class ErrorResponse extends Usuario {

    private String mensaje;

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}


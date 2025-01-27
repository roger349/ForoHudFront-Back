package com.rer.ForoHubBackEndApp.Models.Enum;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Categorias {
    Programacion,
    BackEnd,
    FrontEnd,
    DataScience,
    DevOps,
    @JsonEnumDefaultValue
    Desconocido
}


package com.literatura.literatuta.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}

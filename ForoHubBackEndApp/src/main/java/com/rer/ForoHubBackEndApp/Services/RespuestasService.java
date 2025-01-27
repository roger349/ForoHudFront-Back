package com.rer.ForoHubBackEndApp.Services;

import com.rer.ForoHubBackEndApp.Models.Model.Respuestas;
import com.rer.ForoHubBackEndApp.Repository.RespuestasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RespuestasService {

    @Autowired
    RespuestasRepository respuestasRepo;

    public Respuestas guardarRespuesta(Respuestas respuesta) {return respuestasRepo.save(respuesta);}
    public List<Respuestas> getAllRespuestas() {return respuestasRepo.findAll();}
    public Optional<Respuestas> getRespuestaById(Long id) {return respuestasRepo.findById(id);}
    public void deleteRespuesta(Long id) {respuestasRepo.deleteById(id);}
}


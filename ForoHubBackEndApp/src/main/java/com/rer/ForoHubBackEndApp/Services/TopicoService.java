package com.rer.ForoHubBackEndApp.Services;

import com.rer.ForoHubBackEndApp.Models.Model.Topico;
import com.rer.ForoHubBackEndApp.Repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    TopicoRepository topicoRepo;

    public Topico guardarTopico(Topico topico) {return topicoRepo.save(topico);}
    public Page<Topico> getAllTopicos(Pageable pageableConSort){return topicoRepo.findAllTopicoWithRespuestas(pageableConSort);}
    public Optional<Topico> getTopicoById(Long id) {return topicoRepo.findById(id);}
    public void deleteTopico(Long id) {topicoRepo.deleteById(id);}
}


package com.rer.ForoHubBackEndApp.Repository;

import com.rer.ForoHubBackEndApp.Models.Model.Respuestas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestasRepository extends JpaRepository<Respuestas, Long> {
    Page<Respuestas> findByTopicoId(Long topicoId, Pageable pageable);
}

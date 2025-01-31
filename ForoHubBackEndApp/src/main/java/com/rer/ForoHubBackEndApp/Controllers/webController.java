package com.rer.ForoHubBackEndApp.Controllers;

import com.rer.ForoHubBackEndApp.Models.Model.Topico;
import com.rer.ForoHubBackEndApp.Models.Model.Usuario;
import com.rer.ForoHubBackEndApp.Repository.RespuestasRepository;
import com.rer.ForoHubBackEndApp.Repository.TopicoRepository;
import com.rer.ForoHubBackEndApp.Repository.UsuarioRepository;
import com.rer.ForoHubBackEndApp.Services.RespuestasService;
import com.rer.ForoHubBackEndApp.Services.TopicoService;
import com.rer.ForoHubBackEndApp.Services.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class webController {

    @Autowired
    UsuarioService usuarioServ;
    @Autowired
    TopicoService topicoServ;
    @Autowired
    RespuestasService respuestaServ;
    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    TopicoRepository topicoRepo;
    @Autowired
    RespuestasRepository respuestasRepo;

    @PreAuthorize("hasAuthority('LEER_USUARIO')")
    @GetMapping("/usuarios/listarUsuarios")
    public String listarUsuarios(Model model) {
        try {
            List<Usuario> usuarios = usuarioServ.getAllUsuarios();
            model.addAttribute("usuarios", usuarios);
            return "usuarios/listar";  // Nombre de la vista Thymeleaf (listar.html)
        } catch (Exception e) {
            return "error";  // Vista de error si ocurre una excepción
        }
    }
    @PreAuthorize("hasAuthority('LEER_USUARIO')")
    @GetMapping("/usuarios/obtenerUsuarioId/{id}")
    public String obtenerUsuarioPorId(@PathVariable @Min(1) long id, Model model) {
        try {
            Optional<Usuario> usuario = usuarioServ.getUsuarioById(id);
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
                return "usuarios/ver";  // Vista para ver un usuario específico
            } else {
                return "error";  // Si no se encuentra el usuario
            }
        } catch (Exception e) {
            return "error";  // Vista de error si ocurre una excepción
        }
    }
    @PreAuthorize("hasAuthority('ACTUALIZAR_USUARIO')")
    @GetMapping("/usuarios/actualizarUsuarioId/{id}")
    public String mostrarFormularioActualizarUsuario(@PathVariable @Min(1) long id, Model model) {
        try {
            Optional<Usuario> usuarioExistente = usuarioServ.getUsuarioById(id);
            if (usuarioExistente.isPresent()) {
                model.addAttribute("usuario", usuarioExistente.get());
                return "usuarios/editar";  // Vista para editar un usuario
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }
    @PreAuthorize("hasAuthority('ACTUALIZAR_USUARIO')")
    @PostMapping("/usuarios/actualizarUsuarioId/{id}")
    public String actualizarUsuario(@PathVariable @Min(1) long id, @Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "usuarios/editar";  // Si hay errores, mostrar el formulario de nuevo
        }
        try {
            Optional<Usuario> usuarioExistente = usuarioServ.getUsuarioById(id);
            if (usuarioExistente.isPresent()) {
                Usuario usuarioActualizado = usuarioServ.saveUsuario(usuario);
                return "redirect:/usuarios/listarUsuarios";  // Redirigir a la lista de usuarios
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }
    @PreAuthorize("hasAuthority('ELIMINAR_USUARIO')")
    @GetMapping("/usuarios/eliminarUsuarioId/{id}")
    public String eliminarUsuario(@PathVariable @Min(1) long id) {
        try {
            Optional<Usuario> usuarioExistente = usuarioServ.getUsuarioById(id);
            if (usuarioExistente.isPresent()) {
                usuarioServ.deleteUsuario(id);
                return "redirect:/usuarios/listarUsuarios";  // Redirigir después de eliminar
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }
    @PreAuthorize("hasAuthority('LEER_TOPICO')")
    @GetMapping("/topicos/listarTopicos")
    public String listarTopicos(Model model, @PageableDefault(page=0, size=10) Pageable pageable) {
        try {
            Page<Topico> topicos = topicoServ.getAllTopicos(pageable);
            model.addAttribute("topicos", topicos);
            return "topicos/listar";  // Vista para listar los tópicos
        } catch (Exception e) {
            return "error";
        }
    }
    @PreAuthorize("hasAuthority('LEER_TOPICO')")
    @GetMapping("/topicos/verTopico/{id}")
    public String verTopico(@PathVariable @Min(1) long id, Model model) {
        try {
            Optional<Topico> topico = topicoServ.getTopicoById(id);
            if (topico.isPresent()) {
                model.addAttribute("topico", topico.get());
                return "topicos/ver";  // Vista para ver un tópico específico
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }
    @PreAuthorize("hasAuthority('CREAR_TOPICO')")
    @GetMapping("/topicos/crearTopico")
    public String mostrarcrearTopico(Model model) {
        model.addAttribute("topico", new Topico());
        return "topicos/crearTopico";  // Vista para crear un tópico
    }
    @PreAuthorize("hasAuthority('CREAR_TOPICO')")
    @PostMapping("/topicos/crearTopico")
    public String crearTopico(@Valid Topico topico, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("topico", topico);
            return "topicos/crear";  // Si hay errores, mostrar el formulario de nuevo
        }
        try {
            Topico nuevoTopico = new Topico();
            nuevoTopico.setTitulo(topico.getTitulo());
            nuevoTopico.setMensaje(topico.getMensaje());
            // Aquí iría el código adicional de creación y asignación de autor
            topicoServ.guardarTopico(nuevoTopico);
            return "redirect:/topicos/listarTopicos";  // Redirigir después de crear el tópico
        } catch (Exception e) {
            return "error";
        }
    }
    // Otros métodos para editar, eliminar y listar respuestas se adaptarían de forma similar
}










package com.rer.ForoHubBackEndApp.Controllers;

import com.rer.ForoHubBackEndApp.Errores.AdminAlreadyExistsException;
import com.rer.ForoHubBackEndApp.Models.Model.Login;
import com.rer.ForoHubBackEndApp.Models.Model.Usuario;
import com.rer.ForoHubBackEndApp.Repository.UsuarioRepository;
import com.rer.ForoHubBackEndApp.Security.Authenticar;
import com.rer.ForoHubBackEndApp.Security.JwtUtil;
import com.rer.ForoHubBackEndApp.Services.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:8082")
public class LoginController {
    @Autowired
    Authenticar authenticar;
    @Autowired
    UsuarioRepository usuarioRepo;
    @Autowired
    UsuarioService usuarioServ;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    final static Logger logger = LoggerFactory.getLogger(LoginController.class);
    // Mostrar formulario de registro
    @GetMapping("/registrarUsuario")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registrar";  // Vista para registrar un usuario (registrarUsuario.html)
    }
    // Manejar registro de usuario
    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                return "auth/registrar";  // Si hay errores, mostrar el formulario de nuevo
            }
            boolean existeAdmin = usuarioServ.existeAdministrador();
            String rol = usuario.getRol().name().toUpperCase();
            if (existeAdmin && "ADMIN".equals(rol)) {
                model.addAttribute("error", "El usuario con rol ADMIN ya existe.");
                return "auth/registrar";  // Mostrar mensaje de error en el formulario
            } else {
                Usuario usuarioNuevo = new Usuario(usuario.getNombre_usuario(),usuario.getContraseña(), usuario.getCorreo_Electronico(), usuario.getRol());
                usuarioServ.crearUsuario(usuarioNuevo);  // Crear usuario
                return "redirect:/login";  // Redirigir al login
            }
        } catch (AdminAlreadyExistsException e) {
            model.addAttribute("error", "El usuario con rol ADMIN ya existe.");
            return "auth/registrar";  // Mostrar mensaje de error en el formulario
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario.");
            return "auth/registrar";  // En caso de otro error
        }
    }
    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("login", new Login());
        return "auth/login";  // Vista para el login (login.html)
    }
    // Manejar autenticación de usuario
    @PostMapping("/login")
    public String autenticar(@Valid @ModelAttribute("login") Login login,BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                return "auth/login";  // Si hay errores, mostrar el formulario de nuevo
            }
            String username = login.getUsername();
            String password = login.getPassword();
            String jwt = authenticar.loginValidacion(username, password);
            if (jwt != null) {
                model.addAttribute("token", jwt);  // Añadir el token a la vista
                model.addAttribute("username", username);  // Añadir el nombre de usuario a la vista
                return "redirect:/dashboard";  // Redirigir a una página de dashboard después del login exitoso
            } else {
                model.addAttribute("error", "Credenciales incorrectas");
                return "auth/login";  // Mostrar error en el formulario si el login falla
            }
        } catch (Exception e) {
            logger.error("Error durante la autenticación", e);
            model.addAttribute("error", "Error durante la autenticación");
            return "auth/login";  // Mostrar mensaje de error
        }
    }
}


package cl.ipss.saborGourmet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cl.ipss.saborGourmet.services.UsuariosService;

@Controller
public class RegistroController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "USER") String role,
            Model model) {
        
        try {
            usuariosService.registrarUsuario(username, password, role);
            model.addAttribute("mensaje", "Usuario registrado exitosamente");
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}

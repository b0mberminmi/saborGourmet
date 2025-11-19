package cl.ipss.saborGourmet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cl.ipss.saborGourmet.models.Usuarios;
import cl.ipss.saborGourmet.repositories.UsuariosRepository;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuarios registrarUsuario(String username, String password, String role) {
        // Verificar si el usuario ya existe
        if (usuariosRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Crear nuevo usuario con contraseña hasheada
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRole(role != null ? role : "USER");

        return usuariosRepository.save(nuevoUsuario);
    }

    public boolean existeUsuario(String username) {
        return usuariosRepository.findByUsername(username).isPresent();
    }
}

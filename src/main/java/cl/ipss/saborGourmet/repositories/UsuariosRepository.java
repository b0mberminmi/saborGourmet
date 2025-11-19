package cl.ipss.saborGourmet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.ipss.saborGourmet.models.Usuarios;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    
    Optional<Usuarios> findByUsername(String username);
    
}

package cl.ipss.saborGourmet.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.ipss.saborGourmet.models.EstadoReserva;
import cl.ipss.saborGourmet.models.Mesa;
import cl.ipss.saborGourmet.models.Reserva;
import cl.ipss.saborGourmet.models.Usuarios;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Reservas ACTIVAS de un día
    List<Reserva> findByFechaAndEstado(LocalDate fecha, EstadoReserva estado);

    // Reserva activa de un usuario en una fecha
    Optional<Reserva> findByUsuarioAndFechaAndEstado(Usuarios usuario, LocalDate fecha, EstadoReserva estado);

    // Ver si una mesa ya está ocupada ese día
    Optional<Reserva> findByMesaAndFechaAndEstado(Mesa mesa, LocalDate fecha, EstadoReserva estado);

    // Listar reservas de un usuario (para historial si se quiere)
    List<Reserva> findByUsuario(Usuarios usuario);

    // Reservas ACTIVAS de un usuario desde hoy en adelante, ordenadas por fecha
    List<Reserva> findByUsuarioAndEstadoAndFechaGreaterThanEqualOrderByFechaAsc(
            Usuarios usuario,
            EstadoReserva estado,
            LocalDate fechaDesde
    );

}
package cl.ipss.saborGourmet.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.ipss.saborGourmet.models.EstadoReserva;
import cl.ipss.saborGourmet.models.Mesa;
import cl.ipss.saborGourmet.models.Reserva;
import cl.ipss.saborGourmet.models.Usuarios;
import cl.ipss.saborGourmet.repositories.MesaRepository;
import cl.ipss.saborGourmet.repositories.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Valida que la fecha esté entre hoy y los próximos 7 días (incluidos).
     */
    private void validarFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        LocalDate max = hoy.plusDays(7);

        if (fecha == null) {
            throw new RuntimeException("La fecha de la reserva es obligatoria");
        }

        if (fecha.isBefore(hoy)) {
            throw new RuntimeException("No se pueden crear reservas en fechas pasadas");
        }

        if (fecha.isAfter(max)) {
            throw new RuntimeException("Solo puede reservar dentro de los próximos 7 días");
        }
    }

    /**
     * Valida que la cantidad de personas esté entre 1 y 20.
     */
    private void validarCantidadPersonas(int cantidadPersonas) {
        if (cantidadPersonas < 1 || cantidadPersonas > 20) {
            throw new RuntimeException("La cantidad de personas debe estar entre 1 y 20");
        }
    }

    /**
     * Verifica que el usuario no tenga ya una reserva ACTIVA ese día.
     */
    private void validarReservaUnicaPorDia(Usuarios usuario, LocalDate fecha) {
        reservaRepository.findByUsuarioAndFechaAndEstado(usuario, fecha, EstadoReserva.ACTIVA)
                .ifPresent(r -> {
                    throw new RuntimeException("Ya tienes una reserva activa para ese día");
                });
    }

    /**
     * Obtiene las mesas disponibles (activas y sin reserva ACTIVA) para una fecha.
     */
    public List<Mesa> obtenerMesasDisponiblesParaFecha(LocalDate fecha) {
        validarFecha(fecha);

        List<Mesa> mesasActivas = mesaRepository.findByActivaTrue();
        List<Reserva> reservasDelDia = reservaRepository.findByFechaAndEstado(fecha, EstadoReserva.ACTIVA);

        Set<Long> idsMesasOcupadas = new HashSet<>();
        for (Reserva r : reservasDelDia) {
            idsMesasOcupadas.add(r.getMesa().getId());
        }

        // Filtramos las mesas activas que NO estén en las ocupadas
        return mesasActivas.stream()
                .filter(m -> !idsMesasOcupadas.contains(m.getId()))
                .toList();
    }

    /**
     * Crea una reserva asignando automáticamente una mesa disponible.
     */
    public Reserva crearReserva(Usuarios usuario, LocalDate fecha, int cantidadPersonas) {
        validarFecha(fecha);
        validarCantidadPersonas(cantidadPersonas);
        validarReservaUnicaPorDia(usuario, fecha);

        List<Mesa> mesasDisponibles = obtenerMesasDisponiblesParaFecha(fecha);

        if (mesasDisponibles.isEmpty()) {
            throw new RuntimeException("No hay mesas disponibles para la fecha seleccionada");
        }

        Mesa mesaAsignada = mesasDisponibles.get(0); // Tomamos la primera disponible

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setMesa(mesaAsignada);
        nuevaReserva.setFecha(fecha);
        nuevaReserva.setEstado(EstadoReserva.ACTIVA);
        nuevaReserva.setCantidadPersonas(cantidadPersonas);

        return reservaRepository.save(nuevaReserva);
    }

    /**
     * Cancela una reserva (usuario o admin), cambiando su estado a CANCELADA.
     */
    public void cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            return; // ya está cancelada
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    /**
     * Reservas por fecha (para admin).
     */
    public List<Reserva> obtenerReservasPorFecha(LocalDate fecha) {
        validarFecha(fecha);
        return reservaRepository.findByFechaAndEstado(fecha, EstadoReserva.ACTIVA);
    }

    /**
     * Reservas de un usuario (historial).
     */
    public List<Reserva> obtenerReservasDeUsuario(Usuarios usuario) {
        return reservaRepository.findByUsuario(usuario);
    }

    /**
     * Obtener todas las reservas (por si se necesitan para panel admin).
     */
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> obtenerReservasActivasFuturasDeUsuario(Usuarios usuario) {
        LocalDate hoy = LocalDate.now();
        return reservaRepository.findByUsuarioAndEstadoAndFechaGreaterThanEqualOrderByFechaAsc(
                usuario,
                EstadoReserva.ACTIVA,
                hoy
        );
    }
}
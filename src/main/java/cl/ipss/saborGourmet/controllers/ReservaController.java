package cl.ipss.saborGourmet.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.ipss.saborGourmet.models.Reserva;
import cl.ipss.saborGourmet.models.Usuarios;
import cl.ipss.saborGourmet.services.ReservaService;
import cl.ipss.saborGourmet.services.UsuariosService;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuariosService usuariosService;

    /**
     * Mostrar los próximos 7 días disponibles para reservar.
     */
    @GetMapping("/reservas")
    public String vistaReservas(Model model) {
        List<LocalDate> dias = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (int i = 0; i <= 7; i++) {
            dias.add(hoy.plusDays(i));
        }

        model.addAttribute("dias", dias);
        return "reservas";
    }

    /**
     * Ver disponibilidad de mesas en una fecha.
     */
    @GetMapping("/reservas/disponibilidad")
    public String disponibilidad(
            @RequestParam("fecha") String fechaStr,
            Model model) {

        LocalDate fecha = LocalDate.parse(fechaStr);

        model.addAttribute("fecha", fecha);
        model.addAttribute("mesasDisponibles", reservaService.obtenerMesasDisponiblesParaFecha(fecha));

        return "reservas-detalle";
    }

    /**
     * Crear una reserva.
     */
    @PostMapping("/reservas/crear")
    public String crearReserva(
            @RequestParam("fecha") String fechaStr,
            @RequestParam("cantidadPersonas") int cantidadPersonas,
            Authentication auth,
            Model model) {

        LocalDate fecha = LocalDate.parse(fechaStr);

        Usuarios usuario = usuariosService
                .existeUsuario(auth.getName())
                ? usuariosService.obtenerPorUsername(auth.getName())
                : null;

        if (usuario == null) {
            model.addAttribute("error", "No se pudo obtener el usuario autenticado.");
            return "reservas";
        }

        try {
            reservaService.crearReserva(usuario, fecha, cantidadPersonas);
            return "redirect:/reservas?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "reservas-detalle";
        }
    }

    /**
     * Cancelar una reserva por ID.
     */
    @PostMapping("/reservas/cancelar")
    public String cancelar(
            @RequestParam("idReserva") Long idReserva) {

        reservaService.cancelarReserva(idReserva);
        return "redirect:/reservas?cancelada";
    }

        /**
     * Ver las reservas activas del usuario desde hoy en adelante.
     */
    @GetMapping("/mis-reservas")
    public String misReservas(Authentication auth, Model model) {

        Usuarios usuario = usuariosService.obtenerPorUsername(auth.getName());

        model.addAttribute("reservas",
                reservaService.obtenerReservasActivasFuturasDeUsuario(usuario));

        model.addAttribute("username", usuario.getUsername());

        return "mis-reservas";
    }

    /**
     * Cancelar una reserva desde la vista "Mis reservas".
     */
    @PostMapping("/mis-reservas/cancelar")
    public String cancelarDesdeMisReservas(
            @RequestParam("idReserva") Long idReserva) {

        reservaService.cancelarReserva(idReserva);
        return "redirect:/mis-reservas?cancelada";
    }
}
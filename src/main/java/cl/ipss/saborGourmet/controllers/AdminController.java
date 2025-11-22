package cl.ipss.saborGourmet.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.ipss.saborGourmet.models.Mesa;
import cl.ipss.saborGourmet.models.Reserva;
import cl.ipss.saborGourmet.services.MesaService;
import cl.ipss.saborGourmet.services.ReservaService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private ReservaService reservaService;

    // Vista principal del panel admin
    @GetMapping
    public String panel() {
        return "admin/panel";
    }

    // ----- GESTIÓN DE MESAS -----

    @GetMapping("/mesas")
    public String listarMesas(Model model) {
        List<Mesa> mesas = mesaService.obtenerTodasLasMesas();
        model.addAttribute("mesas", mesas);
        return "admin/mesas";
    }

    @PostMapping("/mesas")
    public String crearMesa(@RequestParam("nombre") String nombre) {
        Mesa mesa = new Mesa();
        mesa.setNombre(nombre);
        mesa.setActiva(true);
        mesaService.guardarMesa(mesa);
        return "redirect:/admin/mesas?creada";
    }

    @PostMapping("/mesas/estado")
    public String cambiarEstadoMesa(
            @RequestParam("idMesa") Long idMesa,
            @RequestParam("activa") boolean activa) {

        mesaService.cambiarEstadoMesa(idMesa, activa);
        return "redirect:/admin/mesas?actualizada";
    }

    // ----- GESTIÓN DE RESERVAS -----

    @GetMapping("/reservas")
    public String reservasPorFecha(
            @RequestParam(name = "fecha", required = false) String fechaStr,
            Model model) {

        LocalDate fechaConsulta;

        if (fechaStr == null || fechaStr.isBlank()) {
            fechaConsulta = LocalDate.now();
        } else {
            fechaConsulta = LocalDate.parse(fechaStr);
        }

        List<Reserva> reservas = reservaService.obtenerReservasPorFecha(fechaConsulta);

        model.addAttribute("fecha", fechaConsulta);
        model.addAttribute("reservas", reservas);

        return "admin/reservas";
    }

    @PostMapping("/reservas/cancelar")
    public String cancelarReservaAdmin(
            @RequestParam("idReserva") Long idReserva,
            @RequestParam(name = "fecha", required = false) String fechaStr) {

        reservaService.cancelarReserva(idReserva);

        if (fechaStr != null && !fechaStr.isBlank()) {
            return "redirect:/admin/reservas?fecha=" + fechaStr + "&cancelada";
        }

        return "redirect:/admin/reservas?cancelada";
    }
}
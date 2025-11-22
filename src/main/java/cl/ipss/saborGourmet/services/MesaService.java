package cl.ipss.saborGourmet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.ipss.saborGourmet.models.Mesa;
import cl.ipss.saborGourmet.repositories.MesaRepository;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> obtenerTodasLasMesas() {
        return mesaRepository.findAll();
    }

    public List<Mesa> obtenerMesasActivas() {
        return mesaRepository.findByActivaTrue();
    }

    public Mesa guardarMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public Mesa cambiarEstadoMesa(Long idMesa, boolean activa) {
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        mesa.setActiva(activa);
        return mesaRepository.save(mesa);
    }

    public Mesa buscarPorId(Long idMesa) {
        return mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
    }
}
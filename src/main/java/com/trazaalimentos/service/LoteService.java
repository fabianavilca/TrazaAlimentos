package com.trazaalimentos.service;

import com.trazaalimentos.entity.Lote;
import com.trazaalimentos.entity.Producto;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.entity.Bloque;
import com.trazaalimentos.repository.LoteRepository;
import com.trazaalimentos.repository.ProductoRepository;
import com.trazaalimentos.repository.UsuarioRepository;
import com.trazaalimentos.util.SHA256Util;
import com.trazaalimentos.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoteService {
    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private BlockchainService blockchainService;

    public Lote crearLote(Lote lote, Long productorId, Long productoId) {
        Optional<Usuario> productor = usuarioRepository.findById(productorId);
        Optional<Producto> producto = productoRepository.findById(productoId);

        if (!productor.isPresent() || !producto.isPresent()) {
            throw new RuntimeException("Productor o Producto no encontrado");
        }

        if (!productor.get().getRol().equals(Usuario.RolUsuario.PRODUCTOR)) {
            throw new RuntimeException("Solo productores pueden crear lotes");
        }

        // Generar código de lote único
        String codigoLote = "LOTE-" + System.currentTimeMillis();
        lote.setCodigoLote(codigoLote);
        lote.setProductor(productor.get());
        lote.setProducto(producto.get());

        // Generar hash inicial
        String hashInicial = SHA256Util.generateInitialHash();
        lote.setHashInicial(hashInicial);

        // Generar código QR con el código del lote
        String datosQR = "Lote: " + codigoLote + ", Producto: " + producto.get().getNombre();
        byte[] codigoQR = qrCodeGenerator.generateQRCodeImage(datosQR);
        lote.setCodigoQr(codigoQR);

        lote.setEstado(Lote.EstadoLote.CREADO);
        Lote loteSaved = loteRepository.save(lote);

        // Crear bloque inicial
        Map<String, Object> datosBloque = new HashMap<>();
        datosBloque.put("accion", "CREACION_LOTE");
        datosBloque.put("codigoLote", codigoLote);
        datosBloque.put("producto", producto.get().getNombre());
        datosBloque.put("cantidad", lote.getCantidad());
        datosBloque.put("unidad", lote.getUnidad());

        blockchainService.crearBloque(loteSaved, productor.get(), "CREACION_LOTE", datosBloque);

        return loteSaved;
    }

    public Optional<Lote> obtenerLotePorId(Long id) {
        return loteRepository.findById(id);
    }

    public Optional<Lote> obtenerLotePorCodigo(String codigoLote) {
        return loteRepository.findByCodigoLote(codigoLote);
    }

    public List<Lote> obtenerLotesDelProductor(Long productorId) {
        return loteRepository.findByProductorId(productorId);
    }

    public List<Lote> obtenerTodosLosLotes() {
        return loteRepository.findAll();
    }

    public Lote actualizarEstadoLote(Long loteId, Lote.EstadoLote nuevoEstado) {
        Optional<Lote> loteOptional = loteRepository.findById(loteId);
        if (loteOptional.isPresent()) {
            Lote lote = loteOptional.get();
            lote.setEstado(nuevoEstado);
            return loteRepository.save(lote);
        }
        throw new RuntimeException("Lote no encontrado");
    }
}
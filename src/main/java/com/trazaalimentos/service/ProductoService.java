package com.trazaalimentos.service;

import com.trazaalimentos.entity.Producto;
import com.trazaalimentos.entity.Usuario;
import com.trazaalimentos.repository.ProductoRepository;
import com.trazaalimentos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Producto crearProducto(Producto producto, Long productorId) {
        Optional<Usuario> productor = usuarioRepository.findById(productorId);
        if (!productor.isPresent()) {
            throw new RuntimeException("Productor no encontrado");
        }
        if (!productor.get().getRol().equals(Usuario.RolUsuario.PRODUCTOR)) {
            throw new RuntimeException("Solo los productores pueden crear productos");
        }
        producto.setProductor(productor.get());
        return productoRepository.save(producto);
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> obtenerProductosPorProductor(Long productorId) {
        return productoRepository.findByProductorId(productorId);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setTipoProducto(productoActualizado.getTipoProducto());
            return productoRepository.save(producto);
        }
        throw new RuntimeException("Producto no encontrado");
    }
}
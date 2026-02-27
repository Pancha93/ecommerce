package SistemadeGestiondeOrdenes.entity.services.impl;

import SistemadeGestiondeOrdenes.entity.dto.OrdenDTO;
import SistemadeGestiondeOrdenes.entity.dto.OrdenItemDTO;
import SistemadeGestiondeOrdenes.entity.dto.CrearOrdenRequest;
import SistemadeGestiondeOrdenes.entity.dto.DireccionDTO;
import SistemadeGestiondeOrdenes.entity.entities.*;
import SistemadeGestiondeOrdenes.entity.repositories.OrdenRepository;
import SistemadeGestiondeOrdenes.entity.repositories.OrdenItemRepository;
import SistemadeGestiondeOrdenes.entity.repositories.CarritoRepository;
import SistemadeGestiondeOrdenes.entity.services.OrdenService;
import SistemadeGestiondeOrdenes.entity.services.CarritoService;
import SistemadeGestiondeOrdenes.entity.services.DireccionService;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final OrdenItemRepository ordenItemRepository;
    private final CarritoRepository carritoRepository;
    private final UserRepository usuarioRepository;
    private final CarritoService carritoService;
    private final DireccionService direccionService;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenDTO> obtenerOrdenesUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenDTO obtenerPorId(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        return convertirADTO(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenDTO obtenerPorNumero(String numeroOrden) {
        Orden orden = ordenRepository.findByNumeroOrden(numeroOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con número: " + numeroOrden));
        return convertirADTO(orden);
    }

    @Override
    public OrdenDTO crearDesdeCarrito(Long usuarioId, CrearOrdenRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Verificar stock
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock para: " + producto.getNombre());
            }
        }

        // Crear orden
        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setEstado(Orden.EstadoOrden.PENDIENTE);
        orden.setMetodoPago(request.getMetodoPago());
        orden.setNotas(request.getNotas());

        // Calcular totales
        BigDecimal subtotal = carrito.getItems().stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orden.setSubtotal(subtotal);
        orden.setCostoEnvio(BigDecimal.ZERO);
        orden.setImpuestos(subtotal.multiply(new BigDecimal("0.16"))); // 16% IVA
        orden.setTotal(subtotal.add(orden.getImpuestos()).add(orden.getCostoEnvio()));

        // Establecer dirección
        if (request.getDireccionEnvioId() != null) {
            DireccionDTO direccionDTO = direccionService.obtenerPorId(request.getDireccionEnvioId());
            Direccion direccion = new Direccion();
            direccion.setId(direccionDTO.getId());
            orden.setDireccionEnvio(direccion);
        }

        orden = ordenRepository.save(orden);

        // Crear items de la orden
        for (CarritoItem carritoItem : carrito.getItems()) {
            OrdenItem ordenItem = new OrdenItem();
            ordenItem.setOrden(orden);
            ordenItem.setProducto(carritoItem.getProducto());
            ordenItem.setCantidad(carritoItem.getCantidad());
            ordenItem.setPrecioUnitario(carritoItem.getPrecioUnitario());
            ordenItemRepository.save(ordenItem);

            // Actualizar stock
            Producto producto = carritoItem.getProducto();
            producto.setStock(producto.getStock() - carritoItem.getCantidad());
        }

        // Limpiar carrito
        carritoService.limpiarCarrito(usuarioId);

        return convertirADTO(ordenRepository.findById(orden.getId()).orElseThrow());
    }

    @Override
    public OrdenDTO actualizarEstado(Long id, String nuevoEstado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));

        try {
            Orden.EstadoOrden estado = Orden.EstadoOrden.valueOf(nuevoEstado.toUpperCase());
            orden.setEstado(estado);
            orden = ordenRepository.save(orden);
            return convertirADTO(orden);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de orden inválido: " + nuevoEstado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenDTO> obtenerPorEstado(String estado) {
        try {
            Orden.EstadoOrden estadoOrden = Orden.EstadoOrden.valueOf(estado.toUpperCase());
            return ordenRepository.findByEstado(estadoOrden).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de orden inválido: " + estado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenDTO> obtenerTodas() {
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private OrdenDTO convertirADTO(Orden orden) {
        List<OrdenItemDTO> items = orden.getItems().stream()
                .map(this::convertirItemADTO)
                .collect(Collectors.toList());

        DireccionDTO direccionDTO = null;
        if (orden.getDireccionEnvio() != null) {
            try {
                direccionDTO = direccionService.obtenerPorId(orden.getDireccionEnvio().getId());
            } catch (Exception e) {
                // Dirección no disponible
            }
        }

        return OrdenDTO.builder()
                .id(orden.getId())
                .numeroOrden(orden.getNumeroOrden())
                .usuarioId(orden.getUsuario().getId())
                .usuarioNombre(orden.getUsuario().getName())
                .estado(orden.getEstado().toString())
                .subtotal(orden.getSubtotal())
                .costoEnvio(orden.getCostoEnvio())
                .impuestos(orden.getImpuestos())
                .total(orden.getTotal())
                .metodoPago(orden.getMetodoPago())
                .notas(orden.getNotas())
                .fechaCreacion(orden.getFechaCreacion())
                .fechaActualizacion(orden.getFechaActualizacion())
                .direccionEnvio(direccionDTO)
                .items(items)
                .build();
    }

    private OrdenItemDTO convertirItemADTO(OrdenItem item) {
        Producto producto = item.getProducto();
        String imagenUrl = producto.getImagenes().stream()
                .filter(ProductoImagen::getEsPrincipal)
                .findFirst()
                .map(ProductoImagen::getUrl)
                .orElse(producto.getImagenes().isEmpty() ? null : producto.getImagenes().get(0).getUrl());

        return OrdenItemDTO.builder()
                .id(item.getId())
                .productoId(producto.getId())
                .productoNombre(producto.getNombre())
                .productoImagen(imagenUrl)
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }
}

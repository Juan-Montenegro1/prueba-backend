package prueba_backend.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.slf4j.Slf4j;
import prueba_backend.demo.dto.ProductoMayorStock;
import prueba_backend.demo.models.Franquicia;
import prueba_backend.demo.models.Producto;
import prueba_backend.demo.models.Sucursal;
import prueba_backend.demo.repositories.FranquisiaRepository;
import prueba_backend.demo.repositories.ProductoRepository;
import prueba_backend.demo.repositories.SucursalRepository;

@Slf4j
@Service
public class FranquiciaService implements IFranquiciaService {
    
    private final FranquisiaRepository franquiciaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;

    public FranquiciaService(FranquisiaRepository franquiciaRepository, 
                            SucursalRepository sucursalRepository, 
                            ProductoRepository productoRepository) {
        this.franquiciaRepository = franquiciaRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
        log.info("FranquiciaService inicializado");
    }

    @Override
    public Franquicia crearFranquicia(Franquicia franquicia) {
        log.info("Creando franquicia: {}", franquicia.getNombre());
        Franquicia saved = franquiciaRepository.save(franquicia);
        log.info("Franquicia creada con ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Sucursal agregarSucursal(Long franquiciaId, Sucursal sucursal) {
        log.info("Agregando sucursal '{}' a franquicia ID: {}", sucursal.getNombre(), franquiciaId);
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> {
                    log.error("Franquicia no encontrada con ID: {}", franquiciaId);
                    return new RuntimeException("Franquicia no encontrada");
                });
        sucursal.setFranquicia(franquicia);
        Sucursal saved = sucursalRepository.save(sucursal);
        log.info("Sucursal agregada con ID: {} a franquicia ID: {}", saved.getId(), franquiciaId);
        return saved;
    }

    @Override
    public Producto agregarProducto(Long sucursalId, Producto producto) {
        log.info("Agregando producto '{}' a sucursal ID: {}", producto.getNombre(), sucursalId);
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> {
                    log.error("Sucursal no encontrada con ID: {}", sucursalId);
                    return new RuntimeException("Sucursal no encontrada");
                });
        producto.setSucursal(sucursal);
        Producto saved = productoRepository.save(producto);
        log.info("Producto agregado con ID: {} a sucursal ID: {}", saved.getId(), sucursalId);
        return saved;
    }

    @Override
    public void eliminarProducto(@PathVariable Long productoId) {
        log.info("Eliminando producto ID: {}", productoId);
        if (!productoRepository.existsById(productoId)) {
            log.error("Producto no encontrado con ID: {}", productoId);
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(productoId);
        log.info("Producto ID: {} eliminado", productoId);
    }

    @Override
    public Producto actualizarStock(@PathVariable Long productoId,  @RequestBody int stockActualizado) {
        log.info("Actualizando stock del producto ID: {} a {}", productoId, stockActualizado);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado con ID: {}", productoId);
                    return new RuntimeException("Producto no encontrado");
                });
        producto.setStock(stockActualizado);
        Producto saved = productoRepository.save(producto);
        log.info("Stock del producto ID: {} actualizado a {}", productoId, saved.getStock());
        return saved;
    }

    @Override
    public List<ProductoMayorStock> obtenerProductoMayorStock(Long franquiciaId) {
        log.info("Obteniendo productos con mayor stock para franquicia ID: {}", franquiciaId);
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> {
                    log.error("Franquicia no encontrada con ID: {}", franquiciaId);
                    return new RuntimeException("Franquicia no encontrada");
                });
        
        List<ProductoMayorStock> result = franquicia.getSucursales().stream()
                .filter(sucursal -> !sucursal.getProductos().isEmpty())
                .map(sucursal -> {
                    Producto productoMayorStock = sucursal.getProductos().stream()
                            .max((p1, p2) -> Integer.compare(p1.getStock(), p2.getStock()))
                            .orElse(null);
                    if (productoMayorStock != null) {
                        return new ProductoMayorStock(
                                sucursal.getNombre(),
                                productoMayorStock.getNombre(),
                                productoMayorStock.getStock()
                        );
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .toList();
        log.info("Encontrados {} productos con mayor stock para franquicia ID: {}", result.size(), franquiciaId);
        return result;
    }
}

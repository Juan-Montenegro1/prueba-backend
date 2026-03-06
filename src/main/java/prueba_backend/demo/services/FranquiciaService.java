package prueba_backend.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import prueba_backend.demo.dto.ProductoMayorStock;
import prueba_backend.demo.models.Franquicia;
import prueba_backend.demo.models.Producto;
import prueba_backend.demo.models.Sucursal;
import prueba_backend.demo.repositories.FranquisiaRepository;
import prueba_backend.demo.repositories.ProductoRepository;
import prueba_backend.demo.repositories.SucursalRepository;




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
    }

    @Override
    public Franquicia crearFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }

    @Override
    public Sucursal agregarSucursal(Long franquiciaId, Sucursal sucursal) {
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
        sucursal.setFranquicia(franquicia);
        return sucursalRepository.save(sucursal);
    }

    @Override
    public Producto agregarProducto(Long sucursalId, Producto producto) {
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
        producto.setSucursal(sucursal);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(@PathVariable Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(productoId);
    }

    @Override
    public Producto actualizarStock(@PathVariable Long productoId,  @RequestBody int stockActualizado) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(stockActualizado);
        return productoRepository.save(producto);
    }

    @Override
    public List<ProductoMayorStock> obtenerProductoMayorStock(Long franquiciaId) {
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));
        
        return franquicia.getSucursales().stream()
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
    }
    
    
}

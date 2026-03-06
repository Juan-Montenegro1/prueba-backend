package prueba_backend.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import prueba_backend.demo.dto.CrearFranquiciaRequest;
import prueba_backend.demo.dto.ProductoMayorStock;
import prueba_backend.demo.models.Franquicia;
import prueba_backend.demo.models.Producto;
import prueba_backend.demo.models.Sucursal;
import prueba_backend.demo.services.FranquiciaService;

@Slf4j
@RestController
@RequestMapping("/api")
public class FranquiciaController {
    
    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
        log.info("FranquiciaController inicializado");
    }

    @PostMapping("/franquicias")
    public ResponseEntity<Franquicia> crearFranquicia(@RequestBody CrearFranquiciaRequest request) {
        log.info("Iniciando creación de franquicia con nombre: {}", request.getNombre());
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(request.getNombre());
        Franquicia saved = franquiciaService.crearFranquicia(franquicia);
        log.info("Franquicia creada exitosamente con ID: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/franquicias/{franquiciaId}/sucursales")
    public ResponseEntity<?> agregarSucursal(@PathVariable Long franquiciaId, @RequestBody Sucursal sucursal) {
        log.info("Agregando sucursal '{}' a franquicia ID: {}", sucursal.getNombre(), franquiciaId);
        try {
            Sucursal saved = franquiciaService.agregarSucursal(franquiciaId, sucursal);
            log.info("Sucursal agregada exitosamente con ID: {} a franquicia ID: {}", saved.getId(), franquiciaId);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            log.error("Error al agregar sucursal a franquicia ID: {}: {}", franquiciaId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/sucursales/{sucursalId}/productos")
    public ResponseEntity<?> agregarProducto(@PathVariable Long sucursalId, @RequestBody Producto producto) {
        log.info("Agregando producto '{}' a sucursal ID: {}", producto.getNombre(), sucursalId);
        try {
            Producto saved = franquiciaService.agregarProducto(sucursalId, producto);
            log.info("Producto agregado exitosamente con ID: {} a sucursal ID: {}", saved.getId(), sucursalId);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            log.error("Error al agregar producto a sucursal ID: {}: {}", sucursalId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/productos/{productoId}/eliminar")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long productoId) {
        log.info("Eliminando producto ID: {}", productoId);
        try {
            franquiciaService.eliminarProducto(productoId);
            log.info("Producto ID: {} eliminado exitosamente", productoId);
            return ResponseEntity.ok("Producto eliminado");
        } catch (RuntimeException e) {
            log.error("Error al eliminar producto ID: {}: {}", productoId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/productos/{productoId}/actualizar-stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long productoId, @RequestBody int nuevoStock) {
        log.info("Actualizando stock del producto ID: {} a {}", productoId, nuevoStock);
        try {
            Producto updated = franquiciaService.actualizarStock(productoId, nuevoStock);
            log.info("Stock del producto ID: {} actualizado a {}", productoId, updated.getStock());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Error al actualizar stock del producto ID: {}: {}", productoId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/franquicias/{franquiciaId}/producto-mayor-stock")
    public ResponseEntity<?> obtenerProductoMayorStock(@PathVariable Long franquiciaId) {
        log.info("Obteniendo productos con mayor stock para franquicia ID: {}", franquiciaId);
        try {
            List<ProductoMayorStock> result = franquiciaService.obtenerProductoMayorStock(franquiciaId);
            log.info("Se encontraron {} productos con mayor stock para franquicia ID: {}", result.size(), franquiciaId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Error al obtener productos con mayor stock para franquicia ID: {}: {}", franquiciaId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

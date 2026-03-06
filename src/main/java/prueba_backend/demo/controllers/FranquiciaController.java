package prueba_backend.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prueba_backend.demo.dto.CrearFranquiciaRequest;
import prueba_backend.demo.models.Franquicia;
import prueba_backend.demo.models.Producto;
import prueba_backend.demo.models.Sucursal;
import prueba_backend.demo.services.FranquiciaService;

@RestController
@RequestMapping("/api")
public class FranquiciaController {
    
    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @PostMapping("/franquicias")
    public ResponseEntity<Franquicia> crearFranquicia(@RequestBody CrearFranquiciaRequest request) {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(request.getNombre());
        Franquicia saved = franquiciaService.crearFranquicia(franquicia);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/franquicias/{franquiciaId}/sucursales")
    public ResponseEntity<?> agregarSucursal(@PathVariable Long franquiciaId, @RequestBody Sucursal sucursal) {
        try {
            return ResponseEntity.ok(franquiciaService.agregarSucursal(franquiciaId, sucursal));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/sucursales/{sucursalId}/productos")
    public ResponseEntity<?> agregarProducto(@PathVariable Long sucursalId, @RequestBody Producto producto) {
        try {
            return ResponseEntity.ok(franquiciaService.agregarProducto(sucursalId, producto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/productos/{productoId}/eliminar")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long productoId) {
        try {
            franquiciaService.eliminarProducto(productoId);
            return ResponseEntity.ok("Producto eliminado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/productos/{productoId}/actualizar-stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long productoId, @RequestBody int nuevoStock) {
        try {
            return ResponseEntity.ok(franquiciaService.actualizarStock(productoId, nuevoStock));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/franquicias/{franquiciaId}/producto-mayor-stock")
    public ResponseEntity<?> obtenerProductoMayorStock(@PathVariable Long franquiciaId) {
        try {
            return ResponseEntity.ok(franquiciaService.obtenerProductoMayorStock(franquiciaId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}

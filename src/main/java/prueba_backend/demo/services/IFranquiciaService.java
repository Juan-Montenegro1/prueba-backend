package prueba_backend.demo.services;

import java.util.List;
import prueba_backend.demo.dto.ProductoMayorStock;
import prueba_backend.demo.models.Franquicia;
import prueba_backend.demo.models.Producto;
import prueba_backend.demo.models.Sucursal;

public interface IFranquiciaService {

    Franquicia crearFranquicia(Franquicia franquicia);

    Sucursal agregarSucursal(Long franquiciaId, Sucursal sucursal);

    Producto agregarProducto(Long sucursalId, Producto producto);

    void eliminarProducto(Long productoId);

    Producto actualizarStock(Long productoId, int nuevoStock);

    List<ProductoMayorStock> obtenerProductoMayorStock(Long franquiciaId);

    Franquicia actualizarFranquicia(Long franquiciaId, Franquicia franquicia);

    Sucursal actualizarSucursal(Long sucursalId, Sucursal sucursal);

    Producto actualizarProducto(Long productoId, Producto producto);
}

package prueba_backend.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoMayorStock {

    private String sucursal;
    private String producto;
    private int stock;
    
}

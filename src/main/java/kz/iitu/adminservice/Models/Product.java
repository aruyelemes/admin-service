package kz.iitu.adminservice.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String price;
    private String count;

    public Product(String name, String price, String count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }
}

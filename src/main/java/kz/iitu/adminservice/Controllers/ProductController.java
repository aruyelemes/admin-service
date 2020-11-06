package kz.iitu.adminservice.Controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import kz.iitu.adminservice.Models.Product;
import kz.iitu.adminservice.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/list")
    public Product[] getAllProducts() {
        ResponseEntity<Product[]> response =
                restTemplate.getForEntity(
                        "http://product-service/api/v1/products/list",
                        Product[].class);
        Product[] products = response.getBody();

        return products;
    }


    @GetMapping("/{id}")
    @HystrixCommand(fallbackMethod = "getFallBackProduct",
            threadPoolKey = "productInfoPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20")
            }
    )
    public Product getProductById(@PathVariable Long id) {
        Product product = restTemplate.getForObject("http://product-service/api/v1/products/" + id, Product.class);

        return product;
    }

    public Product getFallBackProduct(@PathVariable Long id) {
        return new Product("No products", "", "");
    }


    @PostMapping("/createProduct")
    public String createProduct(@ModelAttribute @Valid @RequestBody Product product) {
        String pName = product.getName();
        String pPrice = product.getPrice();
        String pCount = product.getCount();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", pName);
        map.add("price", pPrice);
        map.add("count", pCount);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://product-service/api/v1/products/createProduct", map, String.class);

        return pName;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        restTemplate.delete("http://product-service/api/v1/products/delete/" + id);
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@ModelAttribute @Valid @RequestBody Product product, @PathVariable Long id) {
        Product pToUpdate = restTemplate.getForObject("http://product-service/api/v1/products/" + id, Product.class);

        pToUpdate.setName(product.getName());
        pToUpdate.setPrice(product.getPrice());
        pToUpdate.setCount(product.getCount());
        String pName = pToUpdate.getName();
        String pPrice = pToUpdate.getPrice();
        String pCount = pToUpdate.getCount();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", pName);
        map.add("price", pPrice);
        map.add("count", pCount);

        HttpEntity<Product> requestUpdate = new HttpEntity<>(pToUpdate, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://product-service/api/v1/products/update/" + id,
                map, String.class);

        return pName;
    }
}
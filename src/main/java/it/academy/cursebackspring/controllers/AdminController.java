package it.academy.cursebackspring.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.dto.response.UsersDTO;
import it.academy.cursebackspring.services.CategoryService;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.services.ProductService;
import it.academy.cursebackspring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/add/category")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> params) {
        categoryService.addCategory(params.get("categoryName"));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add/product")
    public ResponseEntity<?> addProduct(@RequestBody CreateProductDTO dto) {
        productService.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/order")
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateOrderStatusDTO dto) {
        orderService.changeOrderStatus(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/category")
    public ResponseEntity<?> deleteCategory(@RequestBody JsonNode params) {
        categoryService.deleteCategory(params.get("categoryId").asLong(), params.get("root").asBoolean());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/product")
    public ResponseEntity<?> deleteProduct(@RequestBody JsonNode params) {
        productService.deleteProduct(params.get("productId").asLong());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/orders")
    public ResponseEntity<?> getOrders(RequestDataDetailsDTO dto) {
        OrdersDTO out = orderService.getListOfOrders(dto);
        return ResponseEntity.ok(out);
    }

    @GetMapping("/get/users")
    public ResponseEntity<?> getUsers(RequestDataDetailsDTO dto) {
        UsersDTO usersDTO = userService.getUsersPage(dto);
        return ResponseEntity.ok(usersDTO);
    }

    @PutMapping("/update/category")
    public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryDTO dto) {
        categoryService.updateCategory(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/order")
    public ResponseEntity<?> deleteOrder(@RequestBody JsonNode params) {
        orderService.deleteOrder(params.get("orderId").asLong());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/order/item")
    public ResponseEntity<?> deleteOrder(@RequestBody DeleteOrderItemDTO dto) {
        orderService.deleteOrderItem(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/order/item")
    public ResponseEntity<?> updateOrderItem(@RequestBody OrderItemDTO dto) {
        orderService.addOrUpdateOrderItemToOrder(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/order/item")
    public ResponseEntity<?> addItemToOrder(@RequestBody OrderItemDTO dto) {
        orderService.addOrUpdateOrderItemToOrder(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/product")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductDTO dto) {
        productService.updateProduct(dto);
        return ResponseEntity.ok().build();
    }


}

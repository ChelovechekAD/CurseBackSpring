package it.academy.cursebackspring.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.dto.response.UsersDTO;
import it.academy.cursebackspring.services.CategoryService;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.services.ProductService;
import it.academy.cursebackspring.services.UserService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/add/category")
    public ResponseEntity<?> addCategory(@RequestBody JsonNode params) {
        String categoryName = params.get(Constants.CATEGORY_NAME_PARAM_KEY).toString();
        if (categoryName == null || categoryName.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.CATEGORY_NAME_VALIDATION_EXCEPTION);
        }
        categoryService.addCategory(categoryName.substring(1, categoryName.length() - 1));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add/product")
    public ResponseEntity<?> addProduct(@RequestBody @Valid CreateProductDTO dto) {
        productService.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/order")
    public ResponseEntity<?> updateOrderStatus(@RequestBody @Valid UpdateOrderStatusDTO dto) {
        orderService.changeOrderStatus(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/category/{categoryId}/{root}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "categoryId")
                                            @Valid
                                            @NotNull(message = Constants.CATEGORY_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                            @Min(value = Constants.MIN_CATEGORY_ID,
                                                    message = Constants.CATEGORY_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                            Long categoryId,
                                            @PathVariable(name = "root") boolean root) {
        categoryService.deleteCategory(categoryId, root);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable
                                           @Valid
                                           @NotNull(message = Constants.PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                           @Min(value = Constants.MIN_PRODUCT_ID,
                                                   message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
                                           Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/orders")
    public ResponseEntity<?> getOrders(@Valid RequestDataDetailsDTO dto) {
        OrdersDTO out = orderService.getListOfOrders(dto);
        return ResponseEntity.ok(out);
    }

    @GetMapping("/get/users")
    public ResponseEntity<?> getUsers(@Valid RequestDataDetailsDTO dto) {
        UsersDTO usersDTO = userService.getUsersPage(dto);
        return ResponseEntity.ok(usersDTO);
    }

    @PutMapping("/update/category")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid UpdateCategoryDTO dto) {
        categoryService.updateCategory(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable
                                         @Valid
                                         @NotNull(message = Constants.CATEGORY_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                         @Min(value = Constants.MIN_ORDER_ID,
                                                 message = Constants.ORDER_ID_VALIDATION_EXCEPTION)
                                         Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/order/item")
    public ResponseEntity<?> deleteOrderItem(
            @RequestParam(name = Constants.PRODUCT_ID_PARAM_KEY)
            @Valid
            @NotNull(message = Constants.PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
            @Min(value = Constants.MIN_PRODUCT_ID, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long productId,
            @RequestParam(name = Constants.ORDER_ID_PARAM_KEY)
            @Valid
            @NotNull(message = Constants.ORDER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
            @Min(value = Constants.MIN_ORDER_ID, message = Constants.ORDER_ID_VALIDATION_EXCEPTION) Long orderId) {
        orderService.deleteOrderItem(new DeleteOrderItemDTO(productId, orderId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/order/item")
    public ResponseEntity<?> updateOrderItem(@RequestBody @Valid OrderItemDTO dto) {
        orderService.addOrUpdateOrderItemToOrder(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/order/item")
    public ResponseEntity<?> addItemToOrder(@RequestBody @Valid OrderItemDTO dto) {
        orderService.addOrUpdateOrderItemToOrder(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/product")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductDTO dto) {
        productService.updateProduct(dto);
        return ResponseEntity.ok().build();
    }


}

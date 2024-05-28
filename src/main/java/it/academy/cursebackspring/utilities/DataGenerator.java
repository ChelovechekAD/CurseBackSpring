package it.academy.cursebackspring.utilities;

import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.enums.RoleEnum;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.Role;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.RoleRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.AuthService;
import it.academy.cursebackspring.services.CategoryService;
import it.academy.cursebackspring.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DataGenerator {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final RoleRepos roleRepos;
    private final UserRepos userRepos;
    private final AuthService authService;

    public void generateCategories() {
        categoryService.addCategory("Стулья");
        categoryService.addCategory("Столы");
        categoryService.addCategory("Шкафы");
        categoryService.addCategory("Диваны");
        categoryService.addCategory("Двери");
    }

    public void generateProducts() {
        IntStream.range(0, 120)
                .forEach(i -> {
                    CreateProductDTO product1 = CreateProductDTO.builder()
                            .price(19329.9 + i * 10)
                            .name("Стул серый " + i)
                            .categoryId(1L)
                            .description("Loreal parisssssh aga net no da disc est okey da nado bolshe")
                            .imageLink("chair-grey.jpg")
                            .build();
                    productService.addProduct(product1);
                });

        IntStream.range(0, 120)
                .forEach(i -> {
                    CreateProductDTO product1 = CreateProductDTO.builder()
                            .price(19329.9 + i * 10)
                            .name("Стол белый " + i)
                            .categoryId(2L)
                            .description("Loreal parisssssh aga net no da disc est okey da nado bolshe")
                            .imageLink("table-white.jpg")
                            .build();
                    productService.addProduct(product1);
                });
    }

    public void generateRoles() {
        Role role = new Role();
        role.setRole(RoleEnum.ROLE_ADMIN);
        roleRepos.save(role);

        Role role1 = new Role();
        role.setRole(RoleEnum.ROLE_DEFAULT_USER);
        roleRepos.save(role1);
    }

    public void generateNewUser() {
        RegUserDTO regUserDTO = RegUserDTO.builder()
                .name("Artem")
                .surname("Chelovek")
                .password("973341")
                .email("yushkevich2004@mail.ru")
                .passwordConfirm("973341")
                .phoneNumber("+321 21 12435342")
                .build();
        authService.regUser(regUserDTO);
        User user = userRepos.findById(1L)
                .orElseThrow(UserNotFoundException::new);
        Set<Role> roleSet = user.getRoleSet();
        roleSet.add(roleRepos.getByRole(RoleEnum.ROLE_ADMIN));
        userRepos.save(user);

        RegUserDTO defaultUser = RegUserDTO.builder()
                .name("Test")
                .surname("Default")
                .password("973341")
                .email("defaultusertest@mail.ru")
                .passwordConfirm("973341")
                .phoneNumber("+321 21 12435342")
                .build();
        authService.regUser(defaultUser);
    }

    public void generateAll() {
        generateCategories();
        generateProducts();
        generateRoles();
        generateNewUser();
    }
}

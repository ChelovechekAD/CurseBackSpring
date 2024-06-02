package it.academy.cursebackspring.repositories.specifications;

import it.academy.cursebackspring.dto.request.ParamFilterDTO;
import it.academy.cursebackspring.models.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private ParamFilterDTO paramFilterDTO;

    @Override
    public Predicate toPredicate(@NonNull Root<Product> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        if (Objects.equals(paramFilterDTO.getKey(), "category")) {
            return criteriaBuilder.equal(root.get(paramFilterDTO.getKey()).get("id"), paramFilterDTO.getValue());
        }
        if (paramFilterDTO.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get(paramFilterDTO.getKey()), paramFilterDTO.getValue());
        } else if (paramFilterDTO.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(paramFilterDTO.getKey()), paramFilterDTO.getValue());
        } else if (paramFilterDTO.getOperation().equalsIgnoreCase("=")) {
            return criteriaBuilder.equal(
                    root.get(paramFilterDTO.getKey()), paramFilterDTO.getValue());
        } else if (paramFilterDTO.getOperation().equalsIgnoreCase(":")) {
            if (root.get(paramFilterDTO.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.get(paramFilterDTO.getKey()), "%" + paramFilterDTO.getValue() + "%");
            } else {
                return criteriaBuilder.equal(root.get(paramFilterDTO.getKey()), paramFilterDTO.getValue());
            }
        }
        return null;
    }
}

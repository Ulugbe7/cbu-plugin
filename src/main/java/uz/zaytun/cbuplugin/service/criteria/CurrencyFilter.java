package uz.zaytun.cbuplugin.service.criteria;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.zaytun.cbuplugin.domain.data.Currency;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.LinkedList;
import java.util.List;

public class CurrencyFilter {

    private CurrencyFilter() {
    }

    public static Specification<Currency> filter(CurrencyDTO criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (criteria.getCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("code"), criteria.getCode()));
            }
            if (criteria.getCurrency() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), criteria.getCurrency()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}

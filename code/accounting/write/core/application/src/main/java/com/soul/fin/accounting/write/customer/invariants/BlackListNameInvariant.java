package com.soul.fin.accounting.write.customer.invariants;

import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.common.application.invariants.AggregateCandidate;
import com.soul.fin.common.application.invariants.InvariantValidator;
import com.soul.fin.common.application.invariants.ValidationResult;
import com.soul.fin.common.application.invariants.Violation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class BlackListNameInvariant
        implements InvariantValidator<Customer> {

    @Override
    public ValidationResult validate(
            AggregateCandidate<Customer> candidate
    ) {
        if (candidate.projectedState().getName().equals("GuilhermeB")) {
            return new ValidationResult.Invalid(
                    List.of(
                            new Violation(
                                    "BALANCE_NEGATIVE",
                                    "0001-erro",
                                    "Account balance cannot be negative"
                            ))
            );
        }

        return new ValidationResult.Valid();
    }
}


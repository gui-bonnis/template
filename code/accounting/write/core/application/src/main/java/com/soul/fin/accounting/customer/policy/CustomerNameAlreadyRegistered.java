package com.soul.fin.accounting.customer.policy;

import com.soul.fin.accounting.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.common.application.policy.*;

public class CustomerNameAlreadyRegistered implements Policy<RegisterCustomerCommand> {

//    private static final BigDecimal LIMIT =
//            new BigDecimal("10000");
//
//    private final WithdrawalReadModel readModel;
//
//    public CustomerNameAlreadyRegistered(
//            WithdrawalReadModel readModel
//    ) {
//        this.readModel = readModel;
//    }

    @Override
    public PolicyDecision evaluate(PolicyContext<RegisterCustomerCommand> context) {
        RegisterCustomerCommand cmd = context.input();

//        BigDecimal withdrawnToday =
//                readModel.totalWithdrawnToday(cmd.customerId());

        if (cmd.name().equals("GuilhermeR")) {
            return new PolicyDecision.Deny(
                    "CUSTOMER_REGISTERED",
                    "Customer already registered",
                    Severity.HARD_BLOCK
            );
        }

        return new PolicyDecision.Allow();
    }

    @Override
    public PolicyMetadata metadata() {
        return new PolicyMetadata(
                "CustomerNameAlreadyRegistered",
                "v1",
                Severity.HARD_BLOCK
        );
    }

}
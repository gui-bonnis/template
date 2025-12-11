package com.soul.fin.service.customer.exception;

import com.soul.fin.common.core.exception.ApplicationException;
import com.soul.fin.service.shared.EntityEnum;

public class CustomerExceptions {

    public static <T> T customerNotFound(String id) {
        var error = new CustomerDomainError.EntityNotFound(EntityEnum.CUSTOMER.name(), id);
        throw new ApplicationException(error);
    }

}

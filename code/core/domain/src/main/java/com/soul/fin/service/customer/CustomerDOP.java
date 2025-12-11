package com.soul.fin.service.customer;

import com.soul.fin.common.core.vo.Audit;
import com.soul.fin.common.core.vo.UpdatedBy;
import com.soul.fin.service.customer.vo.CustomerId;
import com.soul.fin.service.customer.vo.CustomerStatus;
import com.soul.fin.service.customer.vo.KycStatus;
import com.soul.fin.service.customer.vo.PersonalInfo;
import com.soul.fin.service.vo.Address;

import java.util.List;

public sealed interface CustomerDOP permits CustomerDOP.New, CustomerDOP.of, CustomerDOP.register {
    CustomerId id();

    PersonalInfo personalInfo();

    List<Address> addresses();

    CustomerStatus status();

    KycStatus kycStatus();

    Audit audit();

    record New(CustomerId id,
               PersonalInfo personalInfo,
               List<Address> addresses,
               CustomerStatus status,
               KycStatus kycStatus,
               Audit audit) implements CustomerDOP {
    }

    record of(CustomerDOP customerDOP,
              UpdatedBy updatedBy) implements CustomerDOP {
        @Override
        public CustomerId id() {
            return customerDOP.id();
        }

        @Override
        public PersonalInfo personalInfo() {
            return customerDOP.personalInfo();
        }

        @Override
        public List<Address> addresses() {
            return customerDOP.addresses();
        }

        @Override
        public CustomerStatus status() {
            return customerDOP.status();
        }

        @Override
        public KycStatus kycStatus() {
            return customerDOP.kycStatus();
        }

        @Override
        public Audit audit() {
            return new Audit.Update(customerDOP.audit(), updatedBy);
        }
    }

    record register(CustomerDOP customerDOP,
                    UpdatedBy updatedBy) implements CustomerDOP {
        @Override
        public CustomerId id() {
            return customerDOP.id();
        }

        @Override
        public PersonalInfo personalInfo() {
            return customerDOP.personalInfo();
        }

        @Override
        public List<Address> addresses() {
            return customerDOP.addresses();
        }

        @Override
        public CustomerStatus status() {
            return CustomerStatus.REGISTERED;
        }

        @Override
        public KycStatus kycStatus() {
            return customerDOP.kycStatus();
        }

        @Override
        public Audit audit() {
            return new Audit.Update(customerDOP.audit(), updatedBy);
        }
    }

}
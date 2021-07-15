package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired private CustomerAddressDao customerAddressDao;

    @Autowired private AddressDao addressDao;

    public AddressEntity getAddressByUUID(final String addressId, final CustomerEntity customerEntity)
            throws AuthorizationFailedException, AddressNotFoundException {
        AddressEntity addressEntity = addressDao.getAddressByUUID(addressId);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }
        if (addressId.isEmpty()) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }

        CustomerAddressEntity customerAddressEntity =
                customerAddressDao.customerAddressByAddress(addressEntity);
        if (!customerAddressEntity.getCustomer().getUuid().equals(customerEntity.getUuid())) {
            throw new AuthorizationFailedException(
                    "ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }
        return addressEntity;
    }
}

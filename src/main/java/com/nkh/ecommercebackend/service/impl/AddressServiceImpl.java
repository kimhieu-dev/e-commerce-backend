package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.dto.response.AddressRes;
import com.nkh.ecommercebackend.entity.Address;
import com.nkh.ecommercebackend.entity.User;
import com.nkh.ecommercebackend.mapper.AddressMapper;
import com.nkh.ecommercebackend.repository.AddressRepo;
import com.nkh.ecommercebackend.service.AddressService;
import com.nkh.ecommercebackend.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepo addressRepo;
    private final CurrentUserService currentUserService;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressRes> getAddresses() {
        User user = currentUserService.getUser();

        List<Address> addresses = addressRepo.findAllByUsername(user.getUsername());

        return addressMapper.toAddressResList(addresses);
    }
}

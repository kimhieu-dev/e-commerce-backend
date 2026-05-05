package com.nkh.ecommercebackend.mapper;

import com.nkh.ecommercebackend.dto.response.AddressRes;
import com.nkh.ecommercebackend.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    List<AddressRes> toAddressResList(List<Address> addresses);
    AddressRes toAddressRes(Address address);
}

package com.prokhorov.clinic.repository;

import com.prokhorov.clinic.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select ad from Address ad where ad.country like :country and ad.city like :city and ad.street like :street and ad.houseNum like :house and ad.flatNum like :flat")
    Optional<Address> findAddress(String country, String city, String street, String house, Short flat);

    @Query("select ad from Address ad where ad.country like :country and ad.city like :city and ad.street like :street and ad.houseNum like :house")
    Optional<Address> findAddress(String country, String city, String street, String house);
}

package kz.narxoz.flowershop.repository;

import kz.narxoz.flowershop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
}

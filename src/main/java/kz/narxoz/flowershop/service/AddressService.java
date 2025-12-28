package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.address.AddressRequest;
import kz.narxoz.flowershop.dto.address.AddressResponse;
import kz.narxoz.flowershop.entity.Address;
import kz.narxoz.flowershop.entity.User;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.mapper.AddressMapper;
import kz.narxoz.flowershop.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository repository;
    private final AddressMapper mapper;

    public AddressService(AddressRepository repository, AddressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AddressResponse> listForUser(Long userId) {
        return repository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public AddressResponse create(User user, AddressRequest req) {
        Address a = mapper.toEntity(req);
        a.setUser(user);

        if (req.isDefault()) {
            unsetDefault(user.getId());
            a.setDefault(true);
        }

        return mapper.toResponse(repository.save(a));
    }

    @Transactional
    public AddressResponse update(User user, Long id, AddressRequest req) {
        Address a = repository.findById(id).orElseThrow(() -> new NotFoundException("Address not found"));
        if (!a.getUser().getId().equals(user.getId())) throw new NotFoundException("Address not found");
        mapper.update(req, a);

        if (req.isDefault()) {
            unsetDefault(user.getId());
            a.setDefault(true);
        }
        return mapper.toResponse(a);
    }

    @Transactional
    public void delete(User user, Long id) {
        Address a = repository.findById(id).orElseThrow(() -> new NotFoundException("Address not found"));
        if (!a.getUser().getId().equals(user.getId())) throw new NotFoundException("Address not found");
        repository.delete(a);
    }

    private void unsetDefault(Long userId) {
        repository.findByUserId(userId).forEach(addr -> {
            if (addr.isDefault()) addr.setDefault(false);
        });
    }
}

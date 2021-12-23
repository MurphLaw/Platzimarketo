package com.example.ejemplospring.persistence.crud;

import com.example.ejemplospring.persistence.entity.Compra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CompraCrudRespository extends CrudRepository<Compra,Integer> {
    Optional<List<Compra>> findByIdCliente(String idCliente);

}

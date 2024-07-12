package com.lostandfound.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lostandfound.Entity.Item;


@Repository
public interface ItemsRepo extends JpaRepository<Item,Long>{

     List<Item> findByUserId(Long id);

}

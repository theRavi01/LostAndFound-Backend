package com.lostandfound.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lostandfound.Entity.Item;
import com.lostandfound.Repo.ItemsRepo;
import com.lostandfound.helper.ResponseEntityObject;

@Service
public class ItemsService {

	@Autowired
	private ItemsRepo itemsRepo;
	
	  public ResponseEntityObject<Item> createItem(Item items) {
		  itemsRepo.save(items);
		  return new ResponseEntityObject<Item>(true, "Succesfully Registered",items);
	  }
	  
}

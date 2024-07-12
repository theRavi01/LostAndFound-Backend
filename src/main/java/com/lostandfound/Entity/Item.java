package com.lostandfound.Entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table 
public class Item {
	@Id
	@jakarta.persistence.Column(name = "id")
	@jakarta.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(strategy = "native", name = "native")
	private long id;
	
	@Column (name = "item_Name")
    private String itemName;
	
	@Column (name = "place")
    private String place;
	
	@Column (name = "date")
    private Date date;
    
    @Column(name = "item_picture_url")
	private String itemPictureUrl;
    
    @Column (name = "type")
    private String type;
    
    @Column (name = "reward_price")
    private String rewardPrice;
    
    @Column(name = "description", columnDefinition = "varchar(1500)")
	private String description;
    
    @Column(name = "category")
	private String category;
    
    @Column(name = "status")
    private int status;
    

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    
}

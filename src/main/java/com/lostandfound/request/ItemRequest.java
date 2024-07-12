package com.lostandfound.request;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ItemRequest {
    private String itemName;
    private String place;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private MultipartFile image;
    private String type;
    private String rewardPrice;
    private String description;
    private String category;
}

package com.macnicagwi.core.components.dtos;

import java.util.List;

import com.day.cq.dam.api.Asset;

import lombok.Data;

@Data
public class ProductLineContactCfmDto {
    
    private String name;
    private String primaryContactNumber;
    private String secondaryContactNumber;
    private String emailId;
    private List<Asset> productLineLogos;
    
}

package com.api.server.dto.order;

import com.api.server.dto.structure.StructureDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    
    private Long id;
    
    private Integer quantity;
    
    private Double unitprice;
    
    private String cancellationreason;
    
    private Long orderId;
    
    private StructureDTO structure;
}

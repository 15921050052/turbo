package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public abstract class Trade {
	
	private String id;
	
	private BigDecimal price;
	
	private BigDecimal amount;
	
	private long timestamp;
	
	public boolean isDone() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}

}
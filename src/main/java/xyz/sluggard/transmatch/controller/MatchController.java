package xyz.sluggard.transmatch.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.vo.HttpResult;

@Slf4j
@RestController
public class MatchController {
	
	private static final long WAIT_TIME = 5000;
	
//	private MatchEngine matchEngine = MatchEngine.ENGIN;
	@Autowired
	private Engine engine;
	
	@RequestMapping(value="/createOrder/{pair}",method=RequestMethod.POST)
	@ApiOperation("新建订单")
	public HttpResult<?> newOrder(@PathVariable String pair,@RequestBody Order order) {
		checkPair(pair);
		engine.newOrder(order);
		return HttpResult.SUCCESS();
	}
	
	@RequestMapping(value="/cancleOrder/{pair}/{side}/{orderId}",method=RequestMethod.POST)
	@ApiOperation("撤销订单")
	public HttpResult<?> canelOrder(@PathVariable String pair,@PathVariable String orderId, @PathVariable Side side) {
		checkPair(pair);
		if(engine.cancelOrder(orderId,side)) {
			return HttpResult.SUCCESS();
		}else {
			return HttpResult.FAILED();
		}
	}
	
	@RequestMapping(value="/getDepth/{pair}",method=RequestMethod.GET)
	@ApiOperation("获取摆单列表")
	public HttpResult<OrderBook> getOrderBook() {
		return HttpResult.SUCCESS(new OrderBook(engine.getAskQueue(), engine.getBidQueue()));
	}
	
	private void checkPair(String pair) {
		if(!engine.getCurrencyPair().equals(pair)) {
			throw new IllegalArgumentException("error pair : " + pair);
		}
	}
	
	
	@Data
	@AllArgsConstructor
	private class OrderBook {
		
		private Collection<Order> ask;
		
		private Collection<Order> bid;
		
		
	}

}

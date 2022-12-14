package labshopeventsourcing.query;


import labshopeventsourcing.event.*;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.io.IOException;

@Service
@ProcessingGroup("orderStatus")
public class OrderStatusViewHandler {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @QueryHandler
    public List<OrderStatus> handle(OrderStatusQuery query) {
        return orderStatusRepository.findAll();
    }

    @EventHandler
    public void whenOrderPlaced_then_CREATE_1 (OrderPlacedEvent orderPlaced) throws Exception{
            // view 객체 생성
            OrderStatus orderStatus = new OrderStatus();
            // view 객체에 이벤트의 Value 를 set 함
            orderStatus.setId(orderPlaced.getId());
            orderStatus.setStatus(orderPlaced.getStatus());
            orderStatus.setAmount(Long.valueOf(orderPlaced.getAmount()));
            orderStatus.setQty(orderPlaced.getQty());
            // view 레파지 토리에 save
            orderStatusRepository.save(orderStatus);
    }


    @EventHandler
    public void whenOrderDeliveryStarted_then_UPDATE_1( OrderDeliveryStartedEvent orderDeliveryStarted) throws Exception{
        // view 객체 조회
        Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findById(orderDeliveryStarted.getId());

        if( orderStatusOptional.isPresent()) {
                OrderStatus orderStatus = orderStatusOptional.get();
        // view 객체에 이벤트의 eventDirectValue 를 set 함
            orderStatus.setStatus("DeliveryStarted");    
            // view 레파지 토리에 save
                orderStatusRepository.save(orderStatus);
            }

    }
    @EventHandler
    public void whenOrderCancelled_then_UPDATE_2( OrderCancelledEvent orderCancelled) throws Exception{
        // view 객체 조회
        Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findById(orderCancelled.getId());

        if( orderStatusOptional.isPresent()) {
                OrderStatus orderStatus = orderStatusOptional.get();
        // view 객체에 이벤트의 eventDirectValue 를 set 함
            orderStatus.setStatus("Cancelled");    
            // view 레파지 토리에 save
                orderStatusRepository.save(orderStatus);
            }

    }


}


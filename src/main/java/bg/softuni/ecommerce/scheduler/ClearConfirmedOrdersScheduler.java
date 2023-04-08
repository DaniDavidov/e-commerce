package bg.softuni.ecommerce.scheduler;

import bg.softuni.ecommerce.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearConfirmedOrdersScheduler {

    private final OrderService orderService;

    public ClearConfirmedOrdersScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 1 * * MON")
    public void clearConfirmedOrders() {
        this.orderService.clearAllConfirmedOrders();
    }
}

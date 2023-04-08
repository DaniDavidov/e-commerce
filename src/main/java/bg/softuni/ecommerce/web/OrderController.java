package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.OrderDto;
import bg.softuni.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getProcessedOrders(Model model) {
        List<OrderDto> allProcessedOrders = this.orderService.getAllProcessedOrders();
        model.addAttribute("orders", allProcessedOrders);
        model.addAttribute("type", "unprocessed");
        model.addAttribute("link", "/orders/unprocessed");
        return "orders";
    }

    @GetMapping("/unprocessed")
    public String getUnprocessedOrders(Model model) {
        List<OrderDto> allUnprocessedOrders = this.orderService.getAllUnprocessedOrders();
        model.addAttribute("orders", allUnprocessedOrders);
        model.addAttribute("type", "processed");
        model.addAttribute("link", "/orders");
        return "orders";
    }

    @GetMapping("/{id}/details")
    public String getOrderDetails(@PathVariable("id") Long orderId, Model model) {
        model.addAttribute("order", orderService.getOrderById(orderId));
        model.addAttribute("items", orderService.getOrderItems(orderId));
        return "order-details";
    }

    @PostMapping("/{id}/confirm")
    public String confirmOrder(@PathVariable("id") Long orderId) {
        this.orderService.confirmOrderById(orderId);
        return "redirect:/orders/unprocessed";
    }


}

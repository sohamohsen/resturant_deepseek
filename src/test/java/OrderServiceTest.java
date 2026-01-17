import com.research.exception.ValidationException;
import com.research.model.Customer;
import com.research.model.MenuItem;
import com.research.model.Order;
import com.research.model.OrderStatus;
import com.research.repository.CustomerRepository;
import com.research.repository.MenuItemRepository;
import com.research.repository.OrderRepository;
import com.research.service.CustomerService;
import com.research.service.OrderService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Epic("Order")
@Feature("Order Service")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    MenuItemRepository menuItemRepository;
    @Mock
    CustomerService customerService;

    @InjectMocks
    OrderService orderService;

    Customer customer;
    MenuItem item;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1);

        item = new MenuItem();
        item.setId(10);
        item.setName("Burger");
        item.setPrice(100);
        item.setAvailable(true);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        when(customerRepository.existsById(1)).thenReturn(true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(menuItemRepository.findById(10)).thenReturn(Optional.of(item));

        Order order = orderService.createOrder(1, List.of(10));

        assertEquals(OrderStatus.PENDING, order.getStatus());
        verify(orderRepository).add(any(Order.class));
        verify(customerService).addLoyaltyPoints(eq(1), anyDouble());
    }

    @Test
    void shouldFail_WhenOrderingUnavailableItem() {
        item.setAvailable(false);

        when(customerRepository.existsById(1)).thenReturn(true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(menuItemRepository.findById(10)).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                () -> orderService.createOrder(1, List.of(10)));
    }
}

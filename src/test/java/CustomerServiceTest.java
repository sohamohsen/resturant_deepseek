import com.research.exception.NotFoundException;
import com.research.model.Customer;
import com.research.repository.CustomerRepository;
import com.research.service.CustomerService;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Epic("Customer Management")
@Feature("Customer Service")
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1);
        customer.setFullName("Soha Mohsen");
        customer.setEmail("soha@test.com");
        customer.setPhone("+201234567890");
    }

    @Test
    @Story("Add Customer")
    @Severity(SeverityLevel.CRITICAL)
    void shouldAddCustomerSuccessfully() {
        customerService.addCustomer(customer);

        verify(customerRepository).add(customer);
    }

    @Test
    @Story("Update Customer")
    @Severity(SeverityLevel.NORMAL)
    void shouldThrowExceptionWhenUpdatingNonExistingCustomer() {
        when(customerRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> customerService.updateCustomer(customer));
    }

    @Test
    @Story("Add Loyalty Points")
    @Severity(SeverityLevel.MINOR)
    void shouldAddLoyaltyPointsCorrectly() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        customerService.addLoyaltyPoints(1, 250); // expected 25 points

        assertEquals(25, customer.getLoyaltyPoints());
        verify(customerRepository).update(customer);
    }

    @Test
    @Story("Invalid Test Example")
    void shouldFail_WhenExpectingWrongPoints() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        customerService.addLoyaltyPoints(1, 100);

        // Wrong expectation ON PURPOSE
        assertEquals(50, customer.getLoyaltyPoints());
    }
}

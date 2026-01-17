import com.research.model.Customer;
import com.research.model.Reservation;
import com.research.model.Table;
import com.research.model.TableStatus;
import com.research.repository.CustomerRepository;
import com.research.repository.ReservationRepository;
import com.research.repository.TableRepository;
import com.research.service.ReservationService;
import com.research.service.TableService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Epic("Reservation")
@Feature("Reservation Service")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    TableRepository tableRepository;
    @Mock
    TableService tableService;

    @InjectMocks
    ReservationService reservationService;

    Customer customer;
    Table table;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1);

        table = new Table();
        table.setId(1);
        table.setTableNumber(10);
        table.setCapacity(4);
        table.setStatus(TableStatus.AVAILABLE);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        when(customerRepository.existsById(1)).thenReturn(true);
        when(tableRepository.existsById(1)).thenReturn(true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(tableRepository.findById(1)).thenReturn(Optional.of(table));
        when(reservationRepository.hasOverlappingReservation(any(), any(), any()))
                .thenReturn(false);

        Reservation reservation = reservationService.createReservation(
                1, 1, LocalDate.now().plusDays(1), LocalTime.NOON, 2);

        assertNotNull(reservation);
        verify(tableService).updateTableStatus(1, TableStatus.RESERVED);
    }

    @Test
    void shouldFail_WhenDateIsInPast_ButTestExpectsSuccess() {
        when(customerRepository.existsById(1)).thenReturn(true);
        when(tableRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() ->
                reservationService.createReservation(
                        1, 1, LocalDate.now().minusDays(1), LocalTime.NOON, 2));
    }
}

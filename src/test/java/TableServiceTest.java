import com.research.exception.ValidationException;
import com.research.model.Table;
import com.research.model.TableStatus;
import com.research.repository.TableRepository;
import com.research.service.TableService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
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
@Epic("Table Management")
@Feature("Table Service")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    TableRepository tableRepository;

    @InjectMocks
    TableService tableService;

    Table table;

    @BeforeEach
    void setup() {
        table = new Table();
        table.setId(1);
        table.setTableNumber(5);
        table.setCapacity(4);
        table.setStatus(TableStatus.AVAILABLE);
    }

    @Test
    void shouldAddTableSuccessfully() {
        when(tableRepository.findByTableNumber(5)).thenReturn(Optional.empty());

        tableService.addTable(table);

        verify(tableRepository).add(table);
    }

    @Test
    void shouldThrowExceptionWhenCapacityTooLarge() {
        table.setCapacity(30);

        assertThrows(ValidationException.class,
                () -> tableService.addTable(table));
    }

    @Test
    void shouldFail_WhenExpectingTableAvailableButItsReserved() {
        table.setStatus(TableStatus.RESERVED);
        when(tableRepository.findById(1)).thenReturn(Optional.of(table));

        assertTrue(tableService.isTableAvailable(1));
    }
}

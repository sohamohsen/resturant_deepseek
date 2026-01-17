import com.research.exception.NotFoundException;
import com.research.model.Staff;
import com.research.repository.StaffRepository;
import com.research.service.StaffService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Epic("Staff Management")
@Feature("Staff Service")
@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    StaffRepository staffRepository;

    @InjectMocks
    StaffService staffService;

    Staff staff;

    @BeforeEach
    void setup() {
        staff = new Staff();
        staff.setId(1);
        staff.setFullName("Ahmed Ali");
        staff.setRole("Waiter");
        staff.setEmail("ahmed@test.com");
        staff.setPhone("+201111111111");
    }

    @Test
    void shouldAddStaffSuccessfully() {
        staffService.addStaff(staff);
        verify(staffRepository).add(staff);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingStaff() {
        when(staffRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> staffService.updateStaff(staff));
    }

    @Test
    void shouldFail_WhenEmailIsInvalid_ButTestExpectsSuccess() {
        staff.setEmail("wrong-email");

        assertDoesNotThrow(() -> staffService.addStaff(staff));
    }
}

import com.research.exception.NotFoundException;
import com.research.model.MenuCategory;
import com.research.model.MenuItem;
import com.research.repository.MenuCategoryRepository;
import com.research.repository.MenuItemRepository;
import com.research.service.MenuItemService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Epic("Menu")
@Feature("Menu Item Service")
@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    MenuItemRepository menuItemRepository;

    @Mock
    MenuCategoryRepository categoryRepository;

    @InjectMocks
    MenuItemService menuItemService;

    MenuItem menuItem;
    MenuCategory category;

    @BeforeEach
    void setUp() {
        category = new MenuCategory(1, "Drinks", "All drinks", true);

        menuItem = new MenuItem();
        menuItem.setId(1);
        menuItem.setName("Cola");
        menuItem.setDescription("Cold drink");
        menuItem.setPrice(25);
        menuItem.setCategory(category);
        menuItem.setAvailable(true);
    }

    @Test
    @Story("Add Menu Item")
    void shouldAddMenuItemSuccessfully() {
        when(categoryRepository.existsById(1)).thenReturn(true);

        menuItemService.addMenuItem(menuItem);

        verify(menuItemRepository).add(menuItem);
    }

    @Test
    @Story("Category Validation")
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> menuItemService.addMenuItem(menuItem));
    }

    @Test
    void shouldFail_WhenPriceIsNegative_ButTestExpectsSuccess() {
        menuItem.setPrice(-10);

        assertDoesNotThrow(() -> menuItemService.addMenuItem(menuItem));
    }
}

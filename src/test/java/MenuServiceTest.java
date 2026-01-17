import com.research.model.MenuCategory;
import com.research.model.MenuItem;
import com.research.repository.MenuCategoryRepository;
import com.research.repository.MenuItemRepository;
import com.research.service.MenuService;
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
@Epic("Menu")
@Feature("Menu Business Rules")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuCategoryRepository categoryRepository;
    @Mock
    MenuItemRepository itemRepository;

    @InjectMocks
    MenuService menuService;

    MenuItem item;
    MenuCategory category;

    @BeforeEach
    void setup() {
        category = new MenuCategory(1, "Food", "Main food", true);

        item = new MenuItem();
        item.setName("Pizza");
        item.setPrice(120);
        item.setCategory(category);
    }

    @Test
    void shouldAddMenuItemSuccessfully() {
        menuService.addMenuItem(item);
        verify(itemRepository).add(item);
    }

    @Test
    void shouldFail_WhenCategoryIsUnavailable_ButTestExpectsSuccess() {
        category.setAvailable(false);

        assertDoesNotThrow(() -> menuService.addMenuItem(item));
    }
}

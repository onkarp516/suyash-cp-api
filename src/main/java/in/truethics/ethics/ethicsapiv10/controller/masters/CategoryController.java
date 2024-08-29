package in.truethics.ethics.ethicsapiv10.controller.masters;

import com.google.gson.JsonObject;
import in.truethics.ethics.ethicsapiv10.service.master_service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/create_category")
    public ResponseEntity<?> createCategory(HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    /* Get all categories of Subgroups */
    @PostMapping(path = "/get_all_categories")
    public Object getAllCategory(HttpServletRequest request) {
        JsonObject result = categoryService.getAllCategory(request);
        return result.toString();
    }

    /* Get  all Categories of Outlets */
    @GetMapping(path = "/get_outlet_categories")
    public Object getAllOutletCategories(HttpServletRequest request) {
        JsonObject result = categoryService.getAllOutletCategories(request);
        return result.toString();
    }

    @PostMapping(path = "/get_category")
    public Object getCategory(HttpServletRequest request) {
        JsonObject result = categoryService.getCategory(request);
        return result.toString();
    }

    @PostMapping(path = "/update_category")
    public Object updateCatgeory(HttpServletRequest request) {
        JsonObject result = categoryService.updateCategory(request);
        return result.toString();
    }

}

package in.truethics.ethics.ethicsapiv10.controller.masters;

import com.google.gson.JsonObject;
import in.truethics.ethics.ethicsapiv10.service.master_service.BalancingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BalancingMethodController {
    @Autowired
    BalancingMethodService service;

    @PostMapping(path = "/create_balancing_method")
    public ResponseEntity<?> createBalancingMethod(HttpServletRequest request) {
        return ResponseEntity.ok(service.createBalancingMethod(request));
    }

    @GetMapping(path = "/get_balancing_methods")
    public Object getBalancingMethod(HttpServletRequest request) {
        JsonObject result = service.getBalancingMethod(request);
        return result.toString();
    }
}

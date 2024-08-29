package in.truethics.ethics.ethicsapiv10.service.access_permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemMasterModules;
import in.truethics.ethics.ethicsapiv10.model.user.Users;
import in.truethics.ethics.ethicsapiv10.repository.access_permission_repository.SystemMasterModuleRepository;
import in.truethics.ethics.ethicsapiv10.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SystemMasterModulesService {
    @Autowired
    private SystemMasterModuleRepository moduleRepository;
    @Autowired
    private JwtTokenUtil jwtRequestFilter;

    public JsonObject createSystemModules(HttpServletRequest request) {
        Users users = jwtRequestFilter.getUserDataFromToken(
                request.getHeader("Authorization").substring(7));

        JsonObject responseObject = new JsonObject();
        SystemMasterModules modules = new SystemMasterModules();
        modules.setName(request.getParameter("name"));
        modules.setSlug(request.getParameter("slug"));
        modules.setStatus(true);
        modules.setCreatedBy(users.getId());
        if (!request.getParameter("parent_id").equalsIgnoreCase(""))
            modules.setParentModuleId(Long.parseLong(request.getParameter("parent_id")));
        try {
            moduleRepository.save(modules);
            responseObject.addProperty("message", "success");
            responseObject.addProperty("responseStatus", HttpStatus.OK.value());
        } catch (Exception e) {
            responseObject.addProperty("message", "error");
            responseObject.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return responseObject;

    }

    /* get all Modules */
    public JsonObject getSystemModules() {
        List<SystemMasterModules> modules = moduleRepository.findByStatus(true);
        JsonArray modulesList = new JsonArray();
        JsonObject moduleObject = new JsonObject();
        for (SystemMasterModules mModules : modules) {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("id", mModules.getId());

            mObject.addProperty("name", mModules.getName());
            mObject.addProperty("slug", mModules.getSlug());
            SystemMasterModules parentModule = moduleRepository.findByIdAndStatus(mModules.getParentModuleId(), true);
            if (parentModule != null) {
                mObject.addProperty("parent_id", parentModule.getId());
                mObject.addProperty("parent_name", parentModule.getName());
            }
            modulesList.add(mObject);
        }
        moduleObject.addProperty("message", "success");
        moduleObject.addProperty("responseStatus", HttpStatus.OK.value());
        moduleObject.add("list", modulesList);
        return moduleObject;
    }

    public JsonObject getParents() {

        List<SystemMasterModules> modules = moduleRepository.findByStatus(true);
        JsonArray modulesList = new JsonArray();
        JsonObject moduleObject = new JsonObject();
        for (SystemMasterModules mModules : modules) {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("parent_id", mModules.getId());
            mObject.addProperty("parent_name", mModules.getName());
            modulesList.add(mObject);
        }
        moduleObject.addProperty("message", "success");
        moduleObject.addProperty("responseStatus", HttpStatus.OK.value());
        moduleObject.add("list", modulesList);
        return moduleObject;
    }
}

package in.truethics.ethics.ethicsapiv10.service.access_permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.truethics.ethics.ethicsapiv10.common.CommonAccessPermissions;
import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemActionMapping;
import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemMasterActions;
import in.truethics.ethics.ethicsapiv10.model.access_permissions.SystemMasterModules;
import in.truethics.ethics.ethicsapiv10.model.user.Users;
import in.truethics.ethics.ethicsapiv10.repository.access_permission_repository.SystemActionMappingRepository;
import in.truethics.ethics.ethicsapiv10.repository.access_permission_repository.SystemMasterActionsRepository;
import in.truethics.ethics.ethicsapiv10.repository.access_permission_repository.SystemMasterModuleRepository;
import in.truethics.ethics.ethicsapiv10.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SystemActionMappingService {
    public List<SystemMasterModules> modulesParentIds = new ArrayList<>();
    public Map<String, List<SystemMasterModules>> hm = new HashMap<>();
    @Autowired
    private SystemActionMappingRepository repository;
    @Autowired
    private SystemMasterModuleRepository moduleRepository;
    @Autowired
    private SystemMasterActionsRepository actionsRepository;
    @Autowired
    private JwtTokenUtil jwtRequestFilter;
    @Autowired
    private CommonAccessPermissions accessPermissions;

    public JsonObject createActionsMappings(HttpServletRequest request) {
        Users users = jwtRequestFilter.getUserDataFromToken(
                request.getHeader("Authorization").substring(7));
        JsonObject actionObject = new JsonObject();
        SystemActionMapping actionMapping = new SystemActionMapping();
        actionMapping.setName(request.getParameter("name"));
        actionMapping.setSlug(request.getParameter("slug"));
        SystemMasterModules modules = moduleRepository.findByIdAndStatus(
                Long.parseLong(request.getParameter("module_id")), true);
        if (modules != null)
            actionMapping.setSystemMasterModules(modules);
        actionMapping.setStatus(true);
        actionMapping.setCreatedBy(users.getId());
        String jsonStr = request.getParameter("actions");
        actionMapping.setActionsId(jsonStr);
        try {
            repository.save(actionMapping);
            actionObject.addProperty("message", "success");
            actionObject.addProperty("responseStatus", HttpStatus.OK.value());
        } catch (Exception e) {
            actionObject.addProperty("message", "error");
            actionObject.addProperty("responseStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return actionObject;
    }

    /* Get all Actions Mapping like create, edit, delete, list */
    public JsonObject getMasterActions() {
        List<SystemMasterActions> mappings = actionsRepository.findByStatus(true);
        JsonArray actionsList = new JsonArray();
        JsonObject actionsObject = new JsonObject();
        for (SystemMasterActions mMappings : mappings) {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("id", mMappings.getId());
            mObject.addProperty("name", mMappings.getName());
            mObject.addProperty("slug", mMappings.getSlug());
            actionsList.add(mObject);
        }
        actionsObject.addProperty("message", "success");
        actionsObject.addProperty("responseStatus", HttpStatus.OK.value());
        actionsObject.add("list", actionsList);
        return actionsObject;
    }

    public JsonObject getActionsMappingsPermissions() {
        JsonObject finalResult = new JsonObject();
        TreeSet<Long> mappingTree = new TreeSet<>();
        JsonArray userPermissions = new JsonArray();
        JsonArray permissions = new JsonArray();
        JsonArray masterModules = new JsonArray();
        List<SystemActionMapping> list = repository.findByStatus(true);
        /*
         * Print elements using the forEach
         */
        for (SystemActionMapping mapping : list) {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("id", mapping.getId());
            mObject.addProperty("name", mapping.getName());
            mObject.addProperty("slug", mapping.getSlug());
            mObject.addProperty("parent_id", mapping.getSystemMasterModules().getId());
            String[] actions = mapping.getActionsId().split(",");
            permissions = accessPermissions.getActions(actions);
            masterModules = accessPermissions.getParentMasters(mapping.getSystemMasterModules().getParentModuleId());
            mObject.add("actions", permissions);
            mObject.add("parent_modules", masterModules);
            userPermissions.add(mObject);
        }
        finalResult.addProperty("message", "success");
        finalResult.addProperty("responseStatus", HttpStatus.OK.value());
        finalResult.add("userActions", userPermissions);
        return finalResult;
    }

    /* Get all ACtions Mappings  for list of mappings */
    public JsonObject getActionsMappings() {
        List<SystemActionMapping> mappings = repository.findByStatus(true);
        JsonArray actionsList = new JsonArray();
        JsonObject actionsObject = new JsonObject();
        for (SystemActionMapping mMappings : mappings) {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("id", mMappings.getId());
            mObject.addProperty("name", mMappings.getName());
            mObject.addProperty("slug", mMappings.getSlug());
            SystemMasterModules modules = moduleRepository.findByIdAndStatus(mMappings.getSystemMasterModules().getId(), true);
            mObject.addProperty("module_id", modules.getId());
            mObject.addProperty("module_name", modules.getName());
            String actionsId = mMappings.getActionsId();
            String[] actions = actionsId.split(",");
            String actionsName = "";
            for (int i = 0; i < actions.length; i++) {
                SystemMasterActions masterActions = actionsRepository.findByIdAndStatus(Long.parseLong(actions[i]), true);
                actionsName = actionsName + masterActions.getName();
                if (i < actions.length - 1)
                    actionsName = actionsName + ",";
            }
            mObject.addProperty("actions", actionsName);
            actionsList.add(mObject);
        }
        actionsObject.addProperty("message", "success");
        actionsObject.addProperty("responseStatus", HttpStatus.OK.value());
        actionsObject.add("list", actionsList);
        return actionsObject;
    }

    public JsonObject createAccessActions(HttpServletRequest request) {
        Users users = jwtRequestFilter.getUserDataFromToken(
                request.getHeader("Authorization").substring(7));
        JsonObject actionObject = new JsonObject();
        SystemMasterActions accessAction = new SystemMasterActions();
        accessAction.setName(request.getParameter("name"));
        accessAction.setSlug(request.getParameter("slug"));
        accessAction.setStatus(true);
        accessAction.setCreatedBy(users.getCreatedBy());
        try {
            actionsRepository.save(accessAction);
            actionObject.addProperty("message", "success");
            actionObject.addProperty("responseStatus", HttpStatus.OK.value());

        } catch (Exception e) {

        }
        return actionObject;
    }

    /*public JsonArray getActions(String[] actions) {
        JsonArray mArray = new JsonArray();
        JsonArray mMasters = new JsonArray();
        //  JsonObject mObject = new JsonObject();
        for (String actionId : actions) {
            JsonObject mAction = new JsonObject();
            SystemMasterActions masterActions = actionsRepository.findByIdAndStatus(Long.parseLong(actionId), true);
            mAction.addProperty("id", masterActions.getId());
            mAction.addProperty("name", masterActions.getName());
            mAction.addProperty("slug", masterActions.getSlug());
            mArray.add(mAction);
        }
        return mArray;
    }*/

    /*private JsonArray getParentMasters(Long masterModuleId) {
        JsonArray mActions = new JsonArray();
        modulesParentIds.clear();
        getModules(masterModuleId);
        for (SystemMasterModules systemMasterModules : modulesParentIds) {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("id", systemMasterModules.getId());
            mObject.addProperty("name", systemMasterModules.getName());
            mObject.addProperty("slug", systemMasterModules.getSlug());
            mActions.add(mObject);
        }
        return mActions;
    }

    private void getModules(Long mapElement) {
        modulesParentIds.add(recursiveCall(mapElement));
    }

    private SystemMasterModules recursiveCall(Long mapElement) {
        SystemMasterModules modules = moduleRepository.findByIdAndStatus(mapElement, true);
        if (modules.getParentModuleId() == null) {
            return modules;
        } else {
            modulesParentIds.add(modules);
            return recursiveCall(modules.getParentModuleId());
        }
    }*/
}

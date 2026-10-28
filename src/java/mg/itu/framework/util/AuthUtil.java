package mg.itu.framework.util;

import java.util.ArrayList;
import java.util.List;

public class AuthUtil {

    public static List<String> getRolesFromAuthAnnotation(String roleNames) {
        List<String> roles = new ArrayList<>();
        if (roleNames != null) {
            String[] roleArray = roleNames.split(",");
            for (String role : roleArray) {
                roles.add(role.trim());
            }
        }
        return roles;
    }

    public static boolean verifyRoleAccess(Object roleAuth , String roleNames) {
        if (roleNames == null || roleNames.trim().isEmpty() || roleNames.equals("")) {
            return true;
        }
        
        List<String> roles = getRolesFromAuthAnnotation(roleNames);
        if (roleAuth == null) {
            return false;
        }

        return roles.contains(roleAuth.toString());
    }

}

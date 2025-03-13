package org.epde.ingrecipe.common.security.authorization;

import java.util.List;
import java.util.Map;

public interface RoleEndpointProvider {
    List<String> getPublicEndpoints();
    List<String> getCommonSecureEndpoints();
    Map<String, List<String>> getRoleEndpoints();
}

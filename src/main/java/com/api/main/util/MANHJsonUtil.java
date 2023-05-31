package com.api.main.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("manhJsonUtil")
public class MANHJsonUtil {

	public StringBuffer convertToVelocity(Map<String, Object> jsonElements, 
			StringBuffer vmTeamplate, String keyVMTeamplate, String key) {
		
		for (Map.Entry<String, Object> entry : jsonElements.entrySet()) {
		
			String newKeyVMTemplate = keyVMTeamplate.concat(".").concat(entry.getKey());

			if (entry.getValue() instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) entry.getValue();
                if(map != null && !map.isEmpty())
                	convertToVelocity(map, vmTeamplate, newKeyVMTemplate, entry.getKey());
                else
                	vmTeamplate.append("#set($document").append(newKeyVMTemplate).append(" = ").append(map).append(")\n");
                
            } else if (entry.getValue() instanceof List) {
                List<?> list = (List<?>) entry.getValue();
                
                if(list.isEmpty())
                	vmTeamplate.append("#set($document").append(newKeyVMTemplate).append(" = ").append(list).append(")\n");
                for(Object listEntry: list) {
                    if (listEntry instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) listEntry;
                        convertToVelocity(map, vmTeamplate, newKeyVMTemplate, entry.getKey());
                    }
                    else {
                    	vmTeamplate.append("#set($document").append(newKeyVMTemplate).append(" = ").append(listEntry).append(")\n");
                    }
                    	
                }
						 
            } else if(entry.getValue() instanceof String || entry.getValue() == null || entry.getValue() instanceof Boolean 
            		|| entry.getValue() instanceof Integer || entry.getValue() instanceof List ) {
            	
            	String tempString = "";
            	if(entry.getValue() instanceof String) {
            		tempString = "\""+entry.getValue()+"\"";
            		vmTeamplate.append("#set($document").append(newKeyVMTemplate).append(" = ").append(tempString).append(")\n");
            	}
            	else {
            		vmTeamplate.append("#set($document").append(newKeyVMTemplate).append(" = ").append(entry.getValue()).append(")\n");
            	}
            }
            
        }
		return vmTeamplate;
	}
}

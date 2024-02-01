package pw.react.backend.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static String[] getNullAndEmailPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> excludedNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null || "email".equals(pd.getName())) {
                excludedNames.add(pd.getName());
            }
        }

        String[] result = new String[excludedNames.size()];
        return excludedNames.toArray(result);
    }
}

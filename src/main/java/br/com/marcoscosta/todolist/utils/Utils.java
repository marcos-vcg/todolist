package br.com.marcoscosta.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNonNullPropertyNamesFromSourceToTarget(Object source, Object target) {
        String[] ignoreProperties = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }
    
    
    public static String[] getNullPropertyNames(Object source) {
        
        Set<String> emptyPropertyNames = new HashSet<>();
        
        final BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            Object object = beanWrapper.getPropertyValue(property.getName());
            if (object == null) {
                emptyPropertyNames.add(property.getName());
            }
        }
        
        String[] result = new String[emptyPropertyNames.size()];
        return emptyPropertyNames.toArray(result);
    }
}

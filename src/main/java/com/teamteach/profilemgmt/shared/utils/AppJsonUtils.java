package com.teamteach.profilemgmt.shared.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public class AppJsonUtils {

    public static String serializeClass(final Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error During Serialization");
            return null;
        }
    }

    public static <T> T convertToTypedObject(final Object obj , TypeReference<T> typeReference){
        ObjectMapper mapper = new ObjectMapper();
        try {
            var str = mapper.writeValueAsString(obj);
            return deserializeClass(str, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error During Serialization");
            return null;
        }
    }

    public static <T> T convertToTypedObject(final Object obj , Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        try {
            var str = mapper.writeValueAsString(obj);
            return deserializeClass(str, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error During Serialization");
            return null;
        }
    }

    public static <T> T deserializeClass(InputStream inputStream , Class<T> clazz){
        if (inputStream == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("Error During Deserialization");
            return null;
        }
    }

    public static <T> T deserializeClass(String obj , TypeReference<T> typeReference){
        if (obj == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            obj = obj.replaceAll("\n","");
            T value = mapper.readValue(obj, typeReference);
            return value;
        } catch (IOException e) {
            log.error("Exception during deserialization {}" , e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T deserializeClass(String obj , Class<T> clazz){
        if (obj == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            obj = obj.replaceAll("\n","");
            T value = mapper.readValue(obj, clazz);
            return value;
        } catch (IOException e) {
            log.error("Exception during deserialization {}" , e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T deserializeClass(String obj , Class<T> clazz, InjectableValues.Std injectVal){
        if (obj == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            obj = obj.replaceAll("\n","");
            T value = mapper.setInjectableValues(injectVal).readValue(obj, clazz);
            return value;
        } catch (IOException e) {
            log.error("Exception during deserialization {}" , e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Class<?> getClass(Type type)
    {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        return null;
    }

}

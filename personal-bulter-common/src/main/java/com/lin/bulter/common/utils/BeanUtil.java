package com.lin.bulter.common.utils;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BeanUtil {

    @FunctionalInterface
    public interface CustomFunction<R, T> {
        void doTask(R r, T t);
    }

    // 缓存map
    static final Map<String, BeanCopier> BEAN_COPIERS = new HashMap<String, BeanCopier>();

    /**
     * bean属性复制
     *
     * @param srcObj
     */
    public static <T, R> R copy(T srcObj, Supplier<R> supplier) {
        R destObj = supplier.get();
        beanCopy(srcObj, destObj);
        return destObj;
    }

    /**
     * bean属性复制
     *
     * @param srcObj
     * @param function
     */
    public static <T, R> R copy(T srcObj, Supplier<R> supplier, CustomFunction<T, R> function) {
        R destObj = supplier.get();
        beanCopy(srcObj, destObj);
        function.doTask(srcObj, destObj);
        return destObj;
    }

    /**
     * bean属性复制
     *
     * @param srcObjList
     */
    public static <T, R> List<R> copyList(List<T> srcObjList, Supplier<R> supplier) {
        List<R> resultList = new ArrayList<>();
        for (T srcObj : srcObjList) {
            R destObj = supplier.get();
            beanCopy(srcObj, destObj);
            resultList.add(destObj);
        }
        return resultList;
    }

    /**
     * bean属性复制
     *
     * @param srcObjList
     * @param function
     */
    public static <T, R> List<R> copyList(List<T> srcObjList, Supplier<R> supplier, CustomFunction<T, R> function) {
        List<R> resultList = new ArrayList<>();
        for (T srcObj : srcObjList) {
            R destObj = supplier.get();
            beanCopy(srcObj, destObj);
            function.doTask(srcObj, destObj);
            resultList.add(destObj);
        }
        return resultList;
    }

    /**
     * bean属性复制
     *
     * @param srcObj
     * @param destObj
     */
    public static void copyWithConverter(Object srcObj, Object destObj, Converter converter) {
        String key = genKey(srcObj.getClass(), destObj.getClass());
        BeanCopier copier = null;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), true);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, converter);
    }

    /**
     * 生成缓存key
     *
     * @param srcClazz
     * @param destClazz
     * @return
     */
    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }

    /**
     * bean属性复制
     *
     * @param srcObj
     * @param destObj
     */
    private static void beanCopy(Object srcObj, Object destObj) {
        String key = genKey(srcObj.getClass(), destObj.getClass());
        BeanCopier copier = null;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, null);
    }

    public static void main(String[] args) {

//        List<UserDto> srcList = new ArrayList<>();
//        srcList.add(new UserDto("lin", "123"));
//        srcList.add(new UserDto("lin2", "123455"));
//        List<UserDto2> destList = copyList(srcList, UserDto2::new, (userDto1, userDto2) -> {
//            userDto2.setA(userDto2.getA()+"22222");
//        });
//        System.out.println(destList);


//        UserDto userDto = new UserDto("lin", "123");
//        UserDto2 user2 = copy(userDto, UserDto2::new);
//        System.out.println(user2);
    }
}

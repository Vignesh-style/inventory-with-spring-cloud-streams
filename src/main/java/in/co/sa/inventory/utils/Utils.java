package in.co.sa.inventory.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Utils {

    public static boolean isMapNullOrEmpty(Map<?, ?> map)
    {
        return map == null || map.isEmpty();
    }

    public static <K> void diff(Set<K> set1, Set<K> set2, Set<K> onlyInFirst, Set<K> onlyInSecond, Set<K> commonElements)
    {
        onlyInFirst.addAll(set1);
        onlyInSecond.addAll(set2);

        commonElements.addAll(set1);
        commonElements.retainAll(onlyInSecond);

        onlyInFirst.removeAll(commonElements);
        onlyInSecond.removeAll(commonElements);
    }

    public static boolean isCollectionNullOrEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }

    public static boolean isCollectionNotNullAndNotEmpty(Collection<?> collection)
    {
        return collection != null && !collection.isEmpty();
    }


    public static boolean nullOrEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }

    public static<T> boolean nullOrEmpty(T[] array)
    {
        return array == null || array.length == 0;
    }

    public static Long getLongIfBigDecimal(Object o)
    {
        if(o instanceof BigDecimal)
            return ((BigDecimal) o).longValue();
        else if (o instanceof String)
            return Long.valueOf((String) o);

        return (Long) o;
    }

    public static boolean isStringNullOrEmpty(String string, boolean trimBeforeEmptyCheck)
    {
        return nullOrEmpty(string, trimBeforeEmptyCheck);
    }

    public static boolean nullOrEmpty(String string, boolean trimBeforeEmptyCheck)
    {
        if (string == null)
            return true;

        return trimBeforeEmptyCheck ? string.trim().isEmpty() : string.isEmpty();
    }

    public static boolean hasElement(Collection<?> collection)
    {
        return collection != null && !collection.isEmpty();
    }

}

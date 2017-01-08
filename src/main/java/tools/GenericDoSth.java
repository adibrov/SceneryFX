package tools;

import java.util.Objects;

/**
 * Created by dibrov on 05/01/17.
 */
public class GenericDoSth {
    public static <T extends Integer> GenericObject<T> foo(T a) {
        return new GenericObject<T>();
    }


    public static void main(String[] args) {
        GenericObject<Integer> genInt = foo(2);
    }
}


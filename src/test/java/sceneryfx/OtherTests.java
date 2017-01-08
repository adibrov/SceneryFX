package sceneryfx;


import cleargl.GLTypeEnum;
import org.junit.Test;

/**
 * Created by dibrov on 05/01/17.
 */
public class OtherTests {

    @Test public void GLEnumTest(){
        GLTypeEnum glEnumEx = GLTypeEnum.Short;
        System.out.println(glEnumEx.valueOf("Byte").getClass());
    }
}

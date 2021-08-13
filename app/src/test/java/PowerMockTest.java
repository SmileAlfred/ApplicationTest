import android.content.Context;

import com.example.applicationtest.MUtils;
import com.example.applicationtest.R;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * 2021-08-13 Unit tests for {@link MUtils}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MUtils.class})
public class PowerMockTest {

    @Test
    public void should_prevent_invoking_of_private_method_but_return_result_of_it() throws Exception {
        //对于私有静态方法mock 不 可行
        PowerMockito.mockStatic(MUtils.class);
        when(MUtils.class, "anotherMethod").thenReturn("abc");

        String retrieved = MUtils.method();

        Assert.assertNotNull(retrieved);
        Assert.assertEquals(retrieved, "abc");
    }

    @Test
    //测试私有静态方法用spy
    public void testMethod() throws Exception {
        PowerMockito.spy(MUtils.class);
        //无返回值无参方法
        PowerMockito.doNothing().when(MUtils.class, "voidMethod");
        //无返回值有参方法
        PowerMockito.doNothing().when(MUtils.class, "voidArgMethod", "Er", 2);
        //有返回值无参方法
        PowerMockito.doReturn("abc").when(MUtils.class, "anotherMethod");

        String retrieved = MUtils.method();

        Assert.assertNotNull(retrieved);
        Assert.assertEquals(retrieved, "abc");
    }

    @Test
    public void testContext() {
        Context mock = PowerMockito.mock(Context.class);
        System.out.println(mock.getString(R.string.app_name));//null
        when(mock.getString(anyInt())).thenCallRealMethod();
        //java.lang.RuntimeException: Method getString in android.content.Context not mocked.
        System.out.println(mock.getString(R.string.app_name));
    }

}

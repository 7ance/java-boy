package cn.lance.test;

import cn.lance.entity.Bar;
import cn.lance.entity.Foo;
import cn.lance.json.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class MockitoTest {

    @Test
    public void testMock() throws JsonProcessingException {
        Foo noArgFoo = new Foo();
        String noArgFooJson = JsonUtils.write(noArgFoo);
        System.out.println("Foo noArgJson: \n" + noArgFooJson);

        Foo mockFoo = mock(Foo.class);
        String mockFooJson = JsonUtils.write(mockFoo);
        System.out.println("Foo mockJson: \n" + mockFooJson);

        Bar noArgBar = new Bar();
        String noArgBarJson = JsonUtils.write(noArgBar);
        System.out.println("Bar noArgJson: \n" + noArgBarJson);

        Bar mockBar = mock(Bar.class);
        String mockBarJson = JsonUtils.write(mockBar);
        System.out.println("Bar mockJson: \n" + mockBarJson);
    }

    @Test
    public void testWhen() {
        Bar mock = mock(Bar.class);
        when(mock.getInteger()).thenReturn(200);
        Integer integer = mock.getInteger();
        System.out.println("integer: " + integer);
        Assertions.assertEquals(200, integer);
    }

}

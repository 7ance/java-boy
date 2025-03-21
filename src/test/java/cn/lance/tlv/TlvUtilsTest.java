package cn.lance.tlv;

import cn.lance.json.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TlvUtilsTest {

    @Test
    public void testParse() throws JsonProcessingException {
        String text = "00020101110001A0102BB";
        List<TlvObject> result = TlvUtils.parse(text);
        System.out.println(JsonUtils.write(result));
    }

}

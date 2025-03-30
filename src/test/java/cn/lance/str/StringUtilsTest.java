package cn.lance.str;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void testFormat() {
        String template = "Today is a {} day.";
        String str1 = StringUtils.format(template, "good", "best", "bad");
        System.out.println("str1: " + str1);

        String str2 = StringUtils.format(template);
        System.out.println("str2: " + str2);

        String template2 = "This month have {} days.";
        String str3 = StringUtils.format(template2, 31);
        System.out.println("str3: " + str3);
    }

}

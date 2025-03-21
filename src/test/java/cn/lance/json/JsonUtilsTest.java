package cn.lance.json;

import cn.lance.entity.Bar;
import cn.lance.entity.Foo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class JsonUtilsTest {

    @Test
    public void testGetDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = JsonUtils.getDefaultObjectMapper();
        System.out.println(defaultObjectMapper);
    }

    @Test
    public void testIsValidJson() {
        String str1 = "{\"a\":1,\"b\":2,\"c\":3}";
        boolean valid1 = JsonUtils.isValidJson(str1);
        System.out.println("valid1: " + valid1);

        String str2 = "{}";
        boolean valid2 = JsonUtils.isValidJson(str2);
        System.out.println("valid2: " + valid2);

        String str3 = "Programming for fun";
        boolean valid3 = JsonUtils.isValidJson(str3);
        System.out.println("valid3: " + valid3);
    }

    @Test
    public void testWrite() throws JsonProcessingException {
        Bar bar = new Bar();
        bar.setInteger(93);

        String jsonBar = JsonUtils.write(bar);
        System.out.println("jsonBar: " + jsonBar);

        Foo foo = new Foo();
        foo.setC('T');
        foo.setCha('K');
        foo.setStr("Complex");
        foo.setBo(true);
        foo.setBoo(false);
        foo.setBy((byte) 93);
        foo.setByt((byte) 37);
        foo.setSho((short) 48);
        foo.setShor((short) 49);
        foo.setIn(12);
        foo.setInteger(13);
        foo.setLo(14L);
        foo.setLon(15L);
        foo.setFl(17);
        foo.setFlo(18F);
        foo.setDoubleAlpha(21.02);
        foo.setDoubleBeta(21.05);
        foo.setDate(new Date());
        foo.setLocalDate(LocalDate.now());
        foo.setLocalDateTime(LocalDateTime.now());
        foo.setBigInteger(BigInteger.TEN);
        foo.setBigDecimal(BigDecimal.TWO);

        List<String> stringList = new ArrayList<>();
        stringList.add("X");
        stringList.add("Y");
        stringList.add("Z");
        foo.setStringList(stringList);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", 456);
        map.put("key2", 789);
        foo.setMap(map);

        List<Bar> barList = new ArrayList<>();
        barList.add(bar);
        foo.setBarList(barList);

        String jsonFoo = JsonUtils.write(foo);
        System.out.println("jsonFoo: " + jsonFoo);
    }

    @Test
    public void testWritePretty() throws JsonProcessingException {
        Bar bar = new Bar();
        bar.setInteger(93);

        String jsonBar = JsonUtils.writePretty(bar);
        System.out.println("jsonBar pretty: " + jsonBar);

        Foo foo = new Foo();
        foo.setC('T');
        foo.setCha('K');
        foo.setStr("Complex");
        foo.setBo(true);
        foo.setBoo(false);
        foo.setBy((byte) 93);
        foo.setByt((byte) 37);
        foo.setSho((short) 48);
        foo.setShor((short) 49);
        foo.setIn(12);
        foo.setInteger(13);
        foo.setLo(14L);
        foo.setLon(15L);
        foo.setFl(17);
        foo.setFlo(18F);
        foo.setDoubleAlpha(21.02);
        foo.setDoubleBeta(21.05);
        foo.setDate(new Date());
        foo.setLocalDate(LocalDate.now());
        foo.setLocalDateTime(LocalDateTime.now());
        foo.setBigInteger(BigInteger.TEN);
        foo.setBigDecimal(BigDecimal.TWO);

        List<String> stringList = new ArrayList<>();
        stringList.add("X");
        stringList.add("Y");
        stringList.add("Z");
        foo.setStringList(stringList);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", 456);
        map.put("key2", 789);
        foo.setMap(map);

        List<Bar> barList = new ArrayList<>();
        barList.add(bar);
        foo.setBarList(barList);

        String jsonFoo = JsonUtils.writePretty(foo);
        System.out.println("jsonFoo pretty: " + jsonFoo);
    }

    @Test
    public void testWriteWithFeature() throws JsonProcessingException {
        Bar bar = new Bar();
        bar.setInteger(93);

        Foo foo = new Foo();
        foo.setC('T');
        foo.setCha('K');
        foo.setStr("Complex");
        foo.setBo(true);
        foo.setBoo(false);
        foo.setBy((byte) 93);
        foo.setByt((byte) 37);
        foo.setSho((short) 48);
        foo.setShor((short) 49);
        foo.setIn(12);
        foo.setInteger(13);
        foo.setLo(14L);
        foo.setLon(15L);
        foo.setFl(17);
        foo.setFlo(18F);
        foo.setDoubleAlpha(21.02);
        foo.setDoubleBeta(21.05);
        foo.setDate(new Date());
        foo.setLocalDate(LocalDate.now());
        foo.setLocalDateTime(LocalDateTime.now());
        foo.setBigInteger(BigInteger.TEN);
        foo.setBigDecimal(BigDecimal.TWO);

        List<String> stringList = new ArrayList<>();
        stringList.add("X");
        stringList.add("Y");
        stringList.add("Z");
        foo.setStringList(stringList);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", 456);
        map.put("key2", 789);
        foo.setMap(map);

        List<Bar> barList = new ArrayList<>();
        barList.add(bar);
        foo.setBarList(barList);

        List<SerializationFeature> enable = new ArrayList<>();
        enable.add(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        List<SerializationFeature> disable = new ArrayList<>();
        String jsonFoo = JsonUtils.writeWithFeature(foo, enable, disable);
        System.out.println("jsonFoo with feature: " + jsonFoo);
    }

    @Test
    public void testRead() throws JsonProcessingException {
        String json1 = "{\"integer\":93}";
        Bar bar = JsonUtils.read(json1, Bar.class);
        System.out.println("bar: " + bar);

        String json2 = "{\"c\":\"T\",\"cha\":\"K\",\"str\":\"Complex\",\"bo\":true,\"boo\":false,\"by\":93,\"byt\":37,\"sho\":48,\"shor\":49,\"in\":12,\"integer\":13,\"lo\":14,\"lon\":15,\"fl\":17.0,\"flo\":18.0,\"doubleAlpha\":21.02,\"doubleBeta\":21.05,\"date\":\"2025-03-19 22:59:52\",\"localDate\":\"2025-03-19\",\"localDateTime\":\"2025-03-19 22:59:52\",\"bigInteger\":10,\"bigDecimal\":2,\"stringList\":[\"X\",\"Y\",\"Z\"],\"map\":{\"key1\":456,\"key2\":789},\"barList\":[{\"integer\":93}]}";
        Foo foo = JsonUtils.read(json2, Foo.class);
        System.out.println("foo: " + foo);
    }

    @Test
    public void testReadWithFeature() throws JsonProcessingException {
        String json1 = "{\"integer\":93}";
        List<DeserializationFeature> enable = new ArrayList<>();
        enable.add(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<DeserializationFeature> disable = new ArrayList<>();
        Bar bar1 = JsonUtils.readWithFeature(json1, Bar.class, enable, disable);
        System.out.println("bar1: " + bar1);

        Exception exception = Assertions.assertThrows(UnrecognizedPropertyException.class,
                () -> {
                    String json2 = "{\"not-exist\":77}";
                    List<DeserializationFeature> enable2 = new ArrayList<>();
                    enable2.add(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    List<DeserializationFeature> disable2 = new ArrayList<>();
                    Bar bar2 = JsonUtils.readWithFeature(json2, Bar.class, enable2, disable2);
                    System.out.println("bar2: " + bar2);
                });
        System.out.println(exception.toString());
    }

    @Test
    public void testReadList() throws JsonProcessingException {
        List<Bar> barList = new ArrayList<>();
        barList.add(new Bar(1));
        barList.add(new Bar(2));
        barList.add(new Bar(3));
        String listJson = JsonUtils.write(barList);


        List<Bar> barListParsed = JsonUtils.readList(listJson, Bar.class);
        System.out.println("barListParsed: " + barListParsed);

        List<Foo> fooListParsed = JsonUtils.readList(listJson, Foo.class);
        System.out.println("fooListParsed: " + fooListParsed);
    }

    @Test
    public void testReadMap() throws JsonProcessingException {
        String json = """
                {
                  "c" : "T",
                  "cha" : "K",
                  "str" : "Complex",
                  "bo" : true,
                  "boo" : false,
                  "by" : 93,
                  "byt" : 37,
                  "sho" : 48,
                  "shor" : 49,
                  "in" : 12,
                  "integer" : 13,
                  "lo" : 14,
                  "lon" : 15,
                  "fl" : 17.0,
                  "flo" : 18.0,
                  "doubleAlpha" : 21.02,
                  "doubleBeta" : 21.05,
                  "date" : "2025-03-19 23:13:05",
                  "localDate" : "2025-03-19",
                  "localDateTime" : "2025-03-19 23:13:05",
                  "bigInteger" : 10,
                  "bigDecimal" : 2,
                  "stringList" : [ "X", "Y", "Z" ],
                  "map" : {
                    "key1" : 456,
                    "key2" : 789
                  },
                  "barList" : [ {
                    "integer" : 93
                  } ]
                }""";
        Map<String, Object> map = JsonUtils.readMap(json);
        System.out.println("map: " + map);
    }

    @Test
    public void testReadTree() throws JsonProcessingException {
        String json = """
                {
                  "c" : "T",
                  "cha" : "K",
                  "str" : "Complex",
                  "bo" : true,
                  "boo" : false,
                  "by" : 93,
                  "byt" : 37,
                  "sho" : 48,
                  "shor" : 49,
                  "in" : 12,
                  "integer" : 13,
                  "lo" : 14,
                  "lon" : 15,
                  "fl" : 17.0,
                  "flo" : 18.0,
                  "doubleAlpha" : 21.02,
                  "doubleBeta" : 21.05,
                  "date" : "2025-03-19 23:13:05",
                  "localDate" : "2025-03-19",
                  "localDateTime" : "2025-03-19 23:13:05",
                  "bigInteger" : 10,
                  "bigDecimal" : 2,
                  "stringList" : [ "X", "Y", "Z" ],
                  "map" : {
                    "key1" : 456,
                    "key2" : 789
                  },
                  "barList" : [ {
                    "integer" : 93
                  } ]
                }""";
        JsonNode jsonNode = JsonUtils.readTree(json);
        System.out.println("jsonNode: " + jsonNode);
    }

    @Test
    public void compress() throws JsonProcessingException {
        String json1 = """
                {
                  "integer" : 93
                }""";
        String compress1 = JsonUtils.compress(json1);
        System.out.println("compress1: " + compress1);

        String json2 = """
                {
                  "c" : "T",
                  "cha" : "K",
                  "str" : "Complex",
                  "bo" : true,
                  "boo" : false,
                  "by" : 93,
                  "byt" : 37,
                  "sho" : 48,
                  "shor" : 49,
                  "in" : 12,
                  "integer" : 13,
                  "lo" : 14,
                  "lon" : 15,
                  "fl" : 17.0,
                  "flo" : 18.0,
                  "doubleAlpha" : 21.02,
                  "doubleBeta" : 21.05,
                  "date" : "2025-03-19 23:13:05",
                  "localDate" : "2025-03-19",
                  "localDateTime" : "2025-03-19 23:13:05",
                  "bigInteger" : 10,
                  "bigDecimal" : 2,
                  "stringList" : [ "X", "Y", "Z" ],
                  "map" : {
                    "key1" : 456,
                    "key2" : 789
                  },
                  "barList" : [ {
                    "integer" : 93
                  } ]
                }""";
        String compress2 = JsonUtils.compress(json2);
        System.out.println("compress2: " + compress2);
    }

    @Test
    public void pretty() throws JsonProcessingException {
        String json1 = "{\"integer\":93}";
        String pretty1 = JsonUtils.pretty(json1);
        System.out.println("pretty1: " + pretty1);

        String json2 = "{\"c\":\"T\",\"cha\":\"K\",\"str\":\"Complex\",\"bo\":true,\"boo\":false,\"by\":93,\"byt\":37,\"sho\":48,\"shor\":49,\"in\":12,\"integer\":13,\"lo\":14,\"lon\":15,\"fl\":17.0,\"flo\":18.0,\"doubleAlpha\":21.02,\"doubleBeta\":21.05,\"date\":\"2025-03-19 22:59:52\",\"localDate\":\"2025-03-19\",\"localDateTime\":\"2025-03-19 22:59:52\",\"bigInteger\":10,\"bigDecimal\":2,\"stringList\":[\"X\",\"Y\",\"Z\"],\"map\":{\"key1\":456,\"key2\":789},\"barList\":[{\"integer\":93}]}";
        String pretty = JsonUtils.pretty(json2);
        System.out.println("pretty2: " + pretty);
    }

}

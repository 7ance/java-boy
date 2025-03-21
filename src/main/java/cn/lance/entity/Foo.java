package cn.lance.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Foo {

    private char c;

    private Character cha;

    private String str;

    private boolean bo;

    private Boolean boo;

    private byte by;

    private Byte byt;

    private short sho;

    private Short shor;

    private int in;

    private Integer integer;

    private long lo;

    private Long lon;

    private float fl;

    private Float flo;

    private double doubleAlpha;

    private Double doubleBeta;

    private Date date;

    private LocalDate localDate;

    private LocalDateTime localDateTime;

    private BigInteger bigInteger;

    private BigDecimal bigDecimal;

    private List<String> stringList;

    private Map<String, Object> map;

    private List<Bar> barList;

}

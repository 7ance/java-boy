package cn.lance.tlv;

import lombok.Data;

import java.util.List;

@Data
public class TlvObject {

    private String tag;

    private int length;

    private String value;

    private List<TlvObject> subTags;

}
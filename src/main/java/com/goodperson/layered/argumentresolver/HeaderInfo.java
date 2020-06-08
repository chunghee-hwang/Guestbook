package com.goodperson.layered.argumentresolver;

import java.util.HashMap;
import java.util.Map;

// 기본 아규먼트 리졸버로 맵 객체를 
//파라미터로 넘겨주는 것이 있기 때문에 
//이렇게 따로 클래스로 만듦
public class HeaderInfo {
    private Map<String, String> map;

    public HeaderInfo() {
        map = new HashMap<>();
    }

    public void put(String name, String value) {
        map.put(name, value);
    }

    public String get(String name) {
        return map.get(name);
    }

}

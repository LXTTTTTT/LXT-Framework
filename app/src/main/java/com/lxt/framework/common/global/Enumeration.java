package com.lxt.framework.common.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Enumeration {

    public enum TypeFace{
        XIAOBAI("小白", "typeface/baotuxiaobaiti.ttf"),
        YUANHEI("圆黑","typeface/ChillRoundGothic_Bold.ttf"),
        ZHUSHI("竹石","typeface/yangrendongzhushiti.ttf");

        private final String name;  // 固定类型
        private final String path;  // 文件路径

        TypeFace(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }


        public static List<TypeFace> list() {
            List<TypeFace> typeFaceList = new ArrayList<>();
            for (TypeFace value : values()) {
                typeFaceList.add(value);
            }
            return typeFaceList;
        }

        public static TypeFace get(final String name){
            return Arrays.stream(values())
                    .filter(typeFace -> typeFace.getName().equals(name))
                    .findFirst()
                    .orElseGet(null);
        }
    }

}

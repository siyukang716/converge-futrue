package com.cloud.study;


import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Sort {
    static class Obj {
        private String name;
        private BigDecimal price;

        public Obj(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public Obj() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Obj [name=" + name + ", price=" + price + "]";
        }

        public static List<Obj> TopCustomSort(List<Obj> list){
            //降序就是反过来  x = 1  y = 2
            // x < y  return  1   y排前面
            // x > y  return  -1  x排前面
            // x = y  return  0    不变

            //第九名 > 第八名 > 第七名
            List<Obj> result = list.stream().sorted(
                    //先按照name排序（模拟需求的a属性排序）
                    Comparator.comparing(Obj::getName, (x, y) -> {
                        System.out.print(x+"\t");
                        System.out.println(y);
                        if (x.contains(y)){
                            return 0;
                        }else {
                            if ("第九名".contains(x)) {
                                return -1;
                            }else if ("第九名".contains(y)) {
                                return 1;
                            }
                            if ("第八名".contains(x)) {
                                if ("第九名".contains(y)){
                                    return 1;
                                }else {
                                    return -1;
                                }
                            }else if ("第八名".contains(y)) {
                                if ("第九名".contains(x)) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        }
                        return 0;
                    })
                            //再按照其他字段排序，要考虑null（模拟需求b和c字段排序）
                            .thenComparing(Comparator.comparing(Obj::getPrice,
                                    Comparator.nullsFirst(BigDecimal::compareTo)).reversed())
            ).collect(Collectors.toList());
            System.out.println(JSON.toJSON(result));
            System.out.println(result.size());
            return result;
        }


        public static void main(String[] args) {

            List<Obj> list = Arrays.asList(
                    new Obj("第一名", new BigDecimal("1")),
                    new Obj("第二名", new BigDecimal("2")),
                    new Obj("第三名", new BigDecimal("3")),
                    new Obj("第四名", new BigDecimal("4")),

                    new Obj("第九名", new BigDecimal("9")),

                    new Obj("第五名", new BigDecimal("4")),
                    new Obj("第六名", new BigDecimal("10")),
                    new Obj("第七名", new BigDecimal("10")),
                    new Obj("第八名", new BigDecimal("10")),
                    new Obj("第八名", new BigDecimal("101")),
                    new Obj("零", null)
            );
            //置顶自定义排序
            List<Obj> objs = TopCustomSort(list);

        }
    }
}
package com.heima.wemedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * lambda表达式
 */
public class StreamTest {

    /**
     * 8、补充 Collectors
     * Collector 接口中方法的实现决定了如何对流执行收集操作(如收集到 List、Set、Map)。但是 Collectors 实用类提供了很多静态方法，可以方便地创建常见收集器实例
     *
     * list.stream().collect(Collectors.toList());
     *
     * 把流中元素收集到List
     *
     * list.stream().collect(Collectors.toSet());
     *
     * 把流中元素收集到Set
     *
     * list.stream().collect(Collectors.toCollection(ArrayList::new));
     *
     * 把流中元素收集到创建的集合
     * list.stream().collect(Collectors.counting());
     *
     * 计算流中元素的个数
     *
     * list.stream().collect(Collectors.summingInt(User::getAge));
     *
     * 对流中元素的整数属性求和
     * list.stream().collect(Collectors.averagingInt(User::getAge));
     *
     * 计算流中元素Integer属性的平均值
     *
     * list.stream().collect(Collectors.summarizingInt(User::getAge));
     *
     * 收集流中Integer属性的统计值。如：平均值
     *
     * list.stream().collect(Collectors.groupingBy(User::getAge));
     *
     * 根据某属性值对流分组，属性为K，结果为V
     * 最大值maxBy和最小值 minBy
     */
    @Test
    public void test8() {
//        把流中元素收集到List
        List<Student> collect = getStudentList().stream().collect(Collectors.toList());
        System.out.println(collect);

//        把流中元素收集到Set
        Set<Student> collect1 = getStudentList().stream().collect(Collectors.toSet());
        System.out.println(collect1);


//        把流中元素收集到创建的集合
        ArrayList<Student> collect2 = getStudentList().stream().collect(Collectors.toCollection(ArrayList::new));
        System.out.println(collect2);

//        计算流中元素的个数
        Long collect3 = getStudentList().stream().collect(Collectors.counting());
        System.out.println(collect3);

//        对流中元素的整数属性求和
        Integer collect4 = getStudentList().stream().collect(Collectors.summingInt(Student::getAge));
        System.out.println(collect4);

//        计算流中元素Integer属性的平均值
        Double collect5 = getStudentList().stream().collect(Collectors.averagingDouble(Student::getSalary));
        System.out.println(collect5);

//        根据某属性值对流分组，属性为K，结果为V
        Map<Integer, List<Student>> collect6 = getStudentList().stream().collect(Collectors.groupingBy(Student::getAge));
        System.out.println(collect6);

//        最大值maxBy和最小值 minBy
        List<String> list = Arrays.asList("1", "2", "3", "4");
        Optional<String> max = list.stream().collect(Collectors.maxBy((s, v) -> s.compareTo(v)));
        Optional<String> min = list.stream().collect(Collectors.minBy((s, v) -> s.compareTo(v)));
        System.out.println(max);
        System.out.println(min);

    }


    /**
     * 7、跳过 skip
     *  或者
     *  获取  limit
     */
    @Test
    public void test7() {
        List<Student> collect = getStudentList().stream().skip(2).collect(Collectors.toList());
        System.out.println(collect);

        List<Student> collect1 = getStudentList().stream().limit(2).collect(Collectors.toList());
        System.out.println(collect1);
    }

    /**
     * 6、快速找到集合中想要匹配的数据 anyMatch
     */
    @Test
    public void test6() {
        boolean match = getStudentList().stream().anyMatch(student -> "男".equals(student.getSex()));
        System.out.println(match);
    }

    /**
     * 5、去重 distinct
     */
    @Test
    public void test5() {
        List<Student> collect = getStudentList().stream().distinct().collect(Collectors.toList());
        System.out.println(collect);

//        也可以根据某一属性去重
        ArrayList<Student> collect1 = getStudentList().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparingLong(Student::getAge))), ArrayList::new));
        System.out.println(collect1);
    }

    /**
     * 4、排序 sorted
     * 按照类里面的某个属性进行排序 场景：按照学生的年龄进行排序倒序排序
     */
    @Test
    public void teast4() {
        List<Student> collect = getStudentList().stream().sorted(Comparator.comparing(Student::getAge).reversed()).collect(Collectors.toList());
        System.out.println(collect);
    }


    /**
     * 3、过滤  filter
     * 从一个数组中筛选出来一些数据。 场景：找出学生列表中所有性别为男的学生 java 8以前的写法：
     */
    @Test
    public void test3() {
        ArrayList<Student> students = new ArrayList<>();
        for (Student student : students) {
            if ("男".equals(student.getSex())) {
                students.add(student);
            }
        }
        System.out.println(students);

        List<Student> collect = getStudentList().stream().filter(student -> student.getSex() == "男").collect(Collectors.toList());
        System.out.println(collect);
    }


    /**
     * 2、类型转换2  Collectors.toMap
     * 将List转换为Map。数组的查找效率是O(n)。效率还是比较低的，我们可以把他转化为Map。在知道key的情况下，访问速度就变成了O(1)。速度提升了很多。
     * 场景：学生的集合已经获取到，他的学号是主键，我现在想要找出学号为0001的学生的名字。java 8以前的写法：
     */
    @Test
    public void test2() {
        for (Student student : getStudentList()) {
            if (1==student.getId()) {
                System.out.println(student.getName());
            }
        }

//        java 8的写法
//        这是取两个属性字段，当然也可以用对象本省作为value，提供了一个属性方法：Function.identity()。可以获取对象本身。
        Map<Integer, String> collect = getStudentList().stream().collect(Collectors.toMap(Student::getId, Student::getName));
        System.out.println(collect.get(1));
    }


    /**
     * 1、类型转换1  map
     * 可以将一个类的对象集合中的某个属性生成新的集合。 比如一个学生类，学生包含学号、姓名、性别、手机号码、qq号。
     * 场景：几个学生集合，将其中所有的学号抽取出来生成新的集合。原来的写法只有遍历这个学生集合，拿到他的学号属性添加到学号的集合中。
     * 代码如下：
     */
    @Test
    public void test1() {
        ArrayList<String> stuNames = new ArrayList<>();
        for (Student student : getStudentList()) {
            stuNames.add(student.getName());
        }
        System.out.println(stuNames);

//        这样要不断的循环，效率也会低很多。java 8中写法：
        List<String> collect = getStudentList().stream().map(Student::getName).collect(Collectors.toList());
        System.out.println(collect);
    }

    public static List<Student> getStudentList(){
        return Arrays.asList(
                new Student(1,"赵云",28,"男",18888.0),
                new Student(2,"孙尚香",22,"女",23145.0),
                new Student(3,"关羽",31,"男",21321.0),
                new Student(4,"貂蝉",44,"女",9000.0),
                new Student(5,"刘备",51,"男",54000.0),
                new Student(6,"甄姬",25,"女",16888.0),
                new Student(7,"曹操",66,"男",17888.0),
                new Student(8,"小乔",34,"女",28888.0),
                new Student(8,"小乔",34,"女",28888.0)
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class Student {
        private Integer id;
        private String name;
        private Integer age;
        private String sex;
        private Double salary;
    }
}

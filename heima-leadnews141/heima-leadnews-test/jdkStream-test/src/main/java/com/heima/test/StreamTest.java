package com.heima.test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @作者 itcast
 * @创建日期 2021/4/15 21:43
 **/
public class StreamTest {
    public static void main(String[] args) {
        // 通过集合获取流
//        List<Student> studentList = getStudentList();
//        Stream<Student> stream = studentList.stream();
//        // 通过数组获取流
//        IntStream stream1 = Arrays.stream(new int[]{1, 2, 3, 4, 5, 6});
//        // 通过零散的数据获取流
//        Stream<String> stringStream = Stream.of("1", "123");
        List<Student> studentList = getStudentList();
        System.out.println(studentList
                .stream()   // 创建流
                .filter(student -> student.getSalary() > 20000)
                //                .forEach(student -> System.out.println(student));
                .distinct()
                //                .skip(1)
                //                .limit(2)
                // name : salary
                //                .map(student -> student.getName() + ":" + student.getSalary())
                .sorted(Comparator.comparing(Student::getSalary).reversed())
                //  关羽:28888,刘备:54000,xxxxx
                .map(student -> student.getName() + ":" + student.getSalary())
                .collect(Collectors.joining(",")));
//                .collect(Collectors.toList());
//                .forEach(System.out::println);
//                .count();
        System.out.println("=====================================");

//        Stream.of("hello boy hehe", "welcome to china").flatMap(new Function<String, Stream<?>>() {
//            @Override
//            public Stream<?> apply(String s) {
//                String[] arr = s.split(" ");
//                return Arrays.stream(arr);
//            }
//        }).forEach(System.out::println);
//

//        System.out.println(Stream.of(1,2,3,4).reduce((a1,a2)->a1+a2));

        Map<String, List<Student>> collect = getStudentList().stream()
                .collect(Collectors.groupingBy(Student::getSex));

        System.out.println(collect);


        Map<String, Double> collect1 = getStudentList().stream()
                .distinct()
                .collect(Collectors.toMap(Student::getName, Student::getSalary));

        System.out.println(collect1);



        //  reduce  归并计算

        //  foreach  遍历

        //  collect  收集
        //     Collectors.toList  ==>  ArrayList集合

        //                joining ==>  拼接成字符串

        //                groupingBy ==>  流中数据进行分组

        //                toMap   ==> 转成map集合
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
    static class Student{
        private Integer id;
        private String name;
        private Integer age;
        private String sex;
        private Double salary;
        public Student(Integer id, String name, Integer age, String sex, Double salary) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.sex = sex;
            this.salary = salary;
        }
        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", sex='" + sex + '\'' +
                    ", salary=" + salary +
                    '}';
        }
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Double getSalary() {
            return salary;
        }

        public void setSalary(Double salary) {
            this.salary = salary;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(age, student.age) && Objects.equals(sex, student.sex) && Objects.equals(salary, student.salary);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age, sex, salary);
        }
    }
}

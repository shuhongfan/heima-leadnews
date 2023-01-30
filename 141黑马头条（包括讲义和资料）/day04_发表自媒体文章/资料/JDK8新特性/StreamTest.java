package com.itheima.kafka.simple;

import java.util.*;

/**
 * @作者 itcast
 * @创建日期 2021/4/15 21:43
 **/
public class StreamTest {



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
    }
}

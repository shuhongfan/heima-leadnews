## lambda表达式

代码中的遇到的函数式接口（有且仅有一个抽象方法），可以用lambda来代替，比如MQ发消息，我们传统的做法：

```java
jmsTemplate.send(
    new MessageCreator() {    
    @Override
        public Message createMessage(Session session) throws JMSException { 
            return session.createTextMessage(String.valueOf(id));
        }
});
```

用lambda实现：

```java
jmsTemplate.send(session -> session.createTextMessage(String.valueOf(id)));
```

<font color="red">补充：</font> 

**Java内置四大核心函数式接口  —— 理论知识**

| **函数式接口**               | **参数类型** | **返回类型** | **用途**                                                     |
| ---------------------------- | ------------ | ------------ | ------------------------------------------------------------ |
| **Consumer<T>**消费型接口    | T            | void         | 对类型为T的对象应用操<br/>作，包含方法：<br/>void accept(T t) |
| **Supplier<T>**供给型接口    | 无           | T            | 返回类型为T的对象，包<br/>含方法：T get();                   |
| **Function<T, R>**函数型接口 | T            | R            | 对类型为T的对象应用操<br/>作，并返回结果。结果<br/>是R类型的对象。包含方 法：R apply(T t); |
| **Predicate<T>**断定型接口   | T            | boolean      | 确定类型为T的对象是否<br/>满足某约束，并返回<br/>boolean 值。包含方法<br/>boolean test(T t); |



## stream流

### 1、类型转换1

​	可以将一个类的对象集合中的某个属性生成新的集合。
比如一个学生类，学生包含学号、姓名、性别、手机号码、qq号。
场景：几个学生集合，将其中所有的学号抽取出来生成新的集合。原来的写法只有遍历这个学生集合，拿到他的学号属性添加到学号的集合中。代码如下：

```java
List<String>studentNos = new ArrayList<String>();
        for (Student student : studentList) { 
            studentNos.add(student.getStudentNo());
        }
```

这样要不断的循环，效率也会低很多。java 8中写法：

```java
List<String> studentNos = studentList.stream().map(Student::getStudentNo).collect(Collectors.toList());
```

### 2、类型转换2 

将List转换为Map。数组的查找效率是O(n)。效率还是比较低的，我们可以把他转化为Map。在知道key的情况下，访问速度就变成了O(1)。速度提升了很多。
场景：学生的集合已经获取到，他的学号是主键，我现在想要找出学号为0001的学生的名字。java 8以前的写法：

```java
for (Student student : studentList) {
    if ("0001".equals(student.getStudentNo())){
        System.out.println(student.getName());
    }
}
```

java 8的写法

```java
Map<String, String> collect = studentList.stream().collect(Collectors.toMap(Student::getStudentNo, Student::getName));
System.out.println(collect.get("0001"));
```

这是取两个属性字段，当然也可以用对象本省作为value，提供了一个属性方法：`Function.identity()`。可以获取对象本身。

### 3、过滤

从一个数组中筛选出来一些数据。
场景：找出学生列表中所有性别为男的学生
java 8以前的写法：

```java
List<Student>manList = new ArrayList<Student>();
for (Student student : studentList) {
    if (student.getSex().equals(1)){
        manList.add(student);
    }
}
```

java 8以后的写法有个filter方法

```java
List<Student> collect1 = studentList.stream().filter(s -> s.getSex().equals(1)).collect(Collectors.toList());
```

在filter方法中返回的是boolean类型。这种方式速度也会快很多。

### 4、排序

按照类里面的某个属性进行排序
场景：按照学生的年龄进行排序倒序排序

stream写法：

```
studentList.stream().sorted(Comparator.comparing(Student::getAge).reversed()).collect(Collectors.toList());
```

<font color="red">补充：</font>默认的自然排序最好也讲解一下 

### 5、去重

```
List<Student> collect2 = studentList.stream().distinct().collect(Collectors.toList());
```

也可以根据某一属性去重

```java
studentList.stream()
           .collect(Collectors.collectingAndThen (Collectors.toCollection(() 
-> new TreeSet<>(Comparator.comparingLong(Student::getName))), ArrayList::new));
```

也可以求和、求最大值、求最小值。和去重类似

### 6、快速找到集合中想要匹配的数据

```java
//是否存在学号为0001的人
boolean match = studentList.stream().anyMatch(detail -> "0001".equals(detail.getStudentNo()));
```

### 7、跳过或者获取

```java
//跳过前面n个元素
List<Student> skipList = studentList.stream().skip(2).collect(Collectors.toList());
//取前n个元素
List<Student> limitList = studentList.stream().limit(2).collect(Collectors.toList());
```



### <font color="red">8、补充 Collectors</font>  

Collector 接口中方法的实现决定了如何对流执行收集操作(如收集到 List、Set、Map)。但是 Collectors 实用类提供了很多静态方法，可以方便地创建常见收集器实例

*  **list.stream().collect(Collectors.toList());**
  * 把流中元素收集到List
*  **list.stream().collect(Collectors.toSet());**
  * 把流中元素收集到Set
* **list.stream().collect(Collectors.toCollection(ArrayList::new));**
  * 把流中元素收集到创建的集合
*  **list.stream().collect(Collectors.counting());**
  * 计算流中元素的个数
* **list.stream().collect(Collectors.summingInt(User::getAge));**
  * 对流中元素的整数属性求和
*  **list.stream().collect(Collectors.averagingInt(User::getAge));**
  * 计算流中元素Integer属性的平均值
*  **list.stream().collect(Collectors.summarizingInt(User::getAge));**
  * 收集流中Integer属性的统计值。如：平均值
* **list.stream().collect(Collectors.groupingBy(User::getAge));**
  * 根据某属性值对流分组，属性为K，结果为V

* 最大值maxBy和最小值 minBy

## Optional对象

java8 中的Optional是为了减少空指针异常的。

```java
BigDecimal bigAbmout = Optional.ofNullable(paymentChange.getPaymentChangeMoney()).orElseGet(() -> BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
```

这段代码的意思是当ofNullable的值为null是，可以个bigAmount一个默认值为0而不是Null防止后续代码使用bigAmount的时候报空指针异常。
还有一个orElse方法，他与orElseGet的区别是，orElseGet传的是一个lambda表达式函数式接口。使用orElse可以写成如下

```java
BigDecimal bigAbmout = Optional.ofNullable(paymentChange.getPaymentChangeMoney()).orElse(BigDecimal.ZERO);
```
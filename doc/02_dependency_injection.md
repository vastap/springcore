# <a name="Home"></a> Dependency Injection

## Содержание:
- [Обзор](#Overview)
- [Dependency Injection в Spring](#usage)
- [CDI](#CDI)

## [↑](#Home) <a name="Overview"></a> Обзор
Если посмотреть на главную страницу [Spring Framework](https://projects.spring.io/spring-framework/), то в списке предоставляемых возможностей указано: **Dependency Injection**, а если мы перейдём в [Reference Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/), то увидим, что раздел **"Core"** описывает **The IoC container**.

Для начала, можно прочитать [статью на хабре](https://habrahabr.ru/post/321344/).
Очень кратко если, то напрямую управляя созданием объектов и их связыванием мы лишаем свой код гибкости. Современный мир обязывает нас быть отзывчивыми на любое изменение требований. И тут на помощь приходит принцип: **Inversion of Control** (его ещё иногда называются **Принцип Голливуда**).
Данный принцип говорит, что хорошо бы не самим это делать, а делегировать управление объектов кому-то. И этого кого-то обычно именуют **IoC Container**. Потому что это некий контейнер, внутри которого установлены правила по которым всё работает, живут созданные объекты и т.д.
Реализуется этот принцип при помощи **Dependency Injection**,(внедрение зависимостей). Мы не хотим говорить, как создавать объекты и какие именно использовать реализации. Мы хотим работать на уровне интерфейсов и говорить, где нам нужны они. А непосредственно объекты нам предоставит IoC Container, т.к. ему мы делегируем всю "чёрную работу".
О том, что из себя представляет **Spring IoC Container** можно прочитать в разделе документации: "[1.2. Container overview](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-basics)".

Объекты, управляемые контейнером называются бинами. Про них можно прочитать в разделе документации: "[1.3. Bean overview](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-definition)".

## [↑](#Home) <a name="usage"></a> Dependency Injection в Spring
Для начала, необходимо изменить наш код в соответствии с уроком: "[Внедрение зависимостей - 2 - The Basics of Spring Framework](https://youtu.be/IBNQ6whVHeI?t=2m30s)".
Главное изменение - создание интерфейса для EventLogger'а:
```java
public interface EventLogger {

    void logEvent(String msg);
}
```
А так же в классе **App** убираем код создания клиента и логгера, добавляем конструктор:
```java
public App(Client client, EventLogger eventLogger) {
	this.client = client;
	this.eventLogger = eventLogger;
}
```
После этого необходимо описать бины, т.е. те классы, которые станут управляться за нас контейнером.
Конфигурацию можно скопировать из документации Spring: "[1.2.1. Configuration metadata](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-metadata)".
Главное условие - файл с конфигурацией должен быть на CLASS_PATH приложения. Например, **src/main/resources**, что является стандартным способом.
Вставим в xml файл, который мы скопировали из документации, описание наших бинов. Например:
```xml
<bean id="client" class="com.github.vastap.core.beans.Client"/>
<bean id="eventLogger" class="com.github.vastap.core.loggers.ConsoleEventLogger"/>
<bean id="app" class="com.github.vastap.core.App"/>
```

Теперь их нужно изменить так, как указано в уроке "[Именование бинов и старт контекста - 3 - The Basics of Spring Framework](https://youtu.be/y-obHCFTbZ4?t=3m30s)".
Изменяем описание бина для класса Client:
```xml
<bean id="client" class="com.github.vastap.core.beans.Client">
	<constructor-arg value="1" />
	<constructor-arg value="John Smith"/>
</bean>
```
И изменяем описание бина для класса App:
```xml
<bean id="app" class="com.github.vastap.core.App">
	<constructor-arg ref="client"/>
	<constructor-arg ref="eventLogger"/>
</bean>
```
И изменяем наш метод main в классе App:
```java
ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
App app = (App) context.getBean("app");

app.logEvent("Hello World from user 1!");
```

Запускаем и убеждаемся, что всё работает как и раньше. Вот мы и использовали Dependency Injection благодаря Spring Framework.

## [↑](#Home) <a name="CDI"></a> CDI
Помимо **Dependency Injection** можно встретить такое определение, как **"Contexts and Dependency Injection"**. Соответственно, аббривиатуры DI и CDI.
С одной стороны, это про одно и тоже, с другой стороны - это немного разное.
В общем случае, это спецификация, описывающая более typesafe подход, позволяющий избежать некоторых ошибок и обеспечивающий гибкость DI.

Одной из эталонных реализаций спецификации CDI считается **WELD**.
Документация Weld: [CDI Reference Implementation](http://docs.jboss.org/weld/reference/latest/en-US/html_single/)
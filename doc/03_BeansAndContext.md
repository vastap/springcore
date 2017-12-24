# <a name="Home"></a> Bean Naming and Context Startup

## Содержание:
- [Обзор](#Overview)
- [Bean Naming](#BeanNaming)
- [Context Startup](#ContextStartup)
- [Resources](#Resources)

## [↑](#Home) <a name="Overview"></a> Обзор
Одной из самых главных вещей для понимания в Spring является Spring Context.
Как мы помним, Spring нам обеспечивает Dependency Injection и представляет в этом случае IoC Container.
В документации этому посвещён раздел: "[1.2. Container overview](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-basics)".
Там сказано, что:
```
The interface org.springframework.context.ApplicationContext represents the Spring IoC container and is responsible for instantiating, configuring, and assembling the aforementioned beans.
```
Контекст описывается метданными, которые мы должны предоставить контейнеру в каком либо виде. Существует несколько способов указания конфигурации:
- XML Configuration
- Annotation-based configuration (Spring 2.5+)
- Java-based configuration (Spring 3.0+)

ApplicationContext является расширением BeanFactory.
В документации Spring в разделе [1.16.1. BeanFactory or ApplicationContext?](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction-ctx-vs-beanfactory) приведены их различия.
Но главное что стоит запомнить, это первая строка этого раздела:
```
Use an ApplicationContext unless you have a good reason for not doing so.
```

## [↑](#Home) <a name="BeanNaming"></a> Bean Naming
В уроке "[Именование бинов и старт контекста - 3 - The Basics of Spring Framework](https://www.youtube.com/watch?v=y-obHCFTbZ4)" рассказывается про именование бинов.
Так же про описание бинов в XML конфигурации можно прочитать в официальной документации Spring, в разделе: "[1.4. Dependencies](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-dependencies)".

## [↑](#Home) <a name="ContextStartup"></a> Context Startup
Для начала, необходимо инстанциировать IoC Container, он же Application Context.
Это описано в документации Spring, в разделе: "[1.2.2. Instantiating a container](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-instantiation)".

Пример использования указан там же: "[1.2.3. Using the container](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-client)".
В нашем примере это выразилось так:
```java
public static void main(String[] args) {
        ApplicationContext context;
        context = new ClassPathXmlApplicationContext("spring.xml");
        App app = (App) context.getBean("app");

        app.logEvent("Hello World from user 1!");
}
```
Мы объявляем ApplicationContext в переменную context.
Создаём контекст по XML конфигурации.
Позже, получаем из этого контекста бин по имени **app**.

Процесс инициализации контекста изложен в статье:
[Spring изнутри. Этапы инициализации контекста](https://habrahabr.ru/post/222579/)

## [↑](#Home) <a name="Resources"></a> Resources
Дополнительные материалы:
- [Урок 2: Введение в Spring IoC контейнер](http://spring-projects.ru/guides/lessons/lesson-2/)
- [Евгений Борисов — Spring-потрошитель, часть 1](https://www.youtube.com/watch?v=BmBr5diz8WA&t=1247s)
- [Евгений Борисов — Spring-потрошитель, часть 2](https://www.youtube.com/watch?v=cou_qomYLNU)
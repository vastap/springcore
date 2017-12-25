# <a name="Home"></a> Scopes And Inner Beans

## Содержание:
- [Обзор](#Overview)
- [Scopes](#Scopes)
- [InnerBeans](#InnerBeans)
- [Реализация](#Implementation)

## [↑](#Home) <a name="Overview"></a> Обзор
В уроке [Scopes and Inner Beans - 4 - The Basics of Spring Framework](https://www.youtube.com/watch?v=kvcFK7criFc&pbjreload=10) рассматриваются следующие темы:
- Scopes
- Inner Beans

## [↑](#Home) <a name="Scopes"></a> Scopes
**Scopes** - это область видимости бинов.
Данная тема описана в разделе документации: "[1.5. Bean scopes](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes)".

По умолчанию каждый бин создаётся со **scope** = "[singleton](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-singleton)".
Интересно, что можно даже создавать свои scope, о чём написано в разделе документации: "[1.5.5. Custom scopes](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-custom)".
Стоит учитывать, что все Singleton бины создаются в момент инициализации контекста, о чём рассказывается в главе "[1.4.4. Lazy-initialized beans](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lazy-init)". Это можно изменить, указав у bean такой аттрибут, как ```lazy-init="true"```.

Чтобы при каждом обращении создавался новый экземпляр существует **scope** = "[prototype](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-prototype)".

Следует понимать, что Spring - всего лишь фрэймворк, он не меняет сути механизмов Java. Поэтому, scope влияет именно на инициализацию. Поэтому, scope отвечает лишь за то, что если куда-то понадобится выполнить inject, то в этот момент будет учитываться scope. А на сам вызов метода не влияет. Даже если будет lazy инициализация, это произойдёт лишь один раз, пока поле не инициализировано. О чём и говориться в "[1.5.3. Singleton beans with prototype-bean dependencies](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-sing-prot-interaction)".

## [↑](#Home) <a name="InnerBeans"></a> Inner Beans
Помимо обычных бинов существует такое понятие, как внутренний бин или Inner Beans.
Про них написано в разделе документации Spring: "[Inner beans](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-inner-beans)".
Внутренние бины отличаются от обычных. Во-первых, они анонимны, для них не действуют аттрибуты id и name. Кроме того, т.к. они являются частью внешнего бина, то и аттрибут scope на них не действует. Кроме того, они являются частью внешнего бина и поэтому просто являются его частью и доступ к ним вне внешнего бина невозможен. Вот такие вот особенности.
Про пример использования Inner Beans можно так же прочитать в старенькой книжке [Spring Recipes: A Problem-Solution Approach](https://goo.gl/TVc7kv). Основное назначение: если бин используется только для обеспечения потребностей конкретного бина - его можно объявить внутренним. Это сократит описание и сделает его назначение более понятным.

## [↑](#Home) <a name="Implementation"></a> Реализация
Для начала необходимо добавить класс Event, представляющий собой логируемое событие:
```java
public class Event {
    private int id;
    private String msg;
    private Date date;
    private DateFormat dateFormat;

    public Event(Date date, DateFormat dateFormat) {
        this.id = new Random().nextInt();
        this.date = date;
        this.dateFormat = dateFormat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", date=" + dateFormat.format(date) +
                '}';
    }
}
```
Далее необходимо изменить наш интерфейс Event Logger'а:
```java
public interface EventLogger {

    void logEvent(Event event);
}
```
Добавить в App поле:
```java
private Event event;
```
Добавить это поле в конструктор, а так же изменить непосредственно сам метод логирования:
```java
public void logEvent(String msg) {
	String message = msg.replaceAll(client.getId(), client.getFullName());
	event.setMsg(message);
	eventLogger.logEvent(event);
}
```
После чего отредактируем xml конфигурацию:
Опишем бин для DateFormat:
```xml
<bean id="dateFormat" class="java.text.DateFormat"
          factory-method="getDateTimeInstance"/>
```
Как мы видим, здесь используется factory-method, который описан в разделе документации "[Instantiation using an instance factory method](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-class-instance-factory-method)".

Опишем наш бин для Event:
```xml
<bean id="event" class="com.github.vastap.core.beans.Event" scope="prototype">
	<constructor-arg>
		<bean class="java.util.Date"/>
	</constructor-arg>
	<constructor-arg ref="dateFormat"/>
</bean>
```
И добавим в описание бина App новый аргумент:
```xml
<bean id="app" class="com.github.vastap.core.App">
	<constructor-arg ref="client"/>
	<constructor-arg ref="event"/>
	<constructor-arg ref="eventLogger"/>
</bean>
```
# <a name="Home"></a> Initialize and Destroy

## Содержание:
- [Обзор](#Overview)
- [Реализация](#Implementation)

## [↑](#Home) <a name="Overview"></a> Обзор
В уроке [Initialize & Destroy - 5 - The Basics of Spring Framework](https://www.youtube.com/watch?v=ygaboLsCKA4) рассматриваются важные события из жизненного цикла бинов: инициализация и уничтожение.

Эти две фазы являются одними из самых важных в жизненном цикле бинов.
Фаза инициализации помогает выполнить различные проверки, выполнение которых в конструкторе нежелательно (и концептуально и потому, что не все бины ещё могут быть инициализированы).
Фаза уничтожения помогает выполнить различные действия, такие как сохранение данных из кэша, закрытие ресурсов, логирование и т.п.

Существует несколько способов, при помощи которых этого можно достигнуть.
Подробнее можно прочитать тут: "[Spring Bean Life Cycle](https://www.journaldev.com/2637/spring-bean-life-cycle)".

В документации Spring этому отведён раздел: "[1.6.1. Lifecycle callbacks](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lifecycle)".

Можно указать init и destroy методы:
- При помощи аннотаций (см. [Урок 2: Введение в Spring IoC контейнер](http://spring-projects.ru/guides/lessons/lesson-2/))
- При помощи имплементации интерфейсов (см. [Initialization callbacks](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean))
- При помощи аттрибутов в XML (см. в том же разделе документации Spring)

## [↑](#Home) <a name="Implementation"></a> Реализация
Реализуем два наследника Event Logger'а, без кэширования и с кэшированием:
```java
public class FileEventLogger implements EventLogger {
    private String fileName;

    public FileEventLogger(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void logEvent(Event event) {
        try {
            File file = new File(fileName);
            FileUtils.writeStringToFile(file, event.toString()+"\n",
            							Charset.forName("UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        File file = new File(fileName);
        if (!file.canWrite()) {
            throw new IllegalStateException("Can't write to file " + fileName);
        }
    }

}
```
А так же логгер с кэшированием:
```java
public class CacheFileEventLogger extends FileEventLogger {
    private int cacheSize;
    private List<Event> cache;

    public CacheFileEventLogger(int cacheSize, String fileName) {
        super(fileName);
        this.cacheSize = cacheSize;
        cache = new ArrayList<>(cacheSize);
    }

    @Override
    public void logEvent(Event event) {
        cache.add(event);
        if (cache.size() == cacheSize) {
            writeEventsFromCache();
            cache.clear();
        }
    }

    private void writeEventsFromCache() {
        for (Event event : cache) {
            super.logEvent(event);
        }
    }

    private void destroy() {
        if (!cache.isEmpty()) {
            writeEventsFromCache();
        }
    }
}
```
Описание в xml файле будет выглядеть следующим образом:
```xml
<bean id="consoleEventLogger"
class="com.github.vastap.core.loggers.ConsoleEventLogger"/>

<bean id="fileEventLogger"
class="com.github.vastap.core.loggers.FileEventLogger"
init-method="init">
	<constructor-arg value="C:/Users/veselroger/log.txt"/>
</bean>

<bean id="cacheFileEventLogger"
class="com.github.vastap.core.loggers.CacheFileEventLogger"
destroy-method="destroy">
	<constructor-arg value="2"/>
	<constructor-arg value="C:/Users/veselroger/log.txt"/>
</bean>
```
Есть тут интересная особенность, которая может быть не очевидна.
init метод от FileEventLogger'а наследуется для CacheFIleEventLogger.
А вот значения аргументов - нет.
Указываем реализацию EventLogger'а для App. Ну и поменяем main класс нашего App:
```java
public static void main(String[] args) {
	ConfigurableApplicationContext context;
	context = new ClassPathXmlApplicationContext("spring.xml");
	App app = (App) context.getBean("app");
	app.logEvent("Hello World from user 1!");
	for (int i = 0; i < 5; i++) {
		app.logEvent("Hello World from user 1!");
	}
	// Instead of context.close() use the hook:
	context.registerShutdownHook();
}
```
Подробнее можно прочитать тут: "[Shutting down the Spring IoC container gracefully in non-web applications](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-shutdown)".

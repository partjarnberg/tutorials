# undertow-jersey-weld-shiro
When it comes to building REST API´s using Java nowadays I always turn to Spring and Spring Boot in particular.

But if I wanted to do the same using the reference implementations in Java for JAX-RS and CDI? 
How would I do this and without deploying to some heavy application server?  Well, this is the result. 

## Requirements
1. REST framework
2. HATEOAS support
3. Dependency injection
4. Basic Auth
5. Websockets
6. Embedded web server
7. Executable jar

## Technologies being used
- [Undertow](http://undertow.io/)
  + web server
  + embeddable
- [Jersey](https://jersey.java.net/)
  + JAX-RS (JSR 311 & JSR 339) Reference Implementation
  + HATEOAS extension
  + CDI 1.1 integration support
- [Weld](http://weld.cdi-spec.org/)
  + Reference implementation of CDI: Contexts and Dependency Injection for the Java EE Platform
- [Apache Shiro](http://shiro.apache.org/)
  + Apache Shiro is an application security framework 
- [maven-shade-plugin](https://maven.apache.org/plugins/maven-shade-plugin/)
  + Plugin with capability of creating an executable uber JAR
  
## Build and run

### Build
mvn clean package

### Run
java -jar target/undertow-jersey-weld-shiro-1.0-SNAPSHOT.jar -httpPort 8080

#### Basic Auth
User: admin

Password: prepareforglory

#### Example output
Goto http://localhost:8080/my-app/api/persons to list persons:

```javascript
{
   "links" : [ {
      "href" : "http://localhost:8080/my-app/api/persons?offset=0&limit=50",
      "rel" : "self"
   } ],
   "persons" : [ {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/persons/f868fc51-41f3-4ec7-9eae-9fbe99fdab2b",
         "rel" : "self"
      } ],
      "firstName" : "Kirk",
      "lastName" : "Mason"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/persons/bcdd0d61-353e-4470-91dc-0acc504bc7b5",
         "rel" : "self"
      } ],
      "firstName" : "William",
      "lastName" : "Dickerson"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/persons/0417e857-0cdf-43ab-8140-628f56f243b7",
         "rel" : "self"
      } ],
      "firstName" : "Earlene",
      "lastName" : "Walker"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/persons/fdfabab9-f5f9-4a69-9e13-604e3e0ab26b",
         "rel" : "self"
      } ],
      "firstName" : "Kenneth",
      "lastName" : "Finnegan"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/persons/721d35be-4e87-430e-82fe-6cecd83cd547",
         "rel" : "self"
      } ],
      "firstName" : "John",
      "lastName" : "Doe"
   } ]
}
```

Follow link http://localhost:8080/my-app/api/persons/f868fc51-41f3-4ec7-9eae-9fbe99fdab2b to see more info on that person:

```javascript
{
   "links" : [ {
      "href" : "http://localhost:8080/my-app/api/persons/f868fc51-41f3-4ec7-9eae-9fbe99fdab2b",
      "rel" : "self"
   }, {
      "href" : "http://localhost:8080/my-app/api/persons?offset=0&limit=50",
      "rel" : "list"
   }, {
      "href" : "http://localhost:8080/my-app/api/persons/f868fc51-41f3-4ec7-9eae-9fbe99fdab2b/jobs",
      "rel" : "jobs"
   } ],
   "email" : "kirk.mason@mail.com",
   "firstName" : "Kirk",
   "lastName" : "Mason"
}
```

Follow http://localhost:8080/my-app/api/persons/f868fc51-41f3-4ec7-9eae-9fbe99fdab2b/jobs to list jobs of that person:

```javascript
{
   "links" : [ {
      "href" : "http://localhost:8080/my-app/api/persons/f868fc51-41f3-4ec7-9eae-9fbe99fdab2b/jobs",
      "rel" : "self"
   } ],
   "jobs" : [ {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/employers/6c5f098c-1071-4728-a15c-56fade963773",
         "rel" : "employer"
      } ],
      "employerName" : "Hipsters",
      "profession" : "Youtube Star"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/employers/41c19f6a-7f59-4c3b-b308-0e123c7c29ec",
         "rel" : "employer"
      } ],
      "employerName" : "Fancy Company",
      "profession" : "Musician"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/employers/7a7875da-1079-4e64-8483-5c193fdd167e",
         "rel" : "employer"
      } ],
      "employerName" : "Shelf Company",
      "profession" : "Construction Worker"
   }, {
      "links" : [ {
         "href" : "http://localhost:8080/my-app/api/employers/2e32f8ad-2aee-4c16-a97e-9c10d2f353a3",
         "rel" : "employer"
      } ],
      "employerName" : "Software Company",
      "profession" : "Engineer"
   } ]
}
```

Follow http://localhost:8080/my-app/api/employers/6c5f098c-1071-4728-a15c-56fade963773 to see more information on that employer:

```javascript
{
   "links" : [ {
      "href" : "http://localhost:8080/my-app/api/employers/6c5f098c-1071-4728-a15c-56fade963773",
      "rel" : "self"
   } ],
   "email" : "contact@hipsters.com",
   "name" : "Hipsters"
}
```

## Key Elements

### Configure Undertow web server
The class com.skillsdevelopment.app.Application contains the configuration and startup of the embedded Undertow web server.

#### Configure deployment of Jersey servlet
```java
Servlets.deployment()
    .setClassLoader(Application.class.getClassLoader())
    .setResourceManager(new ClassPathResourceManager(Application.class.getClassLoader()))
    .setContextPath("/my-app")
    .setDeploymentName("my-app.war")
    .addServlets(
        servlet("jerseyServlet", org.glassfish.jersey.servlet.ServletContainer.class)
            .setLoadOnStartup(1)
            .addInitParam(JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName())
            .addMapping("/api/*"))
```

#### Connect Weld to servlet deployment configuration
```java
Servlets.deployment()
    ...
    ...
    .addListener(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class))
    ...
    ...
```

#### Connect Apache Shiro to servlet deployment configuration and activate its filter
```java
Servlets.deployment()
    ...
    ...
    .addListener(Servlets.listener(org.apache.shiro.web.env.EnvironmentLoaderListener.class))
    .addFilter(Servlets.filter("shiroFilter", org.apache.shiro.web.servlet.ShiroFilter.class))
    .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.REQUEST)
    .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.FORWARD)
    .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.INCLUDE)
    .addFilterUrlMapping("shiroFilter", "/*", DispatcherType.ERROR)
    ...
    ...
```

#### Start server
```java
final Undertow server = Undertow.builder().addHttpListener(httpPort, "0.0.0.0")
                        .setHandler(pathHandler).build();
server.start();
```

### Eagerly instantiate @ApplicationScoped beans
By default Weld instantiate beans when they are requested. This is a problem if we, as in this case, 
want a service which all it does is listening for event on an event bus.

To solve this issue you must implement a 'javax.enterprise.inject.spi.Extension' service extension.

```java
package com.skillsdevelopment.app.config;

import com.skillsdevelopment.app.config.annotation.Eager;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import java.util.ArrayList;
import java.util.List;

public class EagerExtension implements Extension {
    private List<Bean<?>> eagerBeansList = new ArrayList<>();

    public <T> void collect(@Observes final ProcessBean<T> event) {
        if (event.getAnnotated().isAnnotationPresent(Eager.class)
                && event.getAnnotated().isAnnotationPresent(ApplicationScoped.class)) {
            eagerBeansList.add(event.getBean());
        }
    }

    public void load(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
        eagerBeansList.forEach(bean -> {
            // note: toString() is important to instantiate the bean
            beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)).toString();
        });
    }
}
```

Coupled with an extra annotation.

```java
package com.skillsdevelopment.app.config.annotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface Eager {
}
```

And finally register the extension via a file named META-INF/services/javax.enterprise.inject.spi.Extension containing one single line:
```
com.skillsdevelopment.app.config.EagerExtension
```
More info regarding META-INF/services can be found at [ServiceLoader (Java Platform SE 8)](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
and how to create extensions [Extension (Java(TM) EE 7 Specification APIs)](http://docs.oracle.com/javaee/7/api/javax/enterprise/inject/spi/Extension.html)

After above is done we can use our new annotation @Eager for @ApplicationScoped beans and be certain that they will be instantiated at startup.

```java
package com.skillsdevelopment.app.server;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.skillsdevelopment.app.config.annotation.Eager;
import com.skillsdevelopment.app.server.event.PersonRetrievedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ApplicationScoped @Eager
public class EventListener implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Inject
    private EventBus eventBus;

    @PostConstruct
    private void init() {
        LOGGER.debug("Register to event bus {}:{} ", eventBus, eventBus.hashCode());
        eventBus.register(this);
    }

    @Subscribe
    public void handlePersonRetrievedEvent(final PersonRetrievedEvent event) {
        LOGGER.info("Full name of person being requested: {}", event.fullName());
    }
}
```
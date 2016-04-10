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

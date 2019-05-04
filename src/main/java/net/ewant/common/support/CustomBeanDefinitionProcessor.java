package net.ewant.common.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * Created by huangzh on 2018/10/26.
 */
public class CustomBeanDefinitionProcessor implements BeanDefinitionRegistryPostProcessor{

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        FullNameAnnotationBeanNameGenerator fullNameAnnotationBeanNameGenerator = new FullNameAnnotationBeanNameGenerator();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
        scanner.setBeanNameGenerator(fullNameAnnotationBeanNameGenerator);
        scanner.scan("net.ewant.common");
        if(beanDefinitionRegistry instanceof SingletonBeanRegistry){
            ((SingletonBeanRegistry) beanDefinitionRegistry).registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, fullNameAnnotationBeanNameGenerator);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}

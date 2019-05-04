package net.ewant.common.support;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by huangzh on 2018/10/26.
 */
public class CommonApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        CustomBeanDefinitionProcessor customBeanDefinitionProcessor = new CustomBeanDefinitionProcessor();
        configurableApplicationContext.addBeanFactoryPostProcessor(customBeanDefinitionProcessor);
    }
}

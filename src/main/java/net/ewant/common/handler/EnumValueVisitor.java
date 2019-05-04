package net.ewant.common.handler;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * 用途1：mybatis 枚举类型入库时获取枚举值以及查询时转化为java枚举对象
 * 用途2：加上@JsonValue 注解可以在spring mvc将响应对象序列化为json时（使用Jackson），遇到enum类型的属性，则调用getValue输出枚举值
 *        反序列化为对象时，需要使用ObjectMapper.readValue，并配置JacksonEnumDeserializer，如下：
 *        SimpleModule sm = new SimpleModule();
 *        sm.addDeserializer(EnumValueVisitor.class, new JacksonEnumDeserializer());
 *        MAPPER.registerModule(sm);
 * @see MybatisEnumTypeHandler
 * Created by admin on 2018/11/9.
 */
@JsonDeserialize(using = JacksonEnumDeserializer.class)
public interface EnumValueVisitor<Value extends Serializable> {
    @JsonValue
    Value getValue();
}

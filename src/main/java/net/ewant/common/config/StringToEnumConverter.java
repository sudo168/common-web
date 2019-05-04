package net.ewant.common.config;

import net.ewant.util.EnumParseUtils;
import org.springframework.core.convert.converter.Converter;

import java.io.Serializable;

/**
 * spring mvc 枚举参数注入实现类
 * @see WebMvcConfiguration
 * Created by admin on 2018/11/9.
 */
public class StringToEnumConverter<E extends Enum> implements Converter<Serializable, Enum> {

    private final Class<E> enumType;

    public StringToEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public Enum convert(Serializable source) {
        return EnumParseUtils.get(String.valueOf(source), enumType);
    }


}

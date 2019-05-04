package net.ewant.common.handler;

import net.ewant.util.EnumParseUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * Created by admin on 2018/12/14.
 */
public class JacksonEnumDeserializer extends JsonDeserializer<EnumValueVisitor> {

    public JacksonEnumDeserializer(){
    }

    @SuppressWarnings("rawtypes")
    @Override
    public EnumValueVisitor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String currentName = jsonParser.getCurrentName();
        Object currentValue = jsonParser.getCurrentValue();// target Enum
        Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        return EnumParseUtils.get(node.asText(), findPropertyType);
    }
}

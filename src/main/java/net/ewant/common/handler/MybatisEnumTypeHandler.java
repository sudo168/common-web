package net.ewant.common.handler;

import net.ewant.util.EnumParseUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库与java对象字段枚举类型转换
 * 需要在配置文件中配置解析器包路径：mybatis.type-handlers-package
 * 子类加上@MappedTypes注解后，会将具体类型传入构造函数
 * Created by huangzh on 2018/10/31.
 */
public class MybatisEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private Class<E> type;

    public MybatisEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        boolean isVisitor = parameter instanceof EnumValueVisitor;
        if (jdbcType == null) {
            ps.setString(i, isVisitor ? String.valueOf(((EnumValueVisitor)parameter).getValue()): parameter.toString());
        } else {
            ps.setObject(i, isVisitor ? ((EnumValueVisitor)parameter).getValue() : parameter.name(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return get(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return get(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return get(cs.getString(columnIndex));
    }

    private E get(String v) {
        return EnumParseUtils.get(v, type);
    }

}

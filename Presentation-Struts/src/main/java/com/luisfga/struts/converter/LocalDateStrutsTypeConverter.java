package com.luisfga.struts.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;
import com.opensymphony.xwork2.conversion.TypeConversionException;

public class LocalDateStrutsTypeConverter extends StrutsTypeConverter {
    
    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if ((values != null) && (values.length > 0) && (!values[0].isEmpty())) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(values[0]);
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
            } catch (Exception e) {
                throw new TypeConversionException("Unable to convert into a LocalDate: " + values[0], e);
            }
        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        try {
            if (o != null) {
                return ((LocalDate) o).toString();
            }
            return null;
        } catch (Exception e) {
            throw new TypeConversionException("Unable to convert into a String: " + o, e);
        }
    }
    
 }
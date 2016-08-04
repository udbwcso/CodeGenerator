package com.template.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.*;

/**
 * Velocity template util
 */
public class TemplateUtil {
    private static final String DEFAULT_ENCODING = "UTF-8";

    static {
        Properties prop = new Properties();
        prop.put("resource.loader", "class");
        prop.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        prop.put(Velocity.INPUT_ENCODING, DEFAULT_ENCODING);
        prop.put(Velocity.OUTPUT_ENCODING, DEFAULT_ENCODING);
        Velocity.init(prop);
    }

    public static String getContent(Map<String, Object> data, String templateFile) {
        //make a Context and put data into it
        VelocityContext context = new VelocityContext();
        if (data != null && !data.isEmpty()) {
            Iterator<String> it = data.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object value = data.get(key);
                context.put(key, value);
            }
        }
        //get the Template
        Template template = Velocity.getTemplate(templateFile, DEFAULT_ENCODING);
        //render the template into a Writer
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}

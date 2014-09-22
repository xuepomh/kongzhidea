package com.hoo.veiw.xml;
 
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;
 
/**
 * <b>function:</b>扩展AbstractView 实现JSON-lib视图
 * @author hoojo
 * @createDate 2011-4-28 下午05:26:43
 * @file MappingJsonlibVeiw.java
 * @package com.hoo.veiw.xml
 * @project SpringMVC4View
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MappingJsonlibVeiw extends AbstractView {
    
    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    
    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";
 
    private String encodeing = DEFAULT_CHAR_ENCODING;
 
    public void setEncodeing(String encodeing) {
        this.encodeing = encodeing;
    }
 
    private Set<String> renderedAttributes;
    
    private JsonConfig cfg = null;
    
    public void setCfg(JsonConfig cfg) {
        this.cfg = cfg;
    }
 
    public MappingJsonlibVeiw() {
        setContentType(DEFAULT_CONTENT_TYPE);
    }
    
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        model = filterModel(model);
        response.setCharacterEncoding(encodeing);
        
        PrintWriter out = response.getWriter();
        if (cfg == null) {
            out.print(JSONSerializer.toJSON(model));
        } else {
            out.print(JSONSerializer.toJSON(model, cfg));
        }
    }
 
    /**
     * Filters out undesired attributes from the given model.
     * <p>Default implementation removes {@link BindingResult} instances and entries not included in the {@link
     * #setRenderedAttributes(Set) renderedAttributes} property.
     */
    protected Map<String, Object> filterModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<String, Object>(model.size());
        Set<String> renderedAttributes =
                !CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes : model.keySet();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
package com.gsg.blogbackend.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 重写 HttpServletRequestWrapper
 * 处理表单、ajax请求
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    // 用于存储请求参数
    private Map<String , String[]> params = new HashMap<String, String[]>();

    private final  byte[] body;

    // 构造方法
    public MyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        // 把请求参数添加到我们自己的map当中
        this.params.putAll(request.getParameterMap());
        body = readBytes(request.getReader(), "utf-8");
    }

    /**
     * 通过BufferedReader和字符编码集转换成byte数组
     * @param br
     * @param encoding
     * @return
     * @throws IOException
     */
    private byte[] readBytes(BufferedReader br, String encoding) throws IOException {
        String str = null;
        StringBuilder sb=new StringBuilder();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString().getBytes(Charset.forName(encoding));
    }


    // 构造方法
    public MyHttpServletRequestWrapper(HttpServletRequest request, byte[] body) throws IOException {
        super(request);
        this.body = body;
    }

    //重写HttpServletRequestWrapper的getInputStream,实现流的可读取
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


    //重写HttpServletRequestWrapper的getReader,实现流的可读取
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    //得到请求体
    public String getBody() {
        return new String(body);
    }

    @Override
    public int getContentLength(){
        return body.length;
    }

    @Override
    public long getContentLengthLong(){
        return body.length;
    }

    /**
     * 添加参数到map中
     * @param extraParams
     */
    public void setParameterMap(Map<String, Object> extraParams) {
        for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加参数到map中
     * @param name
     * @param value
     */
    public void setParameter(String name, Object value) {
        if (value != null) {
            System.out.println(value);
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }

    /**
     * 重写getParameter，代表参数从当前类中的map获取
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    /**
     * 重写getParameterValues方法，从当前类的 map中取值
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

}


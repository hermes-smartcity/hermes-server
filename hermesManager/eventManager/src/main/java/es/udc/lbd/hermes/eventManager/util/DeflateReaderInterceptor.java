package es.udc.lbd.hermes.eventManager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.InflaterInputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

public class DeflateReaderInterceptor implements ReaderInterceptor {
    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

        MultivaluedMap<String,String> headers = context.getHeaders();
        List<String> contentEncoding = headers.get("Content-Encoding");

        if(contentEncoding!= null && contentEncoding.contains("deflate")) {
            final InputStream originalInputStream = context.getInputStream();
            context.setInputStream(new InflaterInputStream(originalInputStream));
        }
        return context.proceed();
    }
}
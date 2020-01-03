package htwb.ai.mundt.services;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBException;

@Provider
public class XMLMappingExceptionMapper implements ExceptionMapper<JAXBException> {
    @Override public Response toResponse(JAXBException exception) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
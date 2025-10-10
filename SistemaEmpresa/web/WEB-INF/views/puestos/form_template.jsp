<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Puesto" %>

<%
    Puesto puesto = (Puesto) request.getAttribute("puesto");
    boolean esEdicion = puesto != null;
    String titulo = esEdicion ? "Editar Puesto" : "Nuevo Puesto";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-briefcase");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/puestos/form_content.jsp" />
</jsp:include>

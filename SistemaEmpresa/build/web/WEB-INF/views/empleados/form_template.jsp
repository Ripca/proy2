<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Empleado" %>

<%
    Empleado empleado = (Empleado) request.getAttribute("empleado");
    boolean esEdicion = empleado != null;
    String titulo = esEdicion ? "Editar Empleado" : "Nuevo Empleado";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-user-edit");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/empleados/form_content.jsp" />
</jsp:include>

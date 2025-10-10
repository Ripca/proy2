<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Cliente" %>

<%
    Cliente cliente = (Cliente) request.getAttribute("cliente");
    boolean esEdicion = cliente != null;
    String titulo = esEdicion ? "Editar Cliente" : "Nuevo Cliente";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-user-edit");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/clientes/form_content.jsp" />
</jsp:include>

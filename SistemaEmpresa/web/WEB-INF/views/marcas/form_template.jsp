<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Marca" %>

<%
    Marca marca = (Marca) request.getAttribute("marca");
    boolean esEdicion = marca != null;
    String titulo = esEdicion ? "Editar Marca" : "Nueva Marca";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-tag");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/marcas/form_content.jsp" />
</jsp:include>

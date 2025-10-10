<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Compra" %>

<%
    Compra compra = (Compra) request.getAttribute("compra");
    boolean esEdicion = compra != null;
    String titulo = esEdicion ? "Editar Compra" : "Nueva Compra";
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-shopping-bag");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/compras/form_content.jsp" />
</jsp:include>

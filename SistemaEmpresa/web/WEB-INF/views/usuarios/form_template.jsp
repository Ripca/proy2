<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Usuario" %>
<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    boolean esEdicion = usuario != null;
    String titulo = esEdicion ? "Editar Usuario" : "Nuevo Usuario";
    
    request.setAttribute("pageTitle", titulo);
    request.setAttribute("pageIcon", "fas fa-user-cog");
%>
<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/usuarios/form_content.jsp" />
</jsp:include>

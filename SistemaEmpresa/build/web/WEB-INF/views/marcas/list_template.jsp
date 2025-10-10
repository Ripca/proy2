<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Marca" %>

<%
    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Marcas");
    request.setAttribute("pageIcon", "fas fa-tags");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/marcas/list_content.jsp" />
</jsp:include>

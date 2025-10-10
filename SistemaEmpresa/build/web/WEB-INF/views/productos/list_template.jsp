<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Producto" %>

<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Productos");
    request.setAttribute("pageIcon", "fas fa-box");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/productos/list_content.jsp" />
</jsp:include>

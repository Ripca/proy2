<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Compra" %>

<%
    List<Compra> compras = (List<Compra>) request.getAttribute("compras");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Compras");
    request.setAttribute("pageIcon", "fas fa-shopping-bag");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/compras/list_content.jsp" />
</jsp:include>

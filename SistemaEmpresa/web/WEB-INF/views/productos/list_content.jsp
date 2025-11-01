<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Producto" %>

<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Productos</h2>
        <p class="text-muted mb-0">Administra el catálogo de productos</p>
    </div>
    <a href="ProductoServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nuevo Producto
    </a>
</div>



<!-- Tabla de productos -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Productos
            <span class="badge bg-primary ms-2">
                <%= productos != null ? productos.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (productos != null && !productos.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>Imagen</th>
                        <th>ID</th>
                        <th>Producto</th>
                        <th>Marca</th>
                        <th>Descripción</th>
                        <th>Precio Costo</th>
                        <th>Precio Venta</th>
                        <th>Existencia</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (productos != null && !productos.isEmpty()) {
                            for (Producto producto : productos) {
                    %>
                    <tr>
                        <td>
                            <% if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                                String rutaImagen = producto.getImagen();
                                // Convertir "web/assets/..." a "/SistemaEmpresa/assets/..."
                                if (rutaImagen.startsWith("web/")) {
                                    rutaImagen = request.getContextPath() + "/" + rutaImagen.substring(4);
                                }
                                // Agregar timestamp para evitar caché del navegador
                                rutaImagen = rutaImagen + "?t=" + System.currentTimeMillis();
                            %>
                                <img src="<%= rutaImagen %>" alt="<%= producto.getProducto() %>"
                                     style="max-width: 50px; max-height: 50px; border-radius: 3px;">
                            <% } else { %>
                                <span class="text-muted">Sin imagen</span>
                            <% } %>
                        </td>
                        <td><%= producto.getIdProducto() %></td>
                        <td>
                            <strong><%= producto.getProducto() %></strong>
                        </td>
                        <td><%= producto.getNombreMarca() != null ? producto.getNombreMarca() : "Sin marca" %></td>
                        <td><%= producto.getDescripcion() != null ? producto.getDescripcion() : "N/A" %></td>
                        <td>Q. <%= String.format("%.2f", producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : 0.0) %></td>
                        <td>Q. <%= String.format("%.2f", producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : 0.0) %></td>
                        <td>
                            <span class="badge <%= producto.getExistencia() > 0 ? "bg-success" : "bg-danger" %>">
                                <%= producto.getExistencia() %>
                            </span>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="ProductoServlet?action=edit&id=<%= producto.getIdProducto() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('ProductoServlet?action=delete&id=<%= producto.getIdProducto() %>', 
                                        '¿Está seguro que desea eliminar el producto <%= producto.getProducto() %>?')" 
                                        title="Eliminar">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
        <%
            } else {
        %>
        <div class="text-center py-5">
            <i class="fas fa-box fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay productos registrados</h5>
            <p class="text-muted">Haz clic en "Nuevo Producto" para agregar el primer producto.</p>
            <a href="ProductoServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primer Producto
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>

<script>
    // Mostrar mensaje de éxito o error si existe
    <% if (success != null && !success.isEmpty()) { %>
        showAlert('success', 'Éxito', '<%= success %>');
        // Limpiar el parámetro de la URL
        window.history.replaceState({}, document.title, 'ProductoServlet?action=list');
    <% } %>

    <% if (error != null && !error.isEmpty()) { %>
        showAlert('error', 'Error', '<%= error %>');
    <% } %>
</script>

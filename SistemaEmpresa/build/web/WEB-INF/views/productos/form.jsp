<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.sistemaempresa.models.Producto"%>
<%@page import="com.sistemaempresa.models.Marca"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - <%= request.getAttribute("producto") != null ? "Editar" : "Nuevo" %> Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navegación -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="DashboardServlet">
                <i class="fas fa-building"></i> Sistema Empresa
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="DashboardServlet">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a class="nav-link" href="ProductoServlet">
                    <i class="fas fa-box"></i> Productos
                </a>
                <a class="nav-link" href="LoginServlet?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <%
                    Producto producto = (Producto) request.getAttribute("producto");
                    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
                    boolean esEdicion = producto != null;
                %>
                
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>
                        <i class="fas fa-box"></i> 
                        <%= esEdicion ? "Editar Producto" : "Nuevo Producto" %>
                    </h1>
                    <a href="ProductoServlet" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver
                    </a>
                </div>

                <!-- Mensajes -->
                <% if (request.getParameter("error") != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getParameter("error") %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <!-- Formulario -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-<%= esEdicion ? "edit" : "plus" %>"></i>
                            Datos del Producto
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="POST" action="ProductoServlet" id="productoForm">
                            <input type="hidden" name="action" value="<%= esEdicion ? "update" : "save" %>">
                            <% if (esEdicion) { %>
                                <input type="hidden" name="idProducto" value="<%= producto.getIdProducto() %>">
                            <% } %>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="producto" class="form-label">
                                            <i class="fas fa-tag"></i> Nombre del Producto *
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="producto" 
                                               name="producto" 
                                               value="<%= esEdicion ? producto.getProducto() : "" %>"
                                               required 
                                               maxlength="45"
                                               placeholder="Ingrese el nombre del producto">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="idMarca" class="form-label">
                                            <i class="fas fa-tags"></i> Marca *
                                        </label>
                                        <select class="form-select" id="idMarca" name="idMarca" required>
                                            <option value="">Seleccione una marca</option>
                                            <%
                                                if (marcas != null) {
                                                    for (Marca marca : marcas) {
                                            %>
                                            <option value="<%= marca.getIdMarca() %>" 
                                                    <%= esEdicion && producto.getIdMarca() == marca.getIdMarca() ? "selected" : "" %>>
                                                <%= marca.getMarca() %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="descripcion" class="form-label">
                                    <i class="fas fa-align-left"></i> Descripción
                                </label>
                                <textarea class="form-control" 
                                          id="descripcion" 
                                          name="descripcion" 
                                          rows="3"
                                          maxlength="255"
                                          placeholder="Descripción del producto (opcional)"><%= esEdicion && producto.getDescripcion() != null ? producto.getDescripcion() : "" %></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="imagen" class="form-label">
                                    <i class="fas fa-image"></i> URL de Imagen
                                </label>
                                <input type="url" 
                                       class="form-control" 
                                       id="imagen" 
                                       name="imagen" 
                                       value="<%= esEdicion && producto.getImagen() != null ? producto.getImagen() : "" %>"
                                       placeholder="https://ejemplo.com/imagen.jpg">
                            </div>

                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="precioCosto" class="form-label">
                                            <i class="fas fa-dollar-sign"></i> Precio Costo *
                                        </label>
                                        <input type="number" 
                                               class="form-control" 
                                               id="precioCosto" 
                                               name="precioCosto" 
                                               value="<%= esEdicion ? producto.getPrecioCosto() : "" %>"
                                               step="0.01"
                                               min="0"
                                               required 
                                               placeholder="0.00">
                                    </div>
                                </div>
                                
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="precioVenta" class="form-label">
                                            <i class="fas fa-money-bill"></i> Precio Venta *
                                        </label>
                                        <input type="number" 
                                               class="form-control" 
                                               id="precioVenta" 
                                               name="precioVenta" 
                                               value="<%= esEdicion ? producto.getPrecioVenta() : "" %>"
                                               step="0.01"
                                               min="0"
                                               required 
                                               placeholder="0.00">
                                    </div>
                                </div>
                                
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="existencia" class="form-label">
                                            <i class="fas fa-boxes"></i> Existencia *
                                        </label>
                                        <input type="number" 
                                               class="form-control" 
                                               id="existencia" 
                                               name="existencia" 
                                               value="<%= esEdicion ? producto.getExistencia() : "" %>"
                                               min="0"
                                               required 
                                               placeholder="0">
                                    </div>
                                </div>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="ProductoServlet" class="btn btn-secondary me-md-2">
                                    <i class="fas fa-times"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> 
                                    <%= esEdicion ? "Actualizar" : "Guardar" %>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación del formulario
        document.getElementById('productoForm').addEventListener('submit', function(e) {
            const producto = document.getElementById('producto').value.trim();
            const marca = document.getElementById('idMarca').value;
            const precioCosto = parseFloat(document.getElementById('precioCosto').value);
            const precioVenta = parseFloat(document.getElementById('precioVenta').value);
            
            if (producto === '') {
                e.preventDefault();
                alert('Por favor ingrese el nombre del producto');
                document.getElementById('producto').focus();
                return false;
            }
            
            if (marca === '') {
                e.preventDefault();
                alert('Por favor seleccione una marca');
                document.getElementById('idMarca').focus();
                return false;
            }
            
            if (precioCosto >= precioVenta) {
                e.preventDefault();
                alert('El precio de venta debe ser mayor al precio de costo');
                document.getElementById('precioVenta').focus();
                return false;
            }
        });

        // Enfocar el primer campo al cargar
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('producto').focus();
        });
    </script>
</body>
</html>

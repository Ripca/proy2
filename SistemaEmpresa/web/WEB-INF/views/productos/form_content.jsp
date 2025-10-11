<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Producto" %>
<%@ page import="com.sistemaempresa.models.Marca" %>
<%@ page import="java.util.List" %>

<%
    Producto producto = (Producto) request.getAttribute("producto");
    List<Marca> marcas = (List<Marca>) request.getAttribute("marcas");
    boolean esEdicion = producto != null;
    String titulo = esEdicion ? "Editar Producto" : "Nuevo Producto";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información del producto</p>
    </div>
    <a href="ProductoServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-box-open"></i> Información del Producto
                </h5>
            </div>
            <div class="card-body">
                <form action="ProductoServlet" method="post" id="formProducto">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idProducto" value="<%= producto.getIdProducto() %>">
                    <% } %>

                    <div class="row">
                        <div class="col-md-8">
                            <div class="mb-3">
                                <label for="producto" class="form-label">
                                    Nombre del Producto <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="producto" name="producto" 
                                       value="<%= esEdicion ? producto.getProducto() : "" %>" 
                                       required maxlength="100">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-3">
                                <label for="idMarca" class="form-label">Marca</label>
                                <select class="form-select" id="idMarca" name="idMarca">
                                    <option value="">Seleccionar marca...</option>
                                    <%
                                        if (marcas != null) {
                                            for (Marca marca : marcas) {
                                                boolean selected = esEdicion && producto.getIdMarca() == marca.getIdMarca();
                                    %>
                                    <option value="<%= marca.getIdMarca() %>" <%= selected ? "selected" : "" %>>
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
                        <label for="descripcion" class="form-label">Descripción</label>
                        <textarea class="form-control" id="descripcion" name="descripcion" rows="3" 
                                  maxlength="200"><%= esEdicion && producto.getDescripcion() != null ? producto.getDescripcion() : "" %></textarea>
                    </div>

                    <div class="row">
                        <div class="col-md-4">
                            <div class="mb-3">
                                <label for="precioCosto" class="form-label">
                                    Precio Costo <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text">Q.</span>
                                    <input type="number" class="form-control" id="precioCosto" name="precioCosto" 
                                           value="<%= esEdicion && producto.getPrecioCosto() != null ? producto.getPrecioCosto().doubleValue() : "" %>"
                                           step="0.01" min="0" required>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-3">
                                <label for="precioVenta" class="form-label">
                                    Precio Venta <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text">Q.</span>
                                    <input type="number" class="form-control" id="precioVenta" name="precioVenta" 
                                           value="<%= esEdicion && producto.getPrecioVenta() != null ? producto.getPrecioVenta().doubleValue() : "" %>"
                                           step="0.01" min="0" required>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mb-3">
                                <label for="existencia" class="form-label">Existencia</label>
                                <input type="number" class="form-control" id="existencia" name="existencia"
                                       value="<%= esEdicion ? producto.getExistencia() : 0 %>"
                                       min="0">
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="imagen" class="form-label">URL de Imagen</label>
                        <input type="url" class="form-control" id="imagen" name="imagen"
                               value="<%= esEdicion && producto.getImagen() != null ? producto.getImagen() : "" %>"
                               maxlength="200"
                               placeholder="https://ejemplo.com/imagen.jpg">
                        <div class="form-text">URL de la imagen del producto (opcional)</div>
                    </div>

                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="ProductoServlet" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validación del formulario
    document.getElementById('formProducto').addEventListener('submit', function(e) {
        const producto = document.getElementById('producto').value.trim();
        const precioCosto = parseFloat(document.getElementById('precioCosto').value);
        const precioVenta = parseFloat(document.getElementById('precioVenta').value);
        
        if (!producto) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del producto es obligatorio.');
            return false;
        }
        
        if (precioCosto <= 0 || precioVenta <= 0) {
            e.preventDefault();
            showAlert('error', 'Error', 'Los precios deben ser mayores a cero.');
            return false;
        }
        
        if (precioVenta <= precioCosto) {
            e.preventDefault();
            showAlert('warning', 'Advertencia', 'El precio de venta debería ser mayor al precio de costo.');
            return false;
        }
    });
    
    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('producto').focus();
    });
</script>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Proveedor" %>

<%
    Proveedor proveedor = (Proveedor) request.getAttribute("proveedor");
    boolean esEdicion = proveedor != null;
    String titulo = esEdicion ? "Editar Proveedor" : "Nuevo Proveedor";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información del proveedor</p>
    </div>
    <a href="ProveedorServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-truck-loading"></i> Información del Proveedor
                </h5>
            </div>
            <div class="card-body">
                <form action="ProveedorServlet" method="post" id="formProveedor">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idProveedor" value="<%= proveedor.getIdProveedor() %>">
                    <% } %>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="proveedor" class="form-label">
                                    Nombre del Proveedor <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="proveedor" name="proveedor"
                                       value="<%= esEdicion ? proveedor.getProveedor() : "" %>"
                                       required maxlength="60">
                                <div class="form-text">Ingrese el nombre completo del proveedor</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="nit" class="form-label">NIT</label>
                                <input type="text" class="form-control" id="nit" name="nit"
                                       value="<%= esEdicion && proveedor.getNit() != null ? proveedor.getNit() : "" %>"
                                       maxlength="12">
                                <div class="form-text">Número de Identificación Tributaria</div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input type="tel" class="form-control" id="telefono" name="telefono"
                                       value="<%= esEdicion && proveedor.getTelefono() != null ? proveedor.getTelefono() : "" %>"
                                       maxlength="25">
                                <div class="form-text">Número de teléfono de contacto</div>
                            </div>
                        </div>

                    </div>

                    <div class="mb-3">
                        <label for="direccion" class="form-label">Dirección</label>
                        <textarea class="form-control" id="direccion" name="direccion" rows="3"
                                  maxlength="80"><%= esEdicion && proveedor.getDireccion() != null ? proveedor.getDireccion() : "" %></textarea>
                        <div class="form-text">Dirección completa del proveedor</div>
                    </div>



                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="ProveedorServlet" class="btn btn-secondary">
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
    document.getElementById('formProveedor').addEventListener('submit', function(e) {
        const proveedor = document.getElementById('proveedor').value.trim();
        
        if (!proveedor) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del proveedor es obligatorio.');
            return false;
        }
        
        if (proveedor.length < 2) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del proveedor debe tener al menos 2 caracteres.');
            return false;
        }
    });
    
    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('proveedor').focus();
    });
</script>

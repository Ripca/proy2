<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Cliente" %>

<%
    Cliente cliente = (Cliente) request.getAttribute("cliente");
    boolean esEdicion = cliente != null;
    String titulo = esEdicion ? "Editar Cliente" : "Nuevo Cliente";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información del cliente</p>
    </div>
    <a href="ClienteServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-user-edit"></i> Información del Cliente
                </h5>
            </div>
            <div class="card-body">
                <form action="ClienteServlet" method="post" id="formCliente">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idCliente" value="<%= cliente.getIdCliente() %>">
                    <% } %>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="cliente" class="form-label">
                                    Nombre del Cliente <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="cliente" name="cliente" 
                                       value="<%= esEdicion ? cliente.getCliente() : "" %>" 
                                       required maxlength="100">
                                <div class="form-text">Ingrese el nombre completo del cliente</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="nit" class="form-label">NIT</label>
                                <input type="text" class="form-control" id="nit" name="nit" 
                                       value="<%= esEdicion && cliente.getNit() != null ? cliente.getNit() : "" %>" 
                                       maxlength="20">
                                <div class="form-text">Número de Identificación Tributaria</div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input type="tel" class="form-control" id="telefono" name="telefono" 
                                       value="<%= esEdicion && cliente.getTelefono() != null ? cliente.getTelefono() : "" %>" 
                                       maxlength="15">
                                <div class="form-text">Número de teléfono de contacto</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="<%= esEdicion && cliente.getEmail() != null ? cliente.getEmail() : "" %>" 
                                       maxlength="100">
                                <div class="form-text">Correo electrónico del cliente</div>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="direccion" class="form-label">Dirección</label>
                        <textarea class="form-control" id="direccion" name="direccion" rows="3" 
                                  maxlength="200"><%= esEdicion && cliente.getDireccion() != null ? cliente.getDireccion() : "" %></textarea>
                        <div class="form-text">Dirección completa del cliente</div>
                    </div>

                    <% if (esEdicion && cliente.getFechaIngreso() != null) { %>
                    <div class="mb-3">
                        <label class="form-label">Fecha de Ingreso</label>
                        <input type="text" class="form-control" 
                               value="<%= cliente.getFechaIngreso().toString() %>" readonly>
                        <div class="form-text">Fecha en que se registró el cliente</div>
                    </div>
                    <% } %>

                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="ClienteServlet" class="btn btn-secondary">
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
    document.getElementById('formCliente').addEventListener('submit', function(e) {
        const cliente = document.getElementById('cliente').value.trim();
        
        if (!cliente) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del cliente es obligatorio.');
            return false;
        }
        
        if (cliente.length < 2) {
            e.preventDefault();
            showAlert('error', 'Error', 'El nombre del cliente debe tener al menos 2 caracteres.');
            return false;
        }
    });
    
    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('cliente').focus();
    });
</script>

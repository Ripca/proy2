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
                <form action="ProductoServlet" method="post" id="formProducto" enctype="multipart/form-data">
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
                        <label for="imagenFile" class="form-label">Imagen del Producto</label>
                        <div class="input-group">
                            <input type="file" class="form-control" id="imagenFile" name="imagenFile"
                                   accept=".png,.jpg,.jpeg" onchange="previewImagen(event)">
                            <button class="btn btn-outline-secondary" type="button" id="btnLimpiar"
                                    onclick="limpiarImagen()" style="display: none;">
                                <i class="fas fa-trash"></i> Limpiar
                            </button>
                        </div>
                        <div class="form-text">Solo PNG, JPEG o JPG (máximo 5MB)</div>

                        <!-- Input oculto para guardar la ruta de la imagen -->
                        <input type="hidden" id="imagen" name="imagen"
                               value="<%= esEdicion && producto.getImagen() != null ? producto.getImagen() : "" %>">

                        <!-- Preview de la imagen -->
                        <div id="previewContainer" class="mt-3" style="display: none;">
                            <img id="previewImg" src="" alt="Preview" style="max-width: 200px; max-height: 200px; border-radius: 5px;">
                        </div>

                        <!-- Mostrar imagen actual si existe -->
                        <% if (esEdicion && producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                            String rutaImagenActual = producto.getImagen();
                            // Convertir "web/assets/..." a "/SistemaEmpresa/assets/..."
                            if (rutaImagenActual.startsWith("web/")) {
                                rutaImagenActual = request.getContextPath() + "/" + rutaImagenActual.substring(4);
                            }
                            // Agregar timestamp para evitar caché del navegador
                            rutaImagenActual = rutaImagenActual + "?t=" + System.currentTimeMillis();
                        %>
                            <div class="mt-3">
                                <p class="text-muted">Imagen actual:</p>
                                <img src="<%= rutaImagenActual %>" alt="Imagen actual"
                                     style="max-width: 200px; max-height: 200px; border-radius: 5px;">
                            </div>
                        <% } %>
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
    // Preview de imagen
    function previewImagen(event) {
        const file = event.target.files[0];

        if (file) {
            // Validar tipo de archivo
            const tiposPermitidos = ['image/png', 'image/jpeg', 'image/jpg'];
            if (!tiposPermitidos.includes(file.type)) {
                showAlert('error', 'Error', 'Solo se permiten archivos PNG, JPEG o JPG');
                event.target.value = '';
                return;
            }

            // Validar tamaño (máximo 5MB)
            if (file.size > 5 * 1024 * 1024) {
                showAlert('error', 'Error', 'El archivo es demasiado grande (máximo 5MB)');
                event.target.value = '';
                return;
            }

            // Mostrar preview
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('previewImg').src = e.target.result;
                document.getElementById('previewContainer').style.display = 'block';
                document.getElementById('btnLimpiar').style.display = 'inline-block';
            };
            reader.readAsDataURL(file);
        }
    }

    // Limpiar imagen
    function limpiarImagen() {
        document.getElementById('imagenFile').value = '';
        document.getElementById('previewContainer').style.display = 'none';
        document.getElementById('btnLimpiar').style.display = 'none';
    }

    // Validación del formulario
    document.getElementById('formProducto').addEventListener('submit', function(e) {
        const producto = document.getElementById('producto').value.trim();
        const precioCosto = parseFloat(document.getElementById('precioCosto').value);
        const precioVenta = parseFloat(document.getElementById('precioVenta').value);
        const imagenFile = document.getElementById('imagenFile').files[0];

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

        // Si hay archivo de imagen, subirlo antes de enviar el formulario
        if (imagenFile) {
            e.preventDefault();
            subirImagen(imagenFile);
        }
    });

    // Subir imagen
    function subirImagen(file) {
        const formData = new FormData();
        formData.append('imagen', file);

        fetch('UploadImagenServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Guardar la ruta de la imagen en el input oculto
                document.getElementById('imagen').value = data.imagen;
                // Enviar el formulario
                document.getElementById('formProducto').submit();
            } else {
                showAlert('error', 'Error', data.message);
            }
        })
        .catch(error => {
            showAlert('error', 'Error', 'Error al subir la imagen: ' + error);
        });
    }

    // Auto-focus en el primer campo
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('producto').focus();
    });
</script>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    boolean esEdicion = venta != null;
    String titulo = esEdicion ? "Editar Venta" : "Nueva Venta";
    String action = esEdicion ? "update" : "save";
%>

<!-- HEADER -->
<div class="page-header">
    <div class="add-item d-flex">
        <div class="page-title">
            <h4><i class="fas fa-receipt me-2"></i><%= titulo %></h4>
            <h6>Gestión de ventas a clientes</h6>
        </div>
    </div>
    <div class="page-btn">
        <a href="VentaServlet" class="btn btn-added color">
            <i class="fas fa-list me-2"></i>Listado de ventas
        </a>
    </div>
</div>

<!-- CONTENIDO PRINCIPAL -->
<div class="row">
    <div class="col-md-12">
        <div class="card shadow-sm">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0"><i class="fas fa-file-invoice"></i> Datos de la Venta</h5>
            </div>
            <div class="card-body">
                <form action="VentaServlet" method="post" id="formVenta">
                    <input type="hidden" name="action" value="<%= action %>">
                    <input type="hidden" name="idCliente" id="hiddenIdCliente" value="">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idVenta" value="<%= venta.getIdVenta() %>">
                    <% } %>

                    <!-- DATOS PRINCIPALES -->
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Cliente <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                                <input type="text" id="txtNitCliente" class="form-control" placeholder="Buscar por NIT" autocomplete="off">
                                <button type="button" class="btn btn-outline-success" id="btnBuscarCliente">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                            </div>
                            <small class="text-muted">Ingrese el NIT del cliente</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Nombre Cliente</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                <input type="text" id="txtNombreCliente" class="form-control" disabled>
                            </div>
                            <span id="lblIdCliente" style="display:none;"></span>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-3">
                            <label class="form-label fw-bold">No. Factura <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-hashtag"></i></span>
                                <input type="number" id="txtNoFactura" name="noFactura" class="form-control" 
                                       value="<%= esEdicion ? String.valueOf(venta.getNoFactura()) : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Serie <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-barcode"></i></span>
                                <input type="text" id="txtSerie" name="serie" class="form-control" 
                                       value="<%= esEdicion ? venta.getSerie() : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Fecha Venta <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-calendar"></i></span>
                                <input type="date" id="txtFecha" name="fecha" class="form-control"
                                       value="<%= esEdicion && venta.getFechaFactura() != null ? venta.getFechaFactura().toString() : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label fw-bold">Teléfono Cliente</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                <input type="text" id="txtTelefonoCliente" class="form-control" disabled>
                            </div>
                        </div>
                    </div>

                    <hr class="my-4">

                    <!-- SECCION DE PRODUCTOS -->
                    <h5 class="mb-3"><i class="fas fa-box"></i> Productos de la Venta</h5>
                    
                    <div class="row mb-3">
                        <div class="col-md-8">
                            <label class="form-label fw-bold">Buscar Producto</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-barcode"></i></span>
                                <input type="text" id="txtCodigoProducto" class="form-control" placeholder="Ingrese código o nombre del producto" autocomplete="off">
                                <button type="button" class="btn btn-outline-success" id="btnBuscarProducto">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- TABLA DE PRODUCTOS -->
                    <div class="table-responsive mb-4">
                        <table id="grvProductosCompra" class="table table-hover table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th style="display:none;">Id Producto</th>
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>Precio Venta</th>
                                    <th>Subtotal</th>
                                    <th style="width: 80px;">Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>

                    <!-- RESUMEN DE TOTALES -->
                    <div class="row">
                        <div class="col-md-8"></div>
                        <div class="col-md-4">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span>Subtotal:</span>
                                        <strong><span class="lbl-info-subtotal">Q. 0.00</span></strong>
                                    </div>
                                    <div class="d-flex justify-content-between mb-3 pb-3 border-bottom">
                                        <span>Descuento:</span>
                                        <strong><span class="lbl-info-descuento">Q. 0.00</span></strong>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <h5>Total:</h5>
                                        <h5><strong><span class="lbl-info-total">Q. 0.00</span></strong></h5>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- BOTONES DE ACCION -->
                    <div class="row mt-4">
                        <div class="col-md-12">
                            <button type="submit" class="btn btn-success btn-lg me-2">
                                <i class="fas fa-save me-2"></i>Guardar Venta
                            </button>
                            <a href="VentaServlet" class="btn btn-secondary btn-lg">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    // Agregar producto a la tabla
    function agregarProductoATabla(producto) {
        const table = document.querySelector('#grvProductosCompra tbody');
        const fila = document.createElement('tr');
        const subtotal = producto.precio_venta * 1;
        
        fila.innerHTML = `
            <td style="display:none;">${producto.id_producto}</td>
            <td>${producto.producto}</td>
            <td><input type="number" class="form-control form-control-sm cantidad" value="1" min="1" onchange="actualizarSubtotal(this)"></td>
            <td>Q. ${parseFloat(producto.precio_venta).toFixed(2)}</td>
            <td class="subtotal">Q. ${subtotal.toFixed(2)}</td>
            <td><button type="button" class="btn btn-sm btn-danger" onclick="this.closest('tr').remove(); actualizarTotales();">
                <i class="fas fa-trash"></i>
            </button></td>
        `;
        table.appendChild(fila);
        actualizarTotales();
    }

    // Actualizar subtotal
    function actualizarSubtotal(input) {
        const fila = input.closest('tr');
        const cantidad = parseFloat(input.value) || 0;
        const precioVenta = parseFloat(fila.cells[3].textContent.replace('Q. ', ''));
        const subtotal = cantidad * precioVenta;
        fila.querySelector('.subtotal').textContent = 'Q. ' + subtotal.toFixed(2);
        actualizarTotales();
    }

    // Actualizar totales
    function actualizarTotales() {
        let subtotal = 0;
        const filas = document.querySelectorAll('#grvProductosCompra tbody tr');
        filas.forEach(fila => {
            const subtotalText = fila.querySelector('.subtotal').textContent.replace('Q. ', '');
            subtotal += parseFloat(subtotalText) || 0;
        });
        document.querySelector('.lbl-info-subtotal').textContent = 'Q. ' + subtotal.toFixed(2);
        document.querySelector('.lbl-info-descuento').textContent = 'Q. 0.00';
        document.querySelector('.lbl-info-total').textContent = 'Q. ' + subtotal.toFixed(2);
    }

    // Buscar cliente
    function buscarCliente() {
        const nit = document.getElementById('txtNitCliente').value.trim();
        if (!nit) { alert('Ingrese el NIT'); return; }
        $.ajax({
            url: 'ClienteServlet',
            type: 'GET',
            data: { action: 'buscarPorNit', nit: nit },
            dataType: 'json',
            success: function(result) {
                if (result && result.id_cliente) {
                    document.getElementById('lblIdCliente').textContent = result.id_cliente;
                    document.getElementById('hiddenIdCliente').value = result.id_cliente;
                    document.getElementById('txtNombreCliente').value = (result.nombres || '') + ' ' + (result.apellidos || '');
                    document.getElementById('txtTelefonoCliente').value = result.telefono || '';
                } else { alert('Cliente no encontrado'); }
            },
            error: function() { alert('Error al buscar'); }
        });
    }

    // Buscar producto
    function buscarProducto() {
        const termino = document.getElementById('txtCodigoProducto').value.trim();
        if (!termino) { alert('Ingrese código o nombre'); return; }
        $.ajax({
            url: 'ProductoServlet',
            type: 'GET',
            data: { action: 'buscarAjax', termino: termino },
            dataType: 'json',
            success: function(result) {
                if (result && result.length > 0) {
                    agregarProductoATabla(result[0]);
                    document.getElementById('txtCodigoProducto').value = '';
                } else { alert('Producto no encontrado'); }
            },
            error: function() { alert('Error al buscar'); }
        });
    }

    $(document).ready(function() {
        document.getElementById('btnBuscarCliente').addEventListener('click', buscarCliente);
        document.getElementById('btnBuscarProducto').addEventListener('click', buscarProducto);
        document.getElementById('txtNitCliente').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') { e.preventDefault(); buscarCliente(); }
        });
        document.getElementById('txtCodigoProducto').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') { e.preventDefault(); buscarProducto(); }
        });
        document.getElementById('formVenta').addEventListener('submit', function(e) {
            e.preventDefault();
            if (!document.getElementById('hiddenIdCliente').value) { alert('Seleccione cliente'); return; }
            const filas = document.querySelectorAll('#grvProductosCompra tbody tr');
            if (filas.length === 0) { alert('Agregue productos'); return; }
            filas.forEach((fila) => {
                const input1 = document.createElement('input');
                input1.type = 'hidden'; input1.name = 'idProducto'; input1.value = fila.cells[0].textContent;
                this.appendChild(input1);
                const input2 = document.createElement('input');
                input2.type = 'hidden'; input2.name = 'cantidad'; input2.value = fila.querySelector('.cantidad').value;
                this.appendChild(input2);
                const input3 = document.createElement('input');
                input3.type = 'hidden'; input3.name = 'precioUnitario'; input3.value = fila.cells[3].textContent.replace('Q. ', '');
                this.appendChild(input3);
            });
            this.submit();
        });
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('txtFecha').value = today;
        actualizarTotales();
    });
</script>


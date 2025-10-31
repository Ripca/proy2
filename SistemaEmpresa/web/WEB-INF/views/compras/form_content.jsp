<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Compra compra = (Compra) request.getAttribute("compra");
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    List<CompraDetalle> detalles = (List<CompraDetalle>) request.getAttribute("detalles");

    if (proveedores == null) proveedores = new java.util.ArrayList<>();
    if (productos == null) productos = new java.util.ArrayList<>();
    if (detalles == null) detalles = new java.util.ArrayList<>();

    boolean esEdicion = compra != null;
    String titulo = esEdicion ? "Editar Compra" : "Nueva Compra";
    String action = esEdicion ? "update" : "save";
%>

<!-- HEADER -->
<div class="page-header">
    <div class="add-item d-flex">
        <div class="page-title">
            <h4><i class="fas fa-shopping-cart me-2"></i><%= titulo %></h4>
            <h6>Gestión de compras a proveedores</h6>
        </div>
    </div>
    <div class="page-btn">
        <a href="CompraServlet" class="btn btn-added color">
            <i class="fas fa-list me-2"></i>Listado de compras
        </a>
    </div>
</div>

<!-- CONTENIDO PRINCIPAL -->
<div class="row">
    <div class="col-md-12">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="fas fa-file-invoice"></i> Datos de la Compra</h5>
            </div>
            <div class="card-body">
                <form action="CompraServlet" method="post" id="formCompra">
                    <input type="hidden" name="action" value="<%= action %>">
                    <input type="hidden" name="idProveedor" id="hiddenIdProveedor" value="<%= esEdicion ? compra.getIdProveedor() : "" %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idCompra" value="<%= compra.getIdCompra() %>">
                    <% } %>

                    <!-- DATOS PRINCIPALES -->
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Proveedor <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-truck"></i></span>
                                <input type="text" id="txtNitProveedor" class="form-control" placeholder="Buscar por NIT" autocomplete="off"
                                       value="<%= esEdicion && compra.getNombreProveedor() != null ? "" : "" %>">
                                <button type="button" class="btn btn-outline-primary" id="btnBuscarProveedor">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                                <button type="button" class="btn btn-outline-info" id="btnListarProveedores" data-bs-toggle="modal" data-bs-target="#modalProveedores">
                                    <i class="fas fa-list"></i> Listar
                                </button>
                            </div>
                            <small class="text-muted">Ingrese el NIT del proveedor o haga clic en Listar</small>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Nombre Proveedor</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-building"></i></span>
                                <input type="text" id="txtNombreProveedor" class="form-control" disabled
                                       value="<%= esEdicion && compra.getNombreProveedor() != null ? compra.getNombreProveedor() : "" %>">
                            </div>
                            <span id="lblIdProveedor" style="display:none;"><%= esEdicion ? compra.getIdProveedor() : "" %></span>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-4">
                            <label class="form-label fw-bold">No. Orden <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-hashtag"></i></span>
                                <input type="number" id="txtNoOrden" name="noOrdenCompra" class="form-control"
                                       value="<%= esEdicion ? String.valueOf(compra.getNoOrdenCompra()) : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Fecha Orden <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-calendar"></i></span>
                                <input type="date" id="txtFecha" name="fechaOrden" class="form-control"
                                       value="<%= esEdicion && compra.getFechaOrden() != null ? compra.getFechaOrden().toString() : "" %>" required>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Teléfono Proveedor</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                <input type="text" id="txtTelefonoProveedor" class="form-control" disabled
                                       value="<%= esEdicion && compra.getTelefonoProveedor() != null ? compra.getTelefonoProveedor() : "" %>">
                            </div>
                        </div>
                    </div>

                    <hr class="my-4">

                    <!-- SECCION DE PRODUCTOS -->
                    <h5 class="mb-3"><i class="fas fa-box"></i> Productos de la Compra</h5>

                    <div class="row mb-3">
                        <div class="col-md-8">
                            <label class="form-label fw-bold">Buscar Producto</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-barcode"></i></span>
                                <input type="text" id="txtCodigoProducto" class="form-control" placeholder="Ingrese código o nombre del producto" autocomplete="off">
                                <button type="button" class="btn btn-outline-primary" id="btnBuscarProducto">
                                    <i class="fas fa-search"></i> Buscar
                                </button>
                                <button type="button" class="btn btn-outline-info" id="btnListarProductos" data-bs-toggle="modal" data-bs-target="#modalProductos">
                                    <i class="fas fa-list"></i> Listar
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- TABLA DE PRODUCTOS -->
                    <div class="table-responsive mb-4">
                        <table id="grvProductosCompra" class="table table-hover table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th style="width: 80px;">ID</th>
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>Existencias</th>
                                    <th>Precio Costo</th>
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
                                <i class="fas fa-save me-2"></i>Guardar Compra
                            </button>
                            <a href="CompraServlet" class="btn btn-secondary btn-lg">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </a>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<!-- MODAL DE PROVEEDORES -->
<div class="modal fade" id="modalProveedores" tabindex="-1" aria-labelledby="modalProveedoresLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="modalProveedoresLabel">Seleccionar Proveedor</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered" id="tablaProveedoresModal">
                        <thead class="table-dark">
                            <tr>
                                <th>NIT</th>
                                <th>Nombre</th>
                                <th>Teléfono</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody id="bodyProveedoresModal">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- MODAL DE PRODUCTOS -->
<div class="modal fade" id="modalProductos" tabindex="-1" aria-labelledby="modalProductosLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title" id="modalProductosLabel">Seleccionar Producto</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered" id="tablaProductosModal">
                        <thead class="table-dark">
                            <tr>
                                <th>Código</th>
                                <th>Producto</th>
                                <th>Existencias</th>
                                <th>Precio Costo</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody id="bodyProductosModal">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function agregarProductoATabla(producto) {
        const table = document.querySelector('#grvProductosCompra tbody');
        const fila = document.createElement('tr');
        const subtotal = producto.precio_costo * 1;

        // Input oculto para idProducto (REQUERIDO para el servidor)
        const inputIdProducto = document.createElement('input');
        inputIdProducto.type = 'hidden';
        inputIdProducto.name = 'idProducto';
        inputIdProducto.value = producto.idProducto;
        fila.appendChild(inputIdProducto);

        // Input oculto para precioCostoUnitario (REQUERIDO para el servidor)
        const inputPrecioOculto = document.createElement('input');
        inputPrecioOculto.type = 'hidden';
        inputPrecioOculto.name = 'precioCostoUnitario';
        inputPrecioOculto.value = producto.precio_costo;
        fila.appendChild(inputPrecioOculto);

        // Mostrar ID del producto en la tabla
        const tdId = document.createElement('td');
        tdId.textContent = producto.idProducto;

        const tdNombre = document.createElement('td');
        tdNombre.textContent = producto.producto;

        const tdCantidad = document.createElement('td');
        const inputCantidad = document.createElement('input');
        inputCantidad.type = 'number';
        inputCantidad.className = 'form-control form-control-sm cantidad';
        inputCantidad.name = 'cantidad';
        inputCantidad.value = '1';
        inputCantidad.min = '1';
        inputCantidad.max = 999999;
        inputCantidad.dataset.existencia = producto.existencia || 0;
        inputCantidad.addEventListener('change', function() {
            actualizarSubtotal(this);
        });
        tdCantidad.appendChild(inputCantidad);

        const tdExistencia = document.createElement('td');
        tdExistencia.textContent = producto.existencia || 0;

        const tdPrecio = document.createElement('td');
        tdPrecio.textContent = 'Q. ' + parseFloat(producto.precio_costo).toFixed(2);

        const tdSubtotal = document.createElement('td');
        tdSubtotal.className = 'subtotal';
        tdSubtotal.textContent = 'Q. ' + subtotal.toFixed(2);

        const tdAccion = document.createElement('td');
        const btnEliminar = document.createElement('button');
        btnEliminar.type = 'button';
        btnEliminar.className = 'btn btn-sm btn-danger';
        btnEliminar.innerHTML = '<i class="fas fa-trash"></i>';
        btnEliminar.addEventListener('click', function() { eliminarFilaProducto(this); });
        tdAccion.appendChild(btnEliminar);

        fila.appendChild(tdId);
        fila.appendChild(tdNombre);
        fila.appendChild(tdCantidad);
        fila.appendChild(tdExistencia);
        fila.appendChild(tdPrecio);
        fila.appendChild(tdSubtotal);
        fila.appendChild(tdAccion);

        table.appendChild(fila);
        console.log('DEBUG: Producto agregado - idProducto: ' + producto.idProducto + ', precio: ' + producto.precio_costo);
        actualizarTotales();
    }

    function eliminarFilaProducto(btn) {
        btn.closest('tr').remove();
        actualizarTotales();
    }

    function actualizarSubtotal(input) {
        const fila = input.closest('tr');
        const cantidad = parseFloat(input.value) || 0;

        // Obtener el precio del input oculto precioCostoUnitario
        const precioInput = fila.querySelector('input[name="precioCostoUnitario"]');
        const precioCosto = precioInput ? parseFloat(precioInput.value) : 0;

        const subtotal = cantidad * precioCosto;
        fila.querySelector('.subtotal').textContent = 'Q. ' + subtotal.toFixed(2);
        console.log('DEBUG actualizarSubtotal: cantidad=' + cantidad + ', precio=' + precioCosto + ', subtotal=' + subtotal);
        actualizarTotales();
    }

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

    function buscarProveedor() {
        const nit = document.getElementById('txtNitProveedor').value.trim();
        if (!nit) {
            alert('Ingrese el NIT del proveedor');
            return;
        }
        $.ajax({
            url: 'ProveedorServlet',
            type: 'GET',
            data: { action: 'buscarPorNit', nit: nit },
            dataType: 'json',
            success: function(result) {
                if (result && result.id_proveedor) {
                    document.getElementById('lblIdProveedor').textContent = result.id_proveedor;
                    document.getElementById('hiddenIdProveedor').value = result.id_proveedor;
                    document.getElementById('txtNombreProveedor').value = result.proveedor;
                    document.getElementById('txtTelefonoProveedor').value = result.telefono || '';
                } else {
                    if (confirm('Proveedor no encontrado. ¿Desea crear un nuevo proveedor con este NIT?')) {
                        window.location.href = 'ProveedorServlet?action=new&nit=' + encodeURIComponent(nit);
                    }
                }
            },
            error: function() {
                alert('Error al buscar el proveedor');
            }
        });
    }

    function buscarProducto() {
        const termino = document.getElementById('txtCodigoProducto').value.trim();
        if (!termino) {
            alert('Ingrese el código o nombre del producto');
            return;
        }
        $.ajax({
            url: 'ProductoServlet',
            type: 'GET',
            data: { action: 'buscarAjax', termino: termino },
            dataType: 'json',
            success: function(result) {
                if (result && result.length > 0) {
                    agregarProductoATabla(result[0]);
                    document.getElementById('txtCodigoProducto').value = '';
                } else {
                    if (confirm('Producto no encontrado. ¿Desea crear un nuevo producto?')) {
                        window.location.href = 'ProductoServlet?action=new&nombre=' + encodeURIComponent(termino);
                    }
                }
            },
            error: function() {
                alert('Error al buscar el producto');
            }
        });
    }

    function cargarProveedoresModal() {
        $.ajax({
            url: 'ProveedorServlet',
            type: 'GET',
            data: { action: 'obtenerTodos' },
            dataType: 'json',
            success: function(result) {
                const tbody = document.getElementById('bodyProveedoresModal');
                tbody.innerHTML = '';
                if (result && result.length > 0) {
                    result.forEach(function(proveedor) {
                        const fila = document.createElement('tr');
                        const tdNit = document.createElement('td');
                        tdNit.textContent = proveedor.nit;
                        const tdNombre = document.createElement('td');
                        tdNombre.textContent = proveedor.proveedor;
                        const tdTelefono = document.createElement('td');
                        tdTelefono.textContent = proveedor.telefono;
                        const tdAccion = document.createElement('td');
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = 'btn btn-sm btn-success';
                        btn.innerHTML = '<i class="fas fa-check"></i> Seleccionar';
                        btn.addEventListener('click', function() {
                            seleccionarProveedorModal(proveedor.id_proveedor, proveedor.proveedor, proveedor.nit, proveedor.telefono);
                        });
                        tdAccion.appendChild(btn);
                        fila.appendChild(tdNit);
                        fila.appendChild(tdNombre);
                        fila.appendChild(tdTelefono);
                        fila.appendChild(tdAccion);
                        tbody.appendChild(fila);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="4" class="text-center">No hay proveedores disponibles</td></tr>';
                }
            },
            error: function() {
                alert('Error al cargar proveedores');
            }
        });
    }

    function cargarProductosModal() {
        $.ajax({
            url: 'ProductoServlet',
            type: 'GET',
            data: { action: 'obtenerTodos' },
            dataType: 'json',
            success: function(result) {
                const tbody = document.getElementById('bodyProductosModal');
                    console.log('entrooo pruebaaaa');
                    console.log(tbody.innerHTML);
        tbody.innerHTML = '';
                if (result && result.length > 0) {
                    result.forEach(function(producto) {
                        const fila = document.createElement('tr');
                        const tdCodigo = document.createElement('td');
                        tdCodigo.textContent = producto.idProducto || '';
                        const tdNombre = document.createElement('td');
                        tdNombre.textContent = producto.producto;
                        const tdExistencia = document.createElement('td');
                        tdExistencia.textContent = producto.existencia || 0;
                        const tdPrecio = document.createElement('td');
                        tdPrecio.textContent = 'Q. ' + parseFloat(producto.precio_costo).toFixed(2);
                        const tdAccion = document.createElement('td');
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = 'btn btn-sm btn-success';
                        btn.innerHTML = '<i class="fas fa-check"></i> Seleccionar';
                        btn.addEventListener('click', function() {
                            agregarProductoATabla(producto);
                            const modal = bootstrap.Modal.getInstance(document.getElementById('modalProductos'));
                            if (modal) modal.hide();
                        });
                        tdAccion.appendChild(btn);
                        fila.appendChild(tdCodigo);
                        fila.appendChild(tdNombre);
                        fila.appendChild(tdExistencia);
                        fila.appendChild(tdPrecio);
                        fila.appendChild(tdAccion);
                        tbody.appendChild(fila);
                    });
                    console.log('pruebaaaa');
                    console.log(tbody.innerHTML);
                    
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay productos disponibles</td></tr>';
                }
            },
            error: function() {
                alert('Error al cargar productos');
            }
        });
    }

    function seleccionarProveedorModal(idProveedor, nombre, nit, telefono) {
        document.getElementById('lblIdProveedor').textContent = idProveedor;
        document.getElementById('hiddenIdProveedor').value = idProveedor;
        document.getElementById('txtNombreProveedor').value = nombre;
        document.getElementById('txtNitProveedor').value = nit;
        document.getElementById('txtTelefonoProveedor').value = telefono;
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalProveedores'));
        if (modal) modal.hide();
    }

    function cargarDetallesCompra() {
        <% if (esEdicion && detalles != null && !detalles.isEmpty()) { %>
            const table = document.querySelector('#grvProductosCompra tbody');

            <% for (CompraDetalle detalle : detalles) { %>
                const fila = document.createElement('tr');
                const cantidad = <%= detalle.getCantidad() %>;
                const precio = <%= detalle.getPrecioCostoUnitario() %>;
                const subtotal = cantidad * precio;
                const idProducto = '<%= detalle.getIdProducto() %>';

                // Input oculto para idCompraDetalle (para identificar detalles existentes)
                const inputIdCompraDetalle = document.createElement('input');
                inputIdCompraDetalle.type = 'hidden';
                inputIdCompraDetalle.name = 'idCompraDetalle';
                inputIdCompraDetalle.value = '<%= detalle.getIdCompraDetalle() %>';
                fila.appendChild(inputIdCompraDetalle);

                // Input oculto para idProducto
                const inputIdProducto = document.createElement('input');
                inputIdProducto.type = 'hidden';
                inputIdProducto.name = 'idProducto';
                inputIdProducto.value = idProducto;
                fila.appendChild(inputIdProducto);

                // Input oculto para precioCostoUnitario
                const inputPrecio = document.createElement('input');
                inputPrecio.type = 'hidden';
                inputPrecio.name = 'precioCostoUnitario';
                inputPrecio.value = precio;
                fila.appendChild(inputPrecio);

                // Mostrar ID del producto en la tabla
                const tdIdProducto = document.createElement('td');
                tdIdProducto.textContent = idProducto;

                const tdNombre = document.createElement('td');
                tdNombre.textContent = '<%= detalle.getNombreProducto() != null ? detalle.getNombreProducto() : "Producto" %>';

                const tdCantidad = document.createElement('td');
                const inputCantidad = document.createElement('input');
                inputCantidad.type = 'number';
                inputCantidad.className = 'form-control form-control-sm cantidad';
                inputCantidad.name = 'cantidad';
                inputCantidad.value = cantidad;
                inputCantidad.min = '1';
                inputCantidad.addEventListener('change', function() {
                    actualizarSubtotal(this);
                });
                tdCantidad.appendChild(inputCantidad);

                const tdPrecio = document.createElement('td');
                tdPrecio.textContent = 'Q. ' + precio.toFixed(2);

                const tdSubtotal = document.createElement('td');
                tdSubtotal.className = 'subtotal';
                tdSubtotal.textContent = 'Q. ' + subtotal.toFixed(2);

                const tdAccion = document.createElement('td');
                const btnEliminar = document.createElement('button');
                btnEliminar.type = 'button';
                btnEliminar.className = 'btn btn-sm btn-danger';
                btnEliminar.innerHTML = '<i class="fas fa-trash"></i>';
                btnEliminar.addEventListener('click', function() { eliminarFilaProducto(this); });
                tdAccion.appendChild(btnEliminar);

                const tdExistencias = document.createElement('td');
                tdExistencias.textContent = '-';

                fila.appendChild(tdIdProducto);
                fila.appendChild(tdNombre);
                fila.appendChild(tdCantidad);
                fila.appendChild(tdExistencias);
                fila.appendChild(tdPrecio);
                fila.appendChild(tdSubtotal);
                fila.appendChild(tdAccion);

                table.appendChild(fila);
            <% } %>

            actualizarTotales();
        <% } %>
    }

    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('btnBuscarProveedor').addEventListener('click', buscarProveedor);
        document.getElementById('btnBuscarProducto').addEventListener('click', buscarProducto);
        document.getElementById('btnListarProveedores').addEventListener('click', cargarProveedoresModal);
        document.getElementById('btnListarProductos').addEventListener('click', cargarProductosModal);

        document.getElementById('txtNitProveedor').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarProveedor();
            }
        });

        document.getElementById('txtCodigoProducto').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarProducto();
            }
        });

        document.getElementById('formCompra').addEventListener('submit', function(e) {
            e.preventDefault();
            if (!document.getElementById('hiddenIdProveedor').value) {
                alert('Debe seleccionar un proveedor');
                return;
            }
            const filas = document.querySelectorAll('#grvProductosCompra tbody tr');
            if (filas.length === 0) {
                alert('Debe agregar al menos un producto');
                return;
            }

            // DEBUG: Verificar que cada fila tenga los inputs ocultos correctos
            let filasValidas = 0;
            filas.forEach((fila, index) => {
                const idProductoInput = fila.querySelector('input[name="idProducto"]');
                const cantidadInput = fila.querySelector('input[name="cantidad"]');
                const precioInput = fila.querySelector('input[name="precioCostoUnitario"]');

                if (idProductoInput && idProductoInput.value && idProductoInput.value.trim() !== '') {
                    filasValidas++;
                    console.log('DEBUG Fila ' + index + ': idProducto=' + idProductoInput.value + ', cantidad=' + (cantidadInput ? cantidadInput.value : 'N/A') + ', precio=' + (precioInput ? precioInput.value : 'N/A'));
                } else {
                    console.warn('DEBUG: Fila ' + index + ' sin idProducto válido, será ignorada');
                }
            });

            if (filasValidas === 0) {
                alert('No hay productos válidos para guardar');
                return;
            }

            console.log('DEBUG: Total de filas válidas: ' + filasValidas);
            this.submit();
        });

        // Cargar detalles si es edición
        cargarDetallesCompra();
        actualizarTotales();
    });
</script>



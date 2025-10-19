<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.*" %>

<%
    Venta venta = (Venta) request.getAttribute("venta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");

    if (clientes == null) clientes = new java.util.ArrayList<>();
    if (productos == null) productos = new java.util.ArrayList<>();

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
                                <button type="button" class="btn btn-outline-info" id="btnListarClientes" data-bs-toggle="modal" data-bs-target="#modalClientes">
                                    <i class="fas fa-list"></i> Listar
                                </button>
                            </div>
                            <small class="text-muted">Ingrese el NIT del cliente o haga clic en Listar</small>
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
                                    <th style="display:none;">Id Producto</th>
                                    <th>Producto</th>
                                    <th>Cantidad</th>
                                    <th>Existencias</th>
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

<!-- MODAL DE CLIENTES -->
<div class="modal fade" id="modalClientes" tabindex="-1" aria-labelledby="modalClientesLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="modalClientesLabel">Seleccionar Cliente</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered" id="tablaClientesModal">
                        <thead class="table-dark">
                            <tr>
                                <th>NIT</th>
                                <th>Nombres</th>
                                <th>Apellidos</th>
                                <th>Teléfono</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody id="bodyClientesModal">
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
                                <th>Precio Venta</th>
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
        const subtotal = producto.precio_venta * 1;

        const tdId = document.createElement('td');
        tdId.style.display = 'none';
        tdId.textContent = producto.id_producto;

        const tdNombre = document.createElement('td');
        tdNombre.textContent = producto.producto;

        const tdCantidad = document.createElement('td');
        const inputCantidad = document.createElement('input');
        inputCantidad.type = 'number';
        inputCantidad.className = 'form-control form-control-sm cantidad';
        inputCantidad.value = '1';
        inputCantidad.min = '1';
        inputCantidad.max = producto.existencia || 999999;
        inputCantidad.dataset.existencia = producto.existencia || 0;
        inputCantidad.addEventListener('change', function() {
            const existencia = parseInt(this.dataset.existencia) || 0;
            const cantidad = parseInt(this.value) || 0;
            if (cantidad > existencia) {
                alert('La cantidad no puede ser mayor a las existencias disponibles (' + existencia + ')');
                this.value = existencia;
            }
            actualizarSubtotal(this);
        });
        tdCantidad.appendChild(inputCantidad);

        const tdExistencia = document.createElement('td');
        tdExistencia.textContent = producto.existencia || 0;

        const tdPrecio = document.createElement('td');
        tdPrecio.textContent = 'Q. ' + parseFloat(producto.precio_venta).toFixed(2);

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
        actualizarTotales();
    }

    function eliminarFilaProducto(btn) {
        btn.closest('tr').remove();
        actualizarTotales();
    }

    function actualizarSubtotal(input) {
        const fila = input.closest('tr');
        const cantidad = parseFloat(input.value) || 0;
        const precioVenta = parseFloat(fila.cells[4].textContent.replace('Q. ', ''));
        const subtotal = cantidad * precioVenta;
        fila.querySelector('.subtotal').textContent = 'Q. ' + subtotal.toFixed(2);
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

    function buscarCliente() {
        const nit = document.getElementById('txtNitCliente').value.trim();
        if (!nit) {
            alert('Ingrese el NIT');
            return;
        }
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
                } else {
                    if (confirm('Cliente no encontrado. ¿Desea crear un nuevo cliente con este NIT?')) {
                        window.location.href = 'ClienteServlet?action=new&nit=' + encodeURIComponent(nit);
                    }
                }
            },
            error: function() {
                alert('Error al buscar');
            }
        });
    }

    function buscarProducto() {
        const termino = document.getElementById('txtCodigoProducto').value.trim();
        if (!termino) {
            alert('Ingrese código o nombre');
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
                alert('Error al buscar');
            }
        });
    }

    function cargarClientesModal() {
        $.ajax({
            url: 'ClienteServlet',
            type: 'GET',
            data: { action: 'obtenerTodos' },
            dataType: 'json',
            success: function(result) {
                const tbody = document.getElementById('bodyClientesModal');
                tbody.innerHTML = '';
                if (result && result.length > 0) {
                    result.forEach(function(cliente) {
                        const fila = document.createElement('tr');
                        const tdNit = document.createElement('td');
                        tdNit.textContent = cliente.nit;
                        const tdNombres = document.createElement('td');
                        tdNombres.textContent = cliente.nombres;
                        const tdApellidos = document.createElement('td');
                        tdApellidos.textContent = cliente.apellidos;
                        const tdTelefono = document.createElement('td');
                        tdTelefono.textContent = cliente.telefono;
                        const tdAccion = document.createElement('td');
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = 'btn btn-sm btn-success';
                        btn.innerHTML = '<i class="fas fa-check"></i> Seleccionar';
                        btn.addEventListener('click', function() {
                            seleccionarClienteModal(cliente.id_cliente, cliente.nombres + ' ' + cliente.apellidos, cliente.nit, cliente.telefono);
                        });
                        tdAccion.appendChild(btn);
                        fila.appendChild(tdNit);
                        fila.appendChild(tdNombres);
                        fila.appendChild(tdApellidos);
                        fila.appendChild(tdTelefono);
                        fila.appendChild(tdAccion);
                        tbody.appendChild(fila);
                    });
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay clientes disponibles</td></tr>';
                }
            },
            error: function() {
                alert('Error al cargar clientes');
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
                tbody.innerHTML = '';
                if (result && result.length > 0) {
                    result.forEach(function(producto) {
                        const fila = document.createElement('tr');
                        const tdCodigo = document.createElement('td');
                        tdCodigo.textContent = producto.codigo || '';
                        const tdNombre = document.createElement('td');
                        tdNombre.textContent = producto.producto;
                        const tdExistencia = document.createElement('td');
                        tdExistencia.textContent = producto.existencia || 0;
                        const tdPrecio = document.createElement('td');
                        tdPrecio.textContent = 'Q. ' + parseFloat(producto.precio_venta).toFixed(2);
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
                } else {
                    tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay productos disponibles</td></tr>';
                }
            },
            error: function() {
                alert('Error al cargar productos');
            }
        });
    }

    function seleccionarClienteModal(idCliente, nombre, nit, telefono) {
        document.getElementById('lblIdCliente').textContent = idCliente;
        document.getElementById('hiddenIdCliente').value = idCliente;
        document.getElementById('txtNombreCliente').value = nombre;
        document.getElementById('txtNitCliente').value = nit;
        document.getElementById('txtTelefonoCliente').value = telefono;
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalClientes'));
        if (modal) modal.hide();
    }

    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('btnBuscarCliente').addEventListener('click', buscarCliente);
        document.getElementById('btnBuscarProducto').addEventListener('click', buscarProducto);
        document.getElementById('btnListarClientes').addEventListener('click', cargarClientesModal);
        document.getElementById('btnListarProductos').addEventListener('click', cargarProductosModal);

        document.getElementById('txtNitCliente').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarCliente();
            }
        });

        document.getElementById('txtCodigoProducto').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarProducto();
            }
        });

        document.getElementById('formVenta').addEventListener('submit', function(e) {
            e.preventDefault();
            if (!document.getElementById('hiddenIdCliente').value) {
                alert('Seleccione cliente');
                return;
            }
            const filas = document.querySelectorAll('#grvProductosCompra tbody tr');
            if (filas.length === 0) {
                alert('Agregue productos');
                return;
            }
            filas.forEach((fila) => {
                const input1 = document.createElement('input');
                input1.type = 'hidden';
                input1.name = 'idProducto';
                input1.value = fila.cells[0].textContent;
                this.appendChild(input1);
                const input2 = document.createElement('input');
                input2.type = 'hidden';
                input2.name = 'cantidad';
                input2.value = fila.querySelector('.cantidad').value;
                this.appendChild(input2);
                const input3 = document.createElement('input');
                input3.type = 'hidden';
                input3.name = 'precioUnitario';
                input3.value = fila.cells[4].textContent.replace('Q. ', '');
                this.appendChild(input3);
            });
            this.submit();
        });

        const today = new Date().toISOString().split('T')[0];
        document.getElementById('txtFecha').value = today;
        actualizarTotales();
    });
</script>
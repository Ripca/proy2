package com.sistemaempresa.servlets;

import com.sistemaempresa.dao.CompraDAO;
import com.sistemaempresa.dao.ProveedorDAO;
import com.sistemaempresa.dao.ProductoDAO;
import com.sistemaempresa.models.Compra;
import com.sistemaempresa.models.CompraDetalle;
import com.sistemaempresa.models.Proveedor;
import com.sistemaempresa.models.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class CompraServlet extends HttpServlet {
    
    private CompraDAO compraDAO = new CompraDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listarCompras(request, response);
                break;
            case "new":
                mostrarFormularioNuevo(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarCompra(request, response);
                break;
            case "view":
                verCompra(request, response);
                break;
            default:
                listarCompras(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarCompra(request, response);
        } else if ("update".equals(action)) {
            actualizarCompra(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    private void listarCompras(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Compra> compras = compraDAO.obtenerTodos();
        request.setAttribute("compras", compras);
        request.getRequestDispatcher("/WEB-INF/views/compras/list_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar datos para los dropdowns
        List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
        List<Producto> productos = productoDAO.obtenerTodos();
        
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("productos", productos);
        
        request.getRequestDispatcher("/WEB-INF/views/compras/form_template.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idCompra = Integer.parseInt(request.getParameter("id"));
        Compra compra = compraDAO.obtenerPorId(idCompra);
        
        if (compra != null) {
            // Cargar datos para los dropdowns
            List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
            List<Producto> productos = productoDAO.obtenerTodos();
            
            request.setAttribute("compra", compra);
            request.setAttribute("proveedores", proveedores);
            request.setAttribute("productos", productos);
            
            request.getRequestDispatcher("/WEB-INF/views/compras/form_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("CompraServlet?error=Compra no encontrada");
        }
    }
    
    private void verCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idCompra = Integer.parseInt(request.getParameter("id"));
        Compra compra = compraDAO.obtenerPorId(idCompra);
        
        if (compra != null) {
            request.setAttribute("compra", compra);
            request.getRequestDispatcher("/WEB-INF/views/compras/view_template.jsp").forward(request, response);
        } else {
            response.sendRedirect("CompraServlet?error=Compra no encontrada");
        }
    }
    
    private void guardarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener datos de la compra
            int noOrdenCompra = Integer.parseInt(request.getParameter("noOrdenCompra"));
            LocalDate fechaOrden = LocalDate.parse(request.getParameter("fechaOrden"));
            int idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
            
            Compra compra = new Compra(noOrdenCompra, fechaOrden, idProveedor);
            
            // Obtener detalles de la compra
            String[] productosIds = request.getParameterValues("idProducto");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] precios = request.getParameterValues("precioCostoUnitario");
            
            if (productosIds != null && cantidades != null && precios != null) {
                for (int i = 0; i < productosIds.length; i++) {
                    if (!productosIds[i].isEmpty() && !cantidades[i].isEmpty() && !precios[i].isEmpty()) {
                        CompraDetalle detalle = new CompraDetalle();
                        detalle.setIdProducto(Integer.parseInt(productosIds[i]));
                        detalle.setCantidad(Integer.parseInt(cantidades[i]));
                        detalle.setPrecioCostoUnitario(Double.parseDouble(precios[i]));
                        compra.agregarDetalle(detalle);
                    }
                }
            }
            
            // Usar el método crear() que implementa la lógica del C#
            int idCompra = compraDAO.crear(compra);
            if (idCompra > 0) {
                response.sendRedirect("CompraServlet?success=Compra creada exitosamente con ID: " + idCompra);
            } else {
                response.sendRedirect("CompraServlet?error=Error al crear la compra");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CompraServlet?error=Error al procesar los datos: " + e.getMessage());
        }
    }
    
    private void actualizarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idCompra = Integer.parseInt(request.getParameter("idCompra"));
            int noOrdenCompra = Integer.parseInt(request.getParameter("noOrdenCompra"));
            LocalDate fechaOrden = LocalDate.parse(request.getParameter("fechaOrden"));
            int idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
            
            Compra compra = new Compra(noOrdenCompra, fechaOrden, idProveedor);
            compra.setIdCompra(idCompra);
            
            if (compraDAO.actualizar(compra)) {
                response.sendRedirect("CompraServlet?success=Compra actualizada exitosamente");
            } else {
                response.sendRedirect("CompraServlet?error=Error al actualizar la compra");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CompraServlet?error=Error al procesar los datos: " + e.getMessage());
        }
    }
    
    private void eliminarCompra(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idCompra = Integer.parseInt(request.getParameter("id"));
            
            if (compraDAO.eliminar(idCompra)) {
                response.sendRedirect("CompraServlet?success=Compra eliminada exitosamente");
            } else {
                response.sendRedirect("CompraServlet?error=Error al eliminar la compra");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CompraServlet?error=Error al procesar la eliminación: " + e.getMessage());
        }
    }
}

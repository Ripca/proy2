package com.sistemaempresa.reportes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.sistemaempresa.config.DatabaseConnection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoReporte = request.getParameter("tipo");
        System.out.println("=== INICIANDO REPORTE ===");
        System.out.println("Tipo: " + tipoReporte);

        Connection conn = null;
        try {
            // 1. Obtener conexión
            conn = DatabaseConnection.getConnection();
            System.out.println("Conexión BD: " + (conn != null ? "OK" : "ERROR"));

            if (conn == null) {
                response.getWriter().println("Error: No hay conexión a BD");
                return;
            }

            // 2. Determinar qué reporte generar
            String reportName = "miReporte.jrxml";
            String fileName = "reporte.pdf";
            String titulo = "Reporte - Sistema Empresa";

            if ("inventario".equals(tipoReporte)) {
                reportName = "miReporte.jrxml";
                fileName = "reporte_inventario.pdf";
                titulo = "Reporte de Inventario de Productos";
            } else if ("productos".equals(tipoReporte)) {
                // TODO: Crear reporte de productos
                reportName = "miReporte.jrxml";
                fileName = "reporte_productos.pdf";
                titulo = "Reporte de Productos";
            } else if ("empleados".equals(tipoReporte)) {
                // TODO: Crear reporte de empleados
                reportName = "miReporte.jrxml";
                fileName = "reporte_empleados.pdf";
                titulo = "Reporte de Empleados";
            } else if ("clientes".equals(tipoReporte)) {
                // TODO: Crear reporte de clientes
                reportName = "miReporte.jrxml";
                fileName = "reporte_clientes.pdf";
                titulo = "Reporte de Clientes";
            } else if ("factura_venta".equals(tipoReporte)) {
                // TODO: Crear reporte de factura de venta
                reportName = "miReporte.jrxml";
                fileName = "factura_venta.pdf";
                titulo = "Factura de Venta";
            } else if ("factura_compra".equals(tipoReporte)) {
                // TODO: Crear reporte de factura de compra
                reportName = "miReporte.jrxml";
                fileName = "factura_compra.pdf";
                titulo = "Factura de Compra";
            } else if ("ventas".equals(tipoReporte)) {
                reportName = "miReporte.jrxml";
                fileName = "reporte_ventas.pdf";
                titulo = "Reporte de Ventas";
            } else if ("compras".equals(tipoReporte)) {
                reportName = "miReporte.jrxml";
                fileName = "reporte_compras.pdf";
                titulo = "Reporte de Compras";
            }

            // 3. Cargar el reporte
            InputStream reportStream = getClass()
                .getResourceAsStream(reportName);

            System.out.println("Report stream: " + (reportStream != null ? "OK" : "NULL"));

            if (reportStream == null) {
                response.setContentType("text/html");
                response.getWriter().println("<h3>Error: Archivo JRXML no encontrado</h3>");
                response.getWriter().println("<p>Buscando en: " + getClass().getPackage().getName() + "/" + reportName + "</p>");
                return;
            }

            // 4. Parámetros
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", titulo);

            // 5. Compilar el reporte JRXML a JasperReport
            System.out.println("Compilando JRXML...");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            System.out.println("JRXML compilado OK");

            // 6. Generar reporte
            System.out.println("Generando JasperPrint...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn);
            System.out.println("JasperPrint generado OK");

            // 7. Configurar respuesta
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 8. Exportar a PDF
            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();

            System.out.println("=== REPORTE GENERADO EXITOSAMENTE ===");

        } catch (Exception e) {
            System.out.println("=== ERROR EN REPORTE ===");
            e.printStackTrace();

            response.setContentType("text/html");
            response.getWriter().println("<h3>Error generando reporte:</h3>");
            response.getWriter().println("<pre>");
            e.printStackTrace(response.getWriter());
            response.getWriter().println("</pre>");
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
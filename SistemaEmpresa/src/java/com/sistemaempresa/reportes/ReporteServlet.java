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
            
            // 2. Cargar el reporte - MISMO PACKAGE QUE EL SERVLET
            InputStream reportStream = getClass()
                .getResourceAsStream("miReporte.jrxml"); // SIN RUTA, MISMO PAQUETE
            
            System.out.println("Report stream: " + (reportStream != null ? "OK" : "NULL"));
            
            if (reportStream == null) {
                response.setContentType("text/html");
                response.getWriter().println("<h3>Error: Archivo JRXML no encontrado</h3>");
                response.getWriter().println("<p>Buscando en: " + getClass().getPackage().getName() + "/miReporte.jrxml</p>");
                return;
            }
            
            // 3. Parámetros
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", "Reporte de Ventas - Sistema Empresa");
            
            // 4. Generar reporte
            System.out.println("Generando JasperPrint...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parametros, conn);
            System.out.println("JasperPrint generado OK");
            
            // 5. Configurar respuesta
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte_ventas.pdf");
            
            // 6. Exportar a PDF
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
    
  /*  protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Aquí manejas la lógica del botón
        System.out.println("Botón presionado: Generar Reporte");
        // Lógica para generar el reporte...
        
        String report="C:\\Users\\VELA\\Desktop\\ProyectoFinalPro\\proy2\\SistemaEmpresa\\src\\java\\com\\sistemaempresa\\reportes\\reporte2.jrxml";
        JasperReport jr;
        
        try(Connection con=DatabaseConnection.getConnection()){
            jr=JasperCompileManager.compileReport(report);
            JasperPrint jp=JasperFillManager.fillReport(jr,null,con);
            JasperViewer.viewReport(jp);
            
        }catch(Exception e){}
        
        
        //request.getRequestDispatcher("resultado.jsp").forward(request, response);
    }*/
    
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    System.out.println("Botón presionado: Generar Reporte");

    String reportPath = "C:\\Users\\VELA\\Desktop\\ProyectoFinalPro\\proy2\\SistemaEmpresa\\src\\java\\com\\sistemaempresa\\reportes\\reporte2.jrxml";
    JasperReport jasperReport;

    try (Connection con = DatabaseConnection.getConnection()) {

        // Compilar el reporte .jrxml a .jasper (si no existe ya compilado)
        jasperReport = JasperCompileManager.compileReport(reportPath);

        // Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, con);

        // OPCIÓN 1: Mostrar en visor (solo si estás depurando localmente)
        // JasperViewer.viewReport(jasperPrint, false);

        // OPCIÓN 2: Exportar a PDF y enviarlo al navegador
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_ventas.pdf");

        try (OutputStream out = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();
        }

        System.out.println("=== REPORTE PDF ENVIADO AL NAVEGADOR ===");

        // Redirigir de nuevo a la página original (opcional)
        // Si deseas que el usuario regrese a la página del botón después de generar el reporte:
        // response.sendRedirect("paginaOriginal.jsp");

    } catch (Exception e) {
        e.printStackTrace();
        response.setContentType("text/html");
        response.getWriter().println("<h3>Error generando reporte:</h3>");
        response.getWriter().println("<pre>");
        e.printStackTrace(response.getWriter());
        response.getWriter().println("</pre>");
    }
}

    
}
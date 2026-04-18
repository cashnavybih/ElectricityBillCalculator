package com.naveed;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/**
 * ElectricityBillServlet
 * Problem 14 – Electricity Bill Calculator
 * Concept Practiced: doGet / doPost
 *
 * Slab Rates:
 *   First 100 units  -> Rs. 2 / unit
 *   Next  100 units  -> Rs. 3 / unit
 *   Above 200 units  -> Rs. 5 / unit
 *   Fixed charge     -> Rs. 50 per month
 *
 * Author : Naveed Manikpuri
 */
@WebServlet("/ElectricityBillServlet")
public class ElectricityBillServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ------------------------------------------------------------------ GET
    // Redirect any direct GET request back to the form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");
    }

    // ----------------------------------------------------------------- POST
    // Called when the HTML form is submitted
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        /* ---- Step 1: Read form parameters ---- */
        String name    = request.getParameter("name");
        String prevStr = request.getParameter("prevReading");
        String currStr = request.getParameter("currReading");

        /* ---- Step 2: Input Validation ---- */
        if (name == null || name.trim().isEmpty()
                || prevStr == null || prevStr.trim().isEmpty()
                || currStr == null || currStr.trim().isEmpty()) {
            sendError(out, "All fields are required. Please go back and fill in the form.");
            return;
        }

        int prevReading, currReading;
        try {
            // Use int as specified in the coding hint
            prevReading = Integer.parseInt(prevStr.trim());
            currReading = Integer.parseInt(currStr.trim());
        } catch (NumberFormatException e) {
            sendError(out, "Meter readings must be whole numbers. Please go back and correct them.");
            return;
        }

        if (prevReading < 0 || currReading < 0) {
            sendError(out, "Meter readings cannot be negative. Please go back and correct them.");
            return;
        }

        if (currReading < prevReading) {
            sendError(out, "Current reading cannot be less than previous reading. Please go back and correct them.");
            return;
        }

        /* ---- Step 3: Calculate units consumed ---- */
        int unitsConsumed = currReading - prevReading;

        /* ---- Step 4: Slab-wise cost calculation ---- */
        // Separate variable for each slab cost (as per coding hint)
        int    slab1Units = 0;  double slab1Cost = 0;
        int    slab2Units = 0;  double slab2Cost = 0;
        int    slab3Units = 0;  double slab3Cost = 0;
        double fixedCharge = 50.0; // Fixed monthly charge

        int remaining = unitsConsumed;

        // Slab 1: first 100 units at Rs.2/unit
        if (remaining > 0) {
            slab1Units = Math.min(remaining, 100);
            slab1Cost  = slab1Units * 2.0;
            remaining -= slab1Units;
        }

        // Slab 2: next 100 units (101-200) at Rs.3/unit
        if (remaining > 0) {
            slab2Units = Math.min(remaining, 100);
            slab2Cost  = slab2Units * 3.0;
            remaining -= slab2Units;
        }

        // Slab 3: above 200 units at Rs.5/unit
        if (remaining > 0) {
            slab3Units = remaining;
            slab3Cost  = slab3Units * 5.0;
        }

        double energyCharge = slab1Cost + slab2Cost + slab3Cost;
        double totalBill    = energyCharge + fixedCharge;

        /* ---- Step 5: Build itemised HTML bill ---- */
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Electricity Bill - " + escapeHtml(name.trim()) + "</title>");
        out.println("<style>");
        out.println("*{box-sizing:border-box;margin:0;padding:0}");
        out.println("body{font-family:'Segoe UI',sans-serif;background:linear-gradient(135deg,#1a1a2e,#16213e,#0f3460);min-height:100vh;display:flex;align-items:center;justify-content:center;padding:20px}");
        out.println(".card{background:#fff;border-radius:16px;box-shadow:0 20px 60px rgba(0,0,0,.3);padding:40px;width:100%;max-width:560px}");
        out.println(".header{text-align:center;margin-bottom:28px}");
        out.println(".icon{font-size:48px;display:block;margin-bottom:10px}");
        out.println("h1{color:#1a1a2e;font-size:22px;font-weight:700}");
        out.println(".consumer{background:#f0f4ff;border-radius:8px;padding:12px 16px;margin-bottom:20px;font-size:14px;color:#333}");
        out.println(".consumer span{font-weight:700;color:#0f3460}");
        out.println("table{width:100%;border-collapse:collapse;margin-bottom:20px;font-size:14px}");
        out.println("th{background:#0f3460;color:#fff;padding:11px 14px;text-align:left}");
        out.println("td{padding:10px 14px;border-bottom:1px solid #eee}");
        out.println("tr:nth-child(even) td{background:#f8f9ff}");
        out.println(".total-row td{font-weight:700;font-size:16px;color:#0f3460;background:#e8eeff!important;border-top:2px solid #0f3460}");
        out.println(".btn{display:block;text-align:center;margin-top:4px;padding:13px;background:linear-gradient(135deg,#0f3460,#533483);color:#fff;border-radius:8px;text-decoration:none;font-weight:600;font-size:15px}");
        out.println("</style></head><body>");
        out.println("<div class='card'>");
        out.println("  <div class='header'><span class='icon'>🧾</span><h1>Electricity Bill Statement</h1></div>");
        out.println("  <div class='consumer'>");
        out.println("    Consumer: <span>" + escapeHtml(name.trim()) + "</span> &nbsp;|&nbsp;");
        out.println("    Prev Reading: <span>" + prevReading + "</span> &nbsp;|&nbsp;");
        out.println("    Curr Reading: <span>" + currReading + "</span> &nbsp;|&nbsp;");
        out.println("    Units Consumed: <span>" + unitsConsumed + "</span>");
        out.println("  </div>");
        out.println("  <table>");
        out.println("    <tr><th>Component</th><th>Units</th><th>Rate</th><th>Amount (&#8377;)</th></tr>");
        out.println("    <tr><td>Slab 1 &nbsp;(1 – 100 units)</td><td>" + slab1Units + "</td><td>&#8377;2 / unit</td><td>&#8377;" + String.format("%.2f", slab1Cost) + "</td></tr>");
        out.println("    <tr><td>Slab 2 &nbsp;(101 – 200 units)</td><td>" + slab2Units + "</td><td>&#8377;3 / unit</td><td>&#8377;" + String.format("%.2f", slab2Cost) + "</td></tr>");
        out.println("    <tr><td>Slab 3 &nbsp;(above 200 units)</td><td>" + slab3Units + "</td><td>&#8377;5 / unit</td><td>&#8377;" + String.format("%.2f", slab3Cost) + "</td></tr>");
        out.println("    <tr><td colspan='3'><strong>Energy Charges Sub-total</strong></td><td><strong>&#8377;" + String.format("%.2f", energyCharge) + "</strong></td></tr>");
        out.println("    <tr><td colspan='3'>Fixed Monthly Charge</td><td>&#8377;" + String.format("%.2f", fixedCharge) + "</td></tr>");
        out.println("    <tr class='total-row'><td colspan='3'>&#9889; TOTAL BILL AMOUNT</td><td>&#8377;" + String.format("%.2f", totalBill) + "</td></tr>");
        out.println("  </table>");
        out.println("  <a href='index.html' class='btn'>&#8592; Calculate Another Bill</a>");
        out.println("</div></body></html>");
    }

    /* ---- Utility: basic HTML escaping to prevent XSS ---- */
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /* ---- Utility: styled error page ---- */
    private void sendError(PrintWriter out, String message) {
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Input Error</title>");
        out.println("<style>body{font-family:sans-serif;display:flex;align-items:center;justify-content:center;min-height:100vh;margin:0;background:#1a1a2e}");
        out.println(".box{background:#fff;border-radius:12px;padding:36px;max-width:420px;text-align:center}");
        out.println("h2{color:#c0392b;margin-bottom:14px}p{color:#555;margin-bottom:20px}");
        out.println("a{color:#fff;background:#0f3460;padding:10px 24px;border-radius:8px;text-decoration:none;font-weight:600}</style></head><body>");
        out.println("<div class='box'><h2>&#9888; Input Error</h2><p>" + message + "</p><a href='index.html'>Go Back</a></div></body></html>");
    }
}

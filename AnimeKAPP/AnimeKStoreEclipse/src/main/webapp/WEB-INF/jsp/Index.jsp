<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ page import ="java.util.Set"
import ="com.mongodb.MongoException"
import ="com.mongodb.DB"
import ="com.mongodb.MongoClient" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> <html> 
     <head>
     <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"> 
     <title> Test Page</title>
     </head> 
     <body> 
   
     
  <table class="GeneratedTable"  >
  <thead>
    <tr>
      
      <th>Descripcion</th>
      <th>Precio</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="productos" items="${collection}">
    <tr>
      <td>${productos}</td>
      
    </tr>
    </c:forEach>
  </tbody>
  
</table>
     </body>
     </html>
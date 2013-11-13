<%
  final String queryString = request.getQueryString();
  final String redirectURL = "org.kie.wires.WiresShowcase/Wires.html" + (queryString == null ? "" : "?" + queryString);
  response.sendRedirect(redirectURL);
%>

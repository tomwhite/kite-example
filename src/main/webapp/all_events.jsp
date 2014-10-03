<%--
  Copyright 2013 Cloudera Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<html>
<head>
<title>All events</title>
<link rel="stylesheet" type="text/css" href="report.css">
<head>
<body>
<h2>All events</h2>
<p>
<sql:query var='queryResults' dataSource="jdbc/hive">
  select * from events
</sql:query>
<%@ include file="display_query_results.jspf" %>
<p>
</form>
<p><a href="index.jsp">Home</a> | <a href="all_events.jsp">All events</a></p>
</body>
</html>

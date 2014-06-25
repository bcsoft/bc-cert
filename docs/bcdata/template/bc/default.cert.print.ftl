<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>证件打印</title>
</head>
<body>
<#list certs as m>
<#if m_index > 0><br/></#if><img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']}mm;">
</#list>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>证件打印</title>
	<style type="text/css">
		img{margin: 2px;}
		div{margin: 2px; color: red; font-weight: bold;}
	</style>
</head>
<body>
<#if certs?size == 0><div>找不到符合条件的证件</div></#if>
<#list certs as m>
	<#if (m_index > 0)><br/></#if>
	<#if m['attachId']??>
		<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number / 1.059}mm;">
	<#else>
		<div>${m['subject']} 未上传</div>
	</#if>
</#list>
</body>
</html>